package com.amuletofbountyalerter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class AmuletOfBountyOverlay extends Overlay {
    private final Client client;
    private final AmuletOfBountyAlerterPlugin plugin;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    public AmuletOfBountyOverlay(Client client, AmuletOfBountyAlerterPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.BOTTOM_RIGHT); // Places it next to the inventory
        setLayer(OverlayLayer.ABOVE_WIDGETS); // Ensures it's visible over UI elements
    }

    @Inject
    private AmuletOfBountyAlerterConfig config;

    @Override
    public Dimension render(Graphics2D graphics) {
        // Always show the overlay if activeNearAllotment() is true
        if ((!config.onlyActiveNearAllotment() && !plugin.isWearingAmuletOfBounty()) || (plugin.nearAnAllotment() && !plugin.isWearingAmuletOfBounty())) {
            panelComponent.getChildren().clear();

            // Set overlay background color
            panelComponent.setBackgroundColor(new Color(255, 0, 0, 128)); // Red if missing amulet

            // Add message
            if (plugin.nearAnAllotment() && !plugin.isWearingAmuletOfBounty()) {
                panelComponent.getChildren().add(net.runelite.client.ui.overlay.components.TitleComponent.builder()
                        .text("You are not wearing an Amulet Of Bounty!")
                        .color(Color.WHITE)
                        .build());
            } else {
                panelComponent.getChildren().add(net.runelite.client.ui.overlay.components.TitleComponent.builder()
                        .text("You are not wearing an Amulet Of Bounty!")
                        .color(Color.WHITE)
                        .build());
            }

            return panelComponent.render(graphics);
        }

        return null;
    }
}
