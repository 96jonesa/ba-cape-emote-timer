package com.bacapeemotetimer;

import net.runelite.api.gameval.ItemID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CapeEmoteTest
{
	@Test
	void stallLengthsMatchTheWiki()
	{
		// Fails if a transcribed value drifts from the wiki's "Emote durations" table
		assertEquals(7, CapeEmote.AGILITY.getStallTicks());
		assertEquals(25, CapeEmote.COOKING.getStallTicks());
		assertEquals(5, CapeEmote.MAGIC.getStallTicks());
		assertEquals(15, CapeEmote.SAILING.getStallTicks());
		assertEquals(32, CapeEmote.ACHIEVEMENT_DIARY.getStallTicks());
		assertEquals(8, CapeEmote.MAX.getStallTicks());
		assertEquals(2, CapeEmote.CABBAGE.getStallTicks());
		assertEquals(9, CapeEmote.BANANA.getStallTicks());
	}

	@Test
	void coversEveryCapeInTheWikiTable()
	{
		assertEquals(29, CapeEmote.values().length);
	}

	@Nested
	class TestForItem
	{
		@Test
		void findsUntrimmedAndTrimmedCapes()
		{
			assertEquals(CapeEmote.ATTACK, CapeEmote.forItem(ItemID.SKILLCAPE_ATTACK));
			assertEquals(CapeEmote.ATTACK, CapeEmote.forItem(ItemID.SKILLCAPE_ATTACK_TRIMMED));
			assertEquals(CapeEmote.QUEST_POINT, CapeEmote.forItem(ItemID.SKILLCAPE_QP_TRIMMED));
		}

		@Test
		void findsMaxCapeVariants()
		{
			assertEquals(CapeEmote.MAX, CapeEmote.forItem(ItemID.SKILLCAPE_MAX));
			assertEquals(CapeEmote.MAX, CapeEmote.forItem(ItemID.SKILLCAPE_MAX_INFERNALCAPE));
			assertEquals(CapeEmote.MAX, CapeEmote.forItem(ItemID.SKILLCAPE_MAX_DIZANAS));
		}

		@Test
		void hasNothingForCapesWithoutTheEmote()
		{
			assertNull(CapeEmote.forItem(ItemID.MUSIC_CAPE));
			assertNull(CapeEmote.forItem(ItemID.INFERNAL_CAPE));
		}
	}

	@Nested
	class TestFinishTick
	{
		@Test
		void startsOnTheTickAfterTheClickAndLastsTheStall()
		{
			assertEquals(1 + 1 + 6, CapeEmote.ATTACK.finishTick(1, false));
			assertEquals(100 + 1 + 32, CapeEmote.ACHIEVEMENT_DIARY.finishTick(100, false));
		}

		@Test
		void takesOneTickLongerWhileMoving()
		{
			assertEquals(1 + 1 + 6 + 1, CapeEmote.ATTACK.finishTick(1, true));
		}
	}
}
