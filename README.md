# BA Cape Emote Timer

A RuneLite plugin for Barbarian Assault. When you're in a wave and wearing a cape that has a Skill Cape emote, it writes on the Skill Cape emote button the wave time your emote would finish at if you clicked it now.

- **Same clock as BA Cursor Timer.** It counts game ticks from the `---- Wave: N ----` game message and stops when you leave the wave, so times use the same `0.0`, `0.6`, `1.2`, … format (seconds and tenths, never rolling over into minutes).
- **Finish time.** The time shown is the current wave tick + 1 + the cape's stall length. The click is handled on the next tick, which starts the stall, and you're free again once the stall's ticks have passed. Clicking while moving adds a tick. With the **Add movement tick** option on, that tick is added whenever you changed tile on the latest game tick. With it off (the default), the time always assumes you're standing still.
- **Per-cape durations.** Stall lengths come from the [OSRS Wiki's Skill Cape emote duration table](https://oldschool.runescape.wiki/w/Skill_Cape_(emote)#Emote_durations). That covers every skillcape (trimmed or untrimmed), the quest point, achievement diary and max capes (all max cape variants), and the cabbage and banana capes. The music cape has no Skill Cape emote, so nothing is shown for it.
- **Only while it matters.** Nothing is drawn outside a wave, without one of those capes equipped, or while the emote tab is closed. The text is clipped to the scrolling emote list.

## Config

| Option | Default | Description |
| --- | --- | --- |
| Add movement tick | off | Add the extra tick the emote takes when clicked while moving, whenever you are moving |
| Text color | white | Color of the emote finish time |
| Font | RuneScape | RuneScape, RuneScape Small, Sans-serif, Serif, or Monospaced |
| Bold | on | Draw the finish time in bold |
| Font size | 16 | Size of the finish time text (16 is the game's normal size) |
| Background | off | Draw a rectangle directly behind the finish time |
| Background color | black | Color of the rectangle |
| Background opacity | 50% | Opacity of the rectangle, from 0% (invisible) to 100% (solid) |

## Development

Build and test with JDK 11:

```sh
./gradlew build
```

To launch a development client with the plugin loaded, run `BACapeEmoteTimerPluginTest.main` from `src/test`, or build `./gradlew shadowJar` and run `java -ea -jar build/libs/ba-cape-emote-timer-1.0-SNAPSHOT-all.jar --developer-mode`.
