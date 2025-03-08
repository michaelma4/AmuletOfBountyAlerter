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
        setPosition(OverlayPosition.TOP_CENTER);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (plugin.nearAnAllotment() && !plugin.isWearingAmuletOfBounty()) {
            panelComponent.getChildren().clear();
            panelComponent.setBackgroundColor(new Color(255, 0, 0, 128)); // Semi-transparent red
            panelComponent.getChildren().add(net.runelite.client.ui.overlay.components.TitleComponent.builder()
                    .text("You are near an allotment without an Amulet of Bounty!")
                    .color(Color.WHITE)
                    .build());
            return panelComponent.render(graphics);
        }
        return null;
    }
}
