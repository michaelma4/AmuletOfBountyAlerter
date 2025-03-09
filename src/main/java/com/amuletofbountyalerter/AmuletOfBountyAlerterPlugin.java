package com.amuletofbountyalerter;

import com.google.inject.Provides;
import com.google.common.collect.ImmutableList;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
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
@Slf4j
@PluginDescriptor(
	name = "Amulet Of Bounty Alerter"
)
public class AmuletOfBountyAlerterPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private AmuletOfBountyAlerterConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AmuletOfBountyOverlay amuletOverlay;

	/*
	 * I personally went to all these locations and handpicked the coordinates myself
	 * */
	private static final List<WorldArea> ALLOTMENT_AREAS = ImmutableList.of(
			new WorldArea(3044, 3299, 25, 18, 0), // South of Falador
			new WorldArea(3589, 3517, 30, 22, 0), // West of Port Phasmatys
			new WorldArea(2800, 3455, 21, 18, 0), // North of Catherby
			new WorldArea(2659, 3366, 18, 17, 0), // North of Ardougne
			new WorldArea(1726, 3543, 20, 23, 0), // South-west corner of Hosidius
			new WorldArea(3790, 2831, 13, 12, 2), // Harmony Island
			new WorldArea(1254, 3719, 26, 23, 0), // Farming Guild
			new WorldArea(3286, 6087, 12, 25, 0), // Prifddinas
			new WorldArea(1577, 3089, 22, 19, 0) // West of Civitas illa Fortis
	);

	@Override
	protected void startUp() throws Exception
	{
		log.info("Amulet Of Bounty Alerter started!");
		sendChatMessage("Amulet Of Bounty Alerter has been enabled!");
		overlayManager.add(amuletOverlay);
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
	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(amuletOverlay); // Remove overlay when plugin stops
		log.info("Amulet Of Bounty Alerter stopped!");
	}

	public boolean nearAnAllotment(){
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

	@Provides
	AmuletOfBountyAlerterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AmuletOfBountyAlerterConfig.class);
	}
}
