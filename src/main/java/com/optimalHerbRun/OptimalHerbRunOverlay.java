package com.optimalHerbRun;

import com.optimalHerbRun.data.HerbPatch;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.api.coords.WorldPoint;
import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Color;

class OptimalHerbRunOverlay extends OverlayPanel
{
    private final OptimalHerbRunPlugin plugin;
    private final OptimalHerbRunConfig config;

    // Define patch locations and names
    private static final String getPatchName(WorldPoint point) {
        // Falador patch
        if (point.getX() == 3058 && point.getY() == 3311) {
            return "Falador";
        }
        // Ardougne patch
        if (point.getX() == 2670 && point.getY() == 3374) {
            return "Ardougne";
        }
        // Catherby patch
        if (point.getX() == 2813 && point.getY() == 3463) {
            return "Catherby";
        }
        // Morytania patch
        if (point.getX() == 3606 && point.getY() == 3529) {
            return "Morytania";
        }
        // Farming Guild patch
        if (point.getX() == 1239 && point.getY() == 3726) {
            return "Farming Guild";
        }
        // TODO: add the rest of the farming patch locations
        return "Unknown";
    }

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

        // List each found patch
        for (WorldPoint location : plugin.getPatches().keySet()) {
            HerbPatch patch = plugin.getPatches().get(location);
            String patchName = getPatchName(location);
            panelComponent.getChildren().add(
                    LineComponent.builder()
                            .left(patchName)
                            .right(patch.getGrowthStage())  // Show growth stage
                            .rightColor(patch.getGrowthStage().equals("Ready") ? Color.GREEN : Color.WHITE)
                            .build()
            );
        }

        return super.render(graphics);
    }
}