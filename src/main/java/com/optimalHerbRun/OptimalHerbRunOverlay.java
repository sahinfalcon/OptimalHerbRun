package com.optimalHerbRun;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;

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
        // Add a simple test line to verify overlay works
        panelComponent.getChildren().add(
                LineComponent.builder()
                        .left("Herb Run:")
                        .right("Active")
                        .build()
        );

        return super.render(graphics);
    }
}