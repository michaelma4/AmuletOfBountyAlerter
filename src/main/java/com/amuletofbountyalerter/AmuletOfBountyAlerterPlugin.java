package com.amuletofbountyalerter;

import com.google.inject.Provides;
import com.google.common.collect.ImmutableList;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import java.util.*;

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

	/*
	 * This was automatically created from the wiki https://oldschool.runescape.wiki/w/List_of_banks
	 * if there is an error please let me know
	 * */
	private static final List<WorldArea> ALLOTMENT_AREAS = ImmutableList.of(
			new WorldArea(3055, 3308, 12, 12, 0), // South of Falador
			new WorldArea(3602, 3526, 12, 12, 0), // West of Port Phasmatys
			new WorldArea(2810, 3464, 12, 12, 0), // North of Catherby
			new WorldArea(2667, 3375, 12, 12, 0), // North of Ardougne
			new WorldArea(1735, 3555, 12, 12, 0), // South-west corner of Hosidius
			new WorldArea(3794, 2836, 12, 12, 2), // Harmony Island
			new WorldArea(1239, 3727, 12, 12, 0), // Farming Guild todo update coordinates
			new WorldArea(3291, 6100, 12, 12, 0), // Prifddinas
			new WorldArea(1586, 3099, 12, 12, 0) // West of Civitas illa Fortis
	);

	@Override
	protected void startUp() throws Exception
	{
		log.info("Amulet Of Bounty Alerter started!");
		sendChatMessage("Amulet Of Bounty Alerter has been enabled!");
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
		log.info("Example stopped!");
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

	//From example repo
	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Amulet of Bounty Alerter says " + config.greeting(), null);
		}
	}


	@Provides
	AmuletOfBountyAlerterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AmuletOfBountyAlerterConfig.class);
	}
}
