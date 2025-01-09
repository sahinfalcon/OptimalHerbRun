package com.optimalHerbRun;

import com.google.inject.Provides;
import javax.inject.Inject;

import com.optimalHerbRun.data.HerbType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ChatMessageType;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.callback.ClientThread;
import com.optimalHerbRun.data.HerbPatch;
import com.optimalHerbRun.data.HerbPatchLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
        name = "Optimal Herb Run",
        description = "Optimises herb runs by tracking patches and suggesting routes",
        tags = {"farming", "herbs", "tracking", "efficiency"}
)
public class OptimalHerbRunPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private OptimalHerbRunConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private OptimalHerbRunOverlay overlay;

    private static final Pattern INSPECT_HERB_TYPE = Pattern.compile(
            ".*The patch has (.*?) leaf growing in it and is at state.*");
    private static final Pattern HERB_PLANTED = Pattern.compile(
            "You plant (?:a |an )?(?<herbType>.*?) seed in the herb patch\\.");

    // Pattern for detecting compost application
    private static final Pattern COMPOST_USED_ON_PATCH = Pattern.compile(
            "You treat the .+ with (?<compostType>ultra|super|)compost\\.");


    private static final Pattern INSPECT_PATCH = Pattern.compile(
            "This is an? .+\\. The soil has been treated with (?<compostType>ultra|super|)compost\\..*");

    // Store all herb patch IDs we want to track
    private static final Set<Integer> HERB_PATCH_IDS = Arrays.stream(HerbPatchLocation.values())
            .map(HerbPatchLocation::getObjectId)
            .collect(Collectors.toSet());

    // Store active patches and their states
    @Getter
    private final Map<WorldPoint, HerbPatch> patches = new HashMap<>();

    @Override
    protected void startUp()
    {
        overlayManager.add(overlay);
        log.info("OptimalHerbRun started!");
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        patches.clear();
        log.info("OptimalHerbRun stopped!");
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        GameObject gameObject = event.getGameObject();
        if (HERB_PATCH_IDS.contains(gameObject.getId())) {
            WorldPoint location = gameObject.getWorldLocation();
            if (!patches.containsKey(location)) {
                HerbPatch patch = new HerbPatch(location);
                patches.put(location, patch);
                log.info("Found {} herb patch at: {}",
                        patch.getLocationName(), location);
            }
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        int varbitId = event.getVarbitId();
        if (varbitId == 4774) {
            WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
            for (Map.Entry<WorldPoint, HerbPatch> entry : patches.entrySet()) {
                if (entry.getKey().distanceTo(playerLocation) < 10) {
                    HerbPatch patch = entry.getValue();
                    patch.updateState(event.getValue());
                    log.info("Updated state for {} patch: {}",
                            patch.getLocationName(),
                            patch.getDisplayState());
                }
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            clientThread.invokeLater(this::checkNearbyPatchStates);
        }
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        checkNearbyPatchStates();
    }

    @Subscribe
    public void onChatMessage(ChatMessage message) {
        if (message.getType() != ChatMessageType.SPAM && message.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }

        String messageString = message.getMessage();
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        HerbPatch nearestPatch = getNearestPatch(playerLocation);

        if (nearestPatch == null) {
            return;
        }

        // Check for compost application/inspection
        if (COMPOST_USED_ON_PATCH.matcher(messageString).matches() ||
                INSPECT_PATCH.matcher(messageString).matches())
        {
            nearestPatch.setProtected(true);
            log.info("Detected compost on {} patch", nearestPatch.getLocationName());
        }

        // Check for herb planting
        var herbPlanted = HERB_PLANTED.matcher(messageString);
        if (herbPlanted.matches()) {
            String herbName = herbPlanted.group("herbType");
            try {
                nearestPatch.setHerbType(HerbType.valueOf(herbName.toUpperCase().replace(" ", "_")));
                log.info("Planted {} in {} patch", herbName, nearestPatch.getLocationName());
            } catch (IllegalArgumentException e) {
                log.warn("Unknown herb type: {}", herbName);
            }
        }

        // Check for herb type from inspection
        var inspectHerb = INSPECT_HERB_TYPE.matcher(messageString);
        if (inspectHerb.matches()) {
            String herbName = inspectHerb.group(1);  // Gets "Guam" from "Guam leaf"
            try {
                nearestPatch.setHerbType(HerbType.valueOf(herbName.toUpperCase()));
                log.info("Found {} in {} patch from inspection", herbName, nearestPatch.getLocationName());
            } catch (IllegalArgumentException e) {
                log.warn("Unknown herb type from inspection: {}", herbName);
            }
        }
    }

    private HerbPatch getNearestPatch(WorldPoint playerLocation) {
        return patches.entrySet().stream()
                .filter(e -> e.getKey().distanceTo(playerLocation) < 10)
                .min((e1, e2) ->
                        Integer.compare(
                                e1.getKey().distanceTo(playerLocation),
                                e2.getKey().distanceTo(playerLocation)
                        ))
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    private void checkNearbyPatchStates() {
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        for (Map.Entry<WorldPoint, HerbPatch> entry : patches.entrySet()) {
            if (entry.getKey().distanceTo(playerLocation) < 10) {
                HerbPatch patch = entry.getValue();
                int value = client.getVarbitValue(4774);
                patch.updateState(value);
                log.info("Checking {} patch, Current state: {} due to value: {}",
                        patch.getLocationName(),
                        patch.getDisplayState(),
                        value);
            }
        }
    }

    @Provides
    OptimalHerbRunConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(OptimalHerbRunConfig.class);
    }
}