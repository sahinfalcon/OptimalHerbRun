package com.optimalHerbRun;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import com.optimalHerbRun.data.HerbPatch;
import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Color;

class OptimalHerbRunOverlay extends OverlayPanel {
    private final OptimalHerbRunPlugin plugin;
    private final OptimalHerbRunConfig config;

    @Inject
    private OptimalHerbRunOverlay(OptimalHerbRunPlugin plugin, OptimalHerbRunConfig config) {
        super();
        setPosition(OverlayPosition.TOP_LEFT);
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Herb Run:")
                        .right("Active")
                        .rightColor(Color.GREEN)
                        .build()
        );

        for (HerbPatch patch : plugin.getPatches().values()) {
            panelComponent.getChildren().add(
                    LineComponent.builder()
                            .left(patch.getLocationName() + ":")
                            .right(patch.getDisplayState())
                            .rightColor(patch.getStatus().getColor())
                            .build()
            );
        }

        return super.render(graphics);
    }
}