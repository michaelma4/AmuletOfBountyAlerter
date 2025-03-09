package com.amuletofbountyalerter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class AmuletOfBountyOverlay extends Overlay {
    private final Client client;
    private final AmuletOfBountyAlerterPlugin plugin;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    public AmuletOfBountyOverlay(Client client, AmuletOfBountyAlerterPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
    }

    @Inject
    private AmuletOfBountyAlerterConfig config;

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();
        // Always show the overlay if activeNearAllotment() is true
        if ((!config.onlyActiveNearAllotment() && !plugin.isWearingAmuletOfBounty()) || (plugin.nearAnAllotment() && !plugin.isWearingAmuletOfBounty())) {

            // Ensure full-width background
            panelComponent.setPreferredSize(new Dimension(150, 0)); // Set a fixed width
            panelComponent.setBackgroundColor(new Color(255, 0, 0, 128));

            // Add message
            panelComponent.getChildren().add((LineComponent.builder())
                    .left("You are not wearing an Amulet Of Bounty!")
                    .build());

            setPosition(OverlayPosition.BOTTOM_RIGHT);
            return panelComponent.render(graphics);
        }

        return null;
    }
}
