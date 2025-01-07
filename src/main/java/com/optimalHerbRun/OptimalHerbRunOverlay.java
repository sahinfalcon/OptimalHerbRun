package com.optimalHerbRun;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.api.Client;
import com.optimalHerbRun.data.HerbPatch;
import com.optimalHerbRun.data.PatchStatus;
import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Color;

class OptimalHerbRunOverlay extends OverlayPanel {
    private final OptimalHerbRunPlugin plugin;
    private final OptimalHerbRunConfig config;
    private final Client client;

    @Inject
    private OptimalHerbRunOverlay(OptimalHerbRunPlugin plugin, OptimalHerbRunConfig config, Client client) {
        super();
        setPosition(OverlayPosition.TOP_LEFT);
        this.plugin = plugin;
        this.config = config;
        this.client = client;
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
            // Get base display text
            String displayText = patch.getDisplayState();

            // Add compost warning if needed
            if (patch.getDisplayState().contains("Growing") &&
                    !patch.isProtected() &&
                    !patch.isDiseaseFree(client)) {
                displayText += " (needs compost)";
            }

            // Get appropriate color for state
            Color stateColor = patch.getStatus().getColor();

            panelComponent.getChildren().add(
                    LineComponent.builder()
                            .left(patch.getLocationName() + ":")
                            .right(displayText)
                            .rightColor(stateColor)
                            .build()
            );
        }

        return super.render(graphics);
    }
}