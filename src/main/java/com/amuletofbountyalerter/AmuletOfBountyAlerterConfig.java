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
}
