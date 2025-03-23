package com.amuletofbountyalerter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("amuletOfBounty")
public interface AmuletOfBountyAlerterConfig extends Config
{

	@ConfigItem(
			keyName = "onlyActiveNearAllotment",
			name = "Only active near allotment",
			description = "Only alert when an allotment patch is nearby",
			position = 1
	)
	default boolean onlyActiveNearAllotment()
	{
		return true;
	}

	@ConfigItem(
			keyName = "notifyAfterPlantingWithoutAmuletOfBounty",
			name = "Notify after planting without amulet of bounty",
			description = "Sends a notification after you plant allotment seeds without an amulet of bounty",
			position = 2
	)
	default boolean notifyAfterPlantingWithoutAmuletOfBounty() { return true; }
}
