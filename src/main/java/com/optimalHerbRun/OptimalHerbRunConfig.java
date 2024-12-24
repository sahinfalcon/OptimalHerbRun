package com.optimalHerbRun;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("optimalHerbRun")
public interface OptimalHerbRunConfig extends Config
{
	@ConfigItem(
			keyName = "notifyReady",
			name = "Notify When Ready",
			description = "Notify when herbs are ready to harvest",
			position = 1
	)
	default boolean notifyReady()
	{
		return true;
	}

	@ConfigItem(
			keyName = "profitThreshold",
			name = "Profit Threshold",
			description = "Minimum profit threshold for run notification (k)",
			position = 2
	)
	default int profitThreshold()
	{
		return 50;
	}
}