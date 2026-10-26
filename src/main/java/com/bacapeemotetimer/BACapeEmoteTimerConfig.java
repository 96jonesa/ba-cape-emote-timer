package com.bacapeemotetimer;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(BACapeEmoteTimerConfig.GROUP)
public interface BACapeEmoteTimerConfig extends Config
{
	String GROUP = "baCapeEmoteTimer";

	@ConfigItem(
		keyName = "addMovementTick",
		name = "Add movement tick",
		description = "While you are moving, add the extra tick the emote takes when clicked mid-walk. Off always assumes you click it standing still.",
		position = 0
	)
	default boolean addMovementTick()
	{
		return false;
	}

	@ConfigItem(
		keyName = "textColor",
		name = "Text color",
		description = "Color of the emote finish time.",
		position = 1
	)
	default Color textColor()
	{
		return Color.WHITE;
	}

	@ConfigItem(
		keyName = "font",
		name = "Font",
		description = "Font of the emote finish time.",
		position = 2
	)
	default TimerFont font()
	{
		return TimerFont.RUNESCAPE;
	}

	@ConfigItem(
		keyName = "bold",
		name = "Bold",
		description = "Draw the emote finish time in bold.",
		position = 3
	)
	default boolean bold()
	{
		return true;
	}

	@Range(min = 8, max = 64)
	@ConfigItem(
		keyName = "fontSize",
		name = "Font size",
		description = "Size of the emote finish time text. 16 is the game's normal size.",
		position = 4
	)
	default int fontSize()
	{
		return 16;
	}

	@ConfigItem(
		keyName = "showBackground",
		name = "Background",
		description = "Draw a rectangle behind the emote finish time.",
		position = 5
	)
	default boolean showBackground()
	{
		return false;
	}

	@ConfigItem(
		keyName = "backgroundColor",
		name = "Background color",
		description = "Color of the rectangle behind the emote finish time.",
		position = 6
	)
	default Color backgroundColor()
	{
		return Color.BLACK;
	}

	@Range(max = 100)
	@Units(Units.PERCENT)
	@ConfigItem(
		keyName = "backgroundOpacity",
		name = "Background opacity",
		description = "Opacity of the rectangle behind the emote finish time, from 0 (invisible) to 100 (solid).",
		position = 7
	)
	default int backgroundOpacity()
	{
		return 50;
	}
}
