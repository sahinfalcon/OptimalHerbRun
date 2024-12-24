package com.optimalHerbRun;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
		name = "Optimal Herb Run",
		description = "Optimises herb runs by tracking patches and suggesting routes",
		tags = {"farming", "herbs", "tracking", "efficiency"}
)
public class OptimalHerbRunPlugin extends Plugin
{
	@Inject
	private OptimalHerbRunConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Override
	protected void startUp()
	{
		// Plugin startup logic
	}

	@Override
	protected void shutDown()
	{
		// Plugin shutdown logic
	}

	@Provides
	OptimalHerbRunConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OptimalHerbRunConfig.class);
	}
}

