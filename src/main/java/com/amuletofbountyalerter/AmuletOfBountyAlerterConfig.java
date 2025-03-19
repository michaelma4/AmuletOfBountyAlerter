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
			description = "Only alert when an allotment patch is rendered",
			position = 1
	)
	default boolean onlyActiveNearAllotment()
	{
		return true;
	}

	@ConfigItem(
			keyName = "notifyAfterPlantingWithoutAmuletOfBounty",
			name = "Notify after planting without amulet of bounty",
			description = "Sends a notification after you plant without an amulet of bounty (only checks snape " +
					"grass seeds in 1.2)",
			position = 2
	)
	default boolean notifyAfterPlantingWithoutAmuletOfBounty() { return true; }
}
