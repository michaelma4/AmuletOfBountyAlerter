package com.amuletofbountyalerter;

import com.google.inject.Provides;
import com.google.common.collect.ImmutableList;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import java.util.*;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.api.*;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@PluginDescriptor(
	name = "Amulet Of Bounty Alerter"
)
public class AmuletOfBountyAlerterPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private Notifier notifier;

	@Inject
	private AmuletOfBountyAlerterConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AmuletOfBountyOverlay amuletOfBountyOverlay;

	@Inject
	private AmuletItemOverlay amuletItemOverlay;

	private Map<Integer, Integer> previousInventory = new HashMap<>();
	public void setPreviousInventory(Map<Integer, Integer> inventory) {
		this.previousInventory = inventory;
	}

	/*
	 * I personally went to all these locations and handpicked the coordinates myself
	 * */
	private static final List<WorldArea> ALLOTMENT_AREAS = ImmutableList.of(
			new WorldArea(3044, 3299, 25, 18, 0), // South of Falador
			new WorldArea(3589, 3517, 30, 22, 0), // West of Port Phasmatys
			new WorldArea(2800, 3455, 21, 18, 0), // North of Catherby
			new WorldArea(2659, 3366, 18, 17, 0), // North of Ardougne
			new WorldArea(1726, 3543, 20, 23, 0), // South-west corner of Hosidius
			new WorldArea(3790, 2831, 13, 12, 0), // Harmony Island
			new WorldArea(1254, 3719, 26, 23, 0), // Farming Guild
			new WorldArea(3286, 6087, 12, 25, 0), // Prifddinas
			new WorldArea(1577, 3089, 22, 19, 0) // West of Civitas illa Fortis
	);

	@Override
	protected void startUp() throws Exception
	{
		log.info("Amulet Of Bounty Alerter started!");
		sendChatMessage("Amulet Of Bounty Alerter has been enabled!");
		overlayManager.add(amuletOfBountyOverlay);
		overlayManager.add(amuletItemOverlay);
	}

	private void sendChatMessage(String message) {
		String formattedMessage = new ChatMessageBuilder()
				.append(message)
				.build();

		chatMessageManager.queue(QueuedMessage.builder()
				.type(ChatMessageType.GAMEMESSAGE)
				.runeLiteFormattedMessage(formattedMessage)
				.build());
	}

	public void notifyUser()
	{
		notifier.notify("You are not wearing an Amulet of Bounty!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(amuletOfBountyOverlay); // Remove overlay when plugin stops
		overlayManager.remove(amuletItemOverlay);
		log.info("Amulet Of Bounty Alerter stopped!");
	}

	public boolean nearAnAllotment() {
		WorldPoint playerPos = client.getLocalPlayer().getWorldLocation();
		for(WorldArea worldArea: ALLOTMENT_AREAS)
		{
			if(playerPos.isInArea(worldArea))
			{
				return true;
			}
		}
		return false;
	}

	public boolean isWearingAmuletOfBounty() {
		ItemContainer equipment = client.getItemContainer(net.runelite.api.InventoryID.EQUIPMENT);
		if (equipment == null) return false;

		Item amulet = equipment.getItem(EquipmentInventorySlot.AMULET.getSlotIdx());
		return amulet != null && amulet.getId() == ItemID.AMULET_OF_BOUNTY;
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		// If user disabled notifications, don't perform any checks and don't send any notifications at all
		if (!config.notifyAfterPlantingWithoutAmuletOfBounty()) {
			return;
		}

		// Only track inventory changes
		if (event.getContainerId() != InventoryID.INVENTORY.getId())
		{
			return;
		}

		ItemContainer container = event.getItemContainer();
		if (container == null)
		{
			return;
		}

		// If first-time tracking, initialize previous inventory and return
		if (previousInventory.isEmpty())
		{
			previousInventory = getInventorySnapshot(container);
			return;
		}

		// List of seeds to track
		int[] seedIds = {
				ItemID.SNAPE_GRASS_SEED,
				ItemID.POTATO_SEED,
				ItemID.ONION_SEED,
				ItemID.CABBAGE_SEED,
				ItemID.TOMATO_SEED,
				ItemID.SWEETCORN_SEED,
				ItemID.STRAWBERRY_SEED,
				ItemID.WATERMELON_SEED
		};

		for (int seedId : seedIds) {
			int currentCount = countItem(container, seedId);
			int previousCount = previousInventory.getOrDefault(seedId, 0);

			if (currentCount < previousCount && nearAnAllotment()) {
				// A seed was planted, now check if Amulet of Bounty is equipped
				checkAmuletOfBounty();
				break;
			}
		}

		// Update inventory tracking
		previousInventory = getInventorySnapshot(container);
	}

	//Will send the user a notification for any scenario where they just planted allotment seeds but were not wearing
	// an amulet of bounty
	private void checkAmuletOfBounty()
	{
		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipment == null)
		{
			notifyUser();
			return;
		}

		int amuletSlot = EquipmentInventorySlot.AMULET.getSlotIdx();
		if (amuletSlot >= equipment.getItems().length || equipment.getItems()[amuletSlot] == null)
		{
			notifyUser();
			return;
		}

		int amuletId = equipment.getItems()[amuletSlot].getId();
		if (amuletId != ItemID.AMULET_OF_BOUNTY)
		{
			notifyUser();
		}
	}

	private int countItem(ItemContainer container, int itemId)
	{
		int count = 0;
		for (Item item : container.getItems())
		{
			if (item != null && item.getId() == itemId)
			{
				count += item.getQuantity();
			}
		}
		return count;
	}

	private Map<Integer, Integer> getInventorySnapshot(ItemContainer container)
	{
		Map<Integer, Integer> snapshot = new HashMap<>();
		for (Item item : container.getItems())
		{
			if (item != null)
			{
				snapshot.put(item.getId(), snapshot.getOrDefault(item.getId(), 0) + item.getQuantity());
			}
		}
		return snapshot;
	}

	@Provides
	AmuletOfBountyAlerterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AmuletOfBountyAlerterConfig.class);
	}
}
