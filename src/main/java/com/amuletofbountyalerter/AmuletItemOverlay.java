package com.amuletofbountyalerter;

import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.game.ItemManager;
import net.runelite.api.Client;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.api.ItemID;

import javax.inject.Inject;
import java.awt.*;

public class AmuletItemOverlay extends WidgetItemOverlay
{
    private final Client client;
    private final AmuletOfBountyAlerterPlugin plugin;
    private final ItemManager itemManager;

    @Inject
    private AmuletOfBountyAlerterConfig config;

    @Inject
    public AmuletItemOverlay(Client client, ItemManager itemManager, AmuletOfBountyAlerterPlugin plugin)
    {
        this.client = client;
        this.itemManager = itemManager;
        this.plugin = plugin;

        // Specify which inventory items to overlay
        showOnInventory();
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem itemWidget)
    {
        if (!config.highlightAmuletOfBountyInInventory()) {
            return;
        }

        if (plugin.isWearingAmuletOfBounty()) {
            return;
        }

        //If onlyActiveNearAllotment is enabled, only highlight the inventory item if player is near an allotment
        if ((itemId == ItemID.AMULET_OF_BOUNTY && config.onlyActiveNearAllotment() && plugin.nearAnAllotment()) ||
                (itemId == ItemID.AMULET_OF_BOUNTY && !config.onlyActiveNearAllotment()))
        {
            drawHighlight(graphics, itemWidget.getCanvasBounds(), Color.GREEN);
        }
    }

    private void drawHighlight(Graphics2D graphics, Rectangle bounds, Color color)
    {
        // Draw a semi-transparent overlay
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
        graphics.fill(bounds);

        // Draw an outline
        graphics.setColor(color);
        graphics.draw(bounds);
    }
}
