package com.bacapeemotetimer;

import com.google.inject.Provides;
import javax.annotation.Nullable;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "BA Cape Emote Timer",
	description = "Shows on the Skill Cape emote the Barbarian Assault wave time its emote would finish at.",
	tags = {"barbarian", "assault", "minigame", "overlay", "timer", "tick", "emote", "skillcape"}
)
public class BACapeEmoteTimerPlugin extends Plugin
{
	// Same wave-start and wave-end signals as the Barbarian Assault plugin's
	// "Wave N duration" chat message, so the two agree on a wave's length.
	private static final String WAVE_START_PREFIX = "---- Wave:";

	@Getter(AccessLevel.PACKAGE)
	private final WaveClock waveClock = new WaveClock();

	private final MovementTracker movementTracker = new MovementTracker();

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private BACapeEmoteTimerOverlay overlay;

	@Provides
	BACapeEmoteTimerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BACapeEmoteTimerConfig.class);
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		waveClock.stop();
		movementTracker.reset();
	}

	/**
	 * Whether the player changed tile on the latest game tick.
	 */
	boolean isMoving()
	{
		return movementTracker.isMoving();
	}

	/**
	 * The Skill Cape emote of the cape the player is wearing, or null if it has none.
	 */
	@Nullable
	CapeEmote equippedCapeEmote()
	{
		ItemContainer worn = client.getItemContainer(InventoryID.WORN);
		if (worn == null)
		{
			return null;
		}

		Item cape = worn.getItem(EquipmentInventorySlot.CAPE.getSlotIdx());
		return cape == null ? null : CapeEmote.forItem(cape.getId());
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() == ChatMessageType.GAMEMESSAGE && event.getMessage().startsWith(WAVE_START_PREFIX))
		{
			// Counting from the message's own tick reads one tick ahead of the wave
			// (checked in game), so the wave's first tick is the one after it.
			waveClock.start(client.getTickCount() + 1);
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		Player player = client.getLocalPlayer();
		movementTracker.update(player == null ? null : player.getWorldLocation());
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		// Set while inside a wave, cleared on leaving it (completion, death or exiting)
		if (event.getVarbitId() == VarbitID.BARBASSAULT_AREAEXIT_PENDING && event.getValue() == 0)
		{
			waveClock.stop();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING)
		{
			waveClock.stop();
			movementTracker.reset();
		}
	}
}
