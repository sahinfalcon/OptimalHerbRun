package com.optimalHerbRun;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
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

    // Convert enum to Set of IDs
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
            clientThread.invokeLater(() -> checkNearbyPatchStates());
        }
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        checkNearbyPatchStates();
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