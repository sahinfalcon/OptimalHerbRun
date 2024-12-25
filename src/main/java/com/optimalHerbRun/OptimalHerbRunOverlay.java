package com.optimalHerbRun;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.api.coords.WorldPoint;
import com.optimalHerbRun.data.HerbPatch;
import com.optimalHerbRun.data.HerbPatchLocation;
import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Map;

class OptimalHerbRunOverlay extends OverlayPanel
{
    private final OptimalHerbRunPlugin plugin;
    private final OptimalHerbRunConfig config;

    @Inject
    private OptimalHerbRunOverlay(OptimalHerbRunPlugin plugin, OptimalHerbRunConfig config)
    {
        super();
        setPosition(OverlayPosition.TOP_LEFT);
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Herb Run:")
                        .right("Active")
                        .rightColor(Color.GREEN)
                        .build()
        );

        // Show total patches found
        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Patches Found:")
                        .right(String.valueOf(plugin.getPatches().size()))
                        .build()
        );

        // List each found patch with its state
        for (Map.Entry<WorldPoint, HerbPatch> entry : plugin.getPatches().entrySet()) {
            WorldPoint location = entry.getKey();
            HerbPatch patch = entry.getValue();
            String patchName = HerbPatchLocation.getNameForLocation(location);

            Color stateColor = Color.WHITE;
            if ("Ready".equals(patch.getGrowthStage())) {
                stateColor = Color.GREEN;
            } else if ("Dead".equals(patch.getGrowthStage())) {
                stateColor = Color.RED;
            }

            panelComponent.getChildren().add(
                    LineComponent.builder()
                            .left(patchName)
                            .right(patch.getGrowthStage())
                            .rightColor(stateColor)
                            .build()
            );
        }

        return super.render(graphics);
    }
}