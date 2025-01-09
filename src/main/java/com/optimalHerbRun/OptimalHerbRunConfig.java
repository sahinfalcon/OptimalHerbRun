package com.optimalHerbRun;

import net.runelite.client.config.*;

import java.awt.Color;

@SuppressWarnings("SameReturnValue")
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
	@ConfigSection(
			name = "Colors",
			description = "Configure colors for different patch states",
			position = 0
	)
	String colorsSection = "colors";

	@Alpha
	@ConfigItem(
			keyName = "readyColor",
			name = "Ready Color",
			description = "Color for patches ready to harvest",
			section = colorsSection
	)
	default Color readyColor() {
		return Color.GREEN;
	}

	@SuppressWarnings("SameReturnValue")
	@Alpha
	@ConfigItem(
			keyName = "growingColor",
			name = "Growing Color",
			description = "Color for growing patches",
			section = colorsSection
	)
	default Color growingColor() {
		return Color.WHITE;
	}

	@Alpha
	@ConfigItem(
			keyName = "deadColor",
			name = "Dead Color",
			description = "Color for dead patches",
			section = colorsSection
	)
	default Color deadColor() {
		return Color.RED;
	}
}