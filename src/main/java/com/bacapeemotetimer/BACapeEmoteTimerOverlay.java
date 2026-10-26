package com.bacapeemotetimer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.SpriteID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ColorUtil;

class BACapeEmoteTimerOverlay extends Overlay
{
	private static final int BACKGROUND_PADDING = 2;

	private final Client client;
	private final BACapeEmoteTimerPlugin plugin;
	private final BACapeEmoteTimerConfig config;

	private Font font;
	private TimerFont fontType;
	private boolean fontBold;
	private int fontSize;

	@Inject
	private BACapeEmoteTimerOverlay(Client client, BACapeEmoteTimerPlugin plugin, BACapeEmoteTimerConfig config)
	{
		super(plugin);
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		this.client = client;
		this.plugin = plugin;
		this.config = config;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		WaveClock clock = plugin.getWaveClock();
		if (!clock.isRunning())
		{
			return null;
		}

		CapeEmote emote = plugin.equippedCapeEmote();
		if (emote == null)
		{
			return null;
		}

		Widget emoteButton = skillCapeEmoteButton();
		Widget scrollable = client.getWidget(InterfaceID.Emote.SCROLLABLE);
		if (emoteButton == null || scrollable == null)
		{
			return null;
		}

		String text = WaveClock.format(emote.finishTick(clock.elapsedTicks(client.getTickCount())));

		Rectangle button = emoteButton.getBounds();
		graphics.setFont(font());
		FontMetrics metrics = graphics.getFontMetrics();
		// Centered on the emote button
		int x = button.x + (button.width - metrics.stringWidth(text)) / 2;
		int y = button.y + (button.height + metrics.getAscent() - metrics.getDescent()) / 2;

		// The emote list scrolls, so only draw within its visible part
		Shape clip = graphics.getClip();
		graphics.clip(scrollable.getBounds());

		if (config.showBackground())
		{
			int alpha = Math.round(config.backgroundOpacity() * 255 / 100f);
			graphics.setColor(ColorUtil.colorWithAlpha(config.backgroundColor(), alpha));
			graphics.fill(backgroundBounds(x, y, metrics, text));
		}

		OverlayUtil.renderTextLocation(graphics, new Point(x, y), text, config.textColor());
		graphics.setClip(clip);
		return null;
	}

	/**
	 * The Skill Cape emote's button in the emote tab, or null while the tab is closed.
	 */
	@Nullable
	private Widget skillCapeEmoteButton()
	{
		Widget contents = client.getWidget(InterfaceID.Emote.CONTENTS);
		if (contents == null || contents.isHidden())
		{
			return null;
		}

		for (Widget button : contents.getDynamicChildren())
		{
			// The locked sprite too: the button stays unlit for a cabbage cape,
			// even though the emote can be performed
			if (button.getSpriteId() == SpriteID.Emotes.SKILLCAPE || button.getSpriteId() == SpriteID.EmotesLocked.SKILLCAPE)
			{
				return button;
			}
		}
		return null;
	}

	/**
	 * The rectangle behind text drawn with its baseline at ({@code x}, {@code baseline}),
	 * covering the text and its one-pixel drop shadow plus padding on every side.
	 */
	static Rectangle backgroundBounds(int x, int baseline, FontMetrics metrics, String text)
	{
		return new Rectangle(
			x - BACKGROUND_PADDING,
			baseline - metrics.getAscent() - BACKGROUND_PADDING,
			metrics.stringWidth(text) + 1 + 2 * BACKGROUND_PADDING,
			metrics.getAscent() + metrics.getDescent() + 1 + 2 * BACKGROUND_PADDING);
	}

	private Font font()
	{
		TimerFont type = config.font();
		boolean bold = config.bold();
		int size = config.fontSize();
		if (font == null || type != fontType || bold != fontBold || size != fontSize)
		{
			font = type.font(bold, size);
			fontType = type;
			fontBold = bold;
			fontSize = size;
		}
		return font;
	}
}
