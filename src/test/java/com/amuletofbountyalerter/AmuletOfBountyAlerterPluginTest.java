package com.amuletofbountyalerter;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class AmuletOfBountyAlerterPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(AmuletOfBountyAlerterPlugin.class);
		RuneLite.main(args);
	}
}