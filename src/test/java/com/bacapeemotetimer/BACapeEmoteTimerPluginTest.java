package com.bacapeemotetimer;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BACapeEmoteTimerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(BACapeEmoteTimerPlugin.class);
		RuneLite.main(args);
	}
}
