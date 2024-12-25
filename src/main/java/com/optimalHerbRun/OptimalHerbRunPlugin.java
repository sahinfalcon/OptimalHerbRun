package com.optimalHerbRun;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.api.coords.WorldPoint;
import com.optimalHerbRun.data.HerbPatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Getter
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
	private OptimalHerbRunConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private OptimalHerbRunOverlay overlay;

	private WorldPoint currentPatchLocation = null;

	private static final Set<Integer> HERB_PATCH_IDS = Set.of(
			8150,  // Falador patch
			8151,  // Ardougne patch
			8152,  // Catherby patch
			8153,  // Morytania patch
			27115  // Farming Guild patch
			// TODO: add the rest of the patch ids e.g troll stronghold, fremenik etc
	);

	// Overlay can access patch info
	// Store active patches and their state
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
				patches.put(location, new HerbPatch(location));
			}
			currentPatchLocation = location;  // Update current patch when detected
			log.info("Found herb patch at: {}", location);
		}
	}


	@Subscribe
	public void onVarbitChanged(VarbitChanged event) {
		int varbitId = event.getVarbitId();
		if (varbitId == 4774 && currentPatchLocation != null) {
			int value = event.getValue();
			String stage;
			switch (value) {
				case 0:
					// Ignore 0 value as it's when we're out of range
					return;
				case 3:
					stage = "Empty";
					break;
				case 4:
					stage = "Stage 1";
					break;
				case 5:
					stage = "Stage 2";
					break;
				case 6:
					stage = "Stage 3";
					break;
				case 7:
					stage = "Stage 4";
					break;
				case 8:
					stage = "Ready";
					break;
				case 9:
				case 10:
					stage = "Harvesting";
					break;
				case 170:
					stage = "Dead";
					break;
				default:
					stage = "Unknown";
			}

			// Only update current patch viewed
			HerbPatch patch = patches.get(currentPatchLocation);
			if (patch != null) {
				patch.setGrowthStage(stage);
				log.info("Updated growth stage for patch at {} to {} as value is now {}",
						currentPatchLocation, stage, value);
			}
		}
	}



	@Provides
	OptimalHerbRunConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OptimalHerbRunConfig.class);
	}

}