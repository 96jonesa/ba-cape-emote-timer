package com.bacapeemotetimer;

import net.runelite.api.coords.WorldPoint;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class MovementTrackerTest
{
	private static final WorldPoint TILE = new WorldPoint(1900, 5250, 0);

	@Test
	void isNotMovingBeforeTwoTicksAreSeen()
	{
		MovementTracker tracker = new MovementTracker();
		assertFalse(tracker.isMoving());

		tracker.update(TILE);
		assertFalse(tracker.isMoving());
	}

	@Test
	void isMovingAfterChangingTile()
	{
		MovementTracker tracker = new MovementTracker();
		tracker.update(TILE);
		tracker.update(TILE.dx(1));

		assertTrue(tracker.isMoving());
	}

	@Test
	void stopsMovingOnceTheTileStaysTheSame()
	{
		MovementTracker tracker = new MovementTracker();
		tracker.update(TILE);
		tracker.update(TILE.dx(2));
		tracker.update(TILE.dx(2));

		assertFalse(tracker.isMoving());
	}

	@Test
	void resetForgetsTheLastTile()
	{
		MovementTracker tracker = new MovementTracker();
		tracker.update(TILE);
		tracker.reset();
		tracker.update(TILE.dx(1));

		assertFalse(tracker.isMoving());
	}
}
