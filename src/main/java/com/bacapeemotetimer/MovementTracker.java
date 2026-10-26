package com.bacapeemotetimer;

import java.util.Objects;
import javax.annotation.Nullable;
import net.runelite.api.coords.WorldPoint;

/**
 * Whether the player changed tile on the latest game tick, from the tile seen on each tick.
 * Movement happens at the start of a tick, before any click made during it, so an emote
 * clicked on the tick of the player's last step still counts as clicked while moving.
 */
class MovementTracker
{
	@Nullable
	private WorldPoint lastLocation;

	private boolean moving;

	void update(@Nullable WorldPoint location)
	{
		moving = lastLocation != null && location != null && !Objects.equals(lastLocation, location);
		lastLocation = location;
	}

	void reset()
	{
		lastLocation = null;
		moving = false;
	}

	boolean isMoving()
	{
		return moving;
	}
}
