package com.amuletofbountyalerter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import net.runelite.api.*;
import net.runelite.api.events.ItemContainerChanged;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class AmuletOfBountyAlerterUnitTests {
    private AmuletOfBountyAlerterPlugin plugin;
    private Client client;

    @BeforeEach
    void setUp()
    {
        client = mock(Client.class);
        plugin = new AmuletOfBountyAlerterPlugin(client);
    }

    //Tests for isWearingAmuletOfBounty()
    @Test
    void testIsWearingAmuletOfBounty_WhenWearingAmuletOfBounty_ShouldReturnTrue()
    {
        // Mock the equipment container
        ItemContainer equipment = mock(ItemContainer.class);
        when(client.getItemContainer(InventoryID.EQUIPMENT)).thenReturn(equipment);

        // Create a real Item instance with the Amulet of Bounty's ID
        Item amuletOfBounty = new Item(ItemID.AMULET_OF_BOUNTY, 1);

        // Simulate that the Amulet of Bounty is in the amulet slot
        when(equipment.getItem(EquipmentInventorySlot.AMULET.getSlotIdx())).thenReturn(amuletOfBounty);

        // Assert that the plugin correctly detects the Amulet of Bounty
        assertTrue(plugin.isWearingAmuletOfBounty());
    }

    @Test
    void testIsWearingAmuletOfBounty_WhenNotWearingAmulet_ShouldReturnFalse()
    {
        // Mock an empty equipment container (user is not wearing an amulet)
        ItemContainer equipment = mock(ItemContainer.class);
        when(client.getItemContainer(InventoryID.EQUIPMENT)).thenReturn(equipment);

        // Mock that the amulet slot contains NO item (null)
        when(equipment.getItem(EquipmentInventorySlot.AMULET.getSlotIdx())).thenReturn(null);

        // Assert that the plugin detects the user is NOT wearing the Amulet of Bounty
        assertFalse(plugin.isWearingAmuletOfBounty());
    }

    @Test
    void testIsWearingAmuletOfBounty_WhenWearingAmuletOfGlory_ShouldReturnFalse()
    {
        // Mock the equipment container
        ItemContainer equipment = mock(ItemContainer.class);
        when(client.getItemContainer(InventoryID.EQUIPMENT)).thenReturn(equipment);

        // Create a real Item instance with the Amulet of Bounty's ID
        Item amuletOfBounty = new Item(ItemID.AMULET_OF_GLORY, 1);

        // Simulate that the Amulet of Bounty is in the amulet slot
        when(equipment.getItem(EquipmentInventorySlot.AMULET.getSlotIdx())).thenReturn(amuletOfBounty);

        // Assert that the plugin correctly detects the Amulet of Bounty
        assertFalse(plugin.isWearingAmuletOfBounty());
    }
}
