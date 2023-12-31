package dev.buchstabet.prefixes.team;

import dev.buchstabet.prefixes.player.DisplayData;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@Getter
@RequiredArgsConstructor
public class Team
{

  private final UUID id;
  private final int priorityValue;
  private final ChatColor color;
  private final DisplayData tab, scoreboard, chat;
  private final String permission;
  private final boolean useCustomColors;

  public Team(int priorityValue, ChatColor color, DisplayData tab, DisplayData scoreboard,
      DisplayData chat)
  {
    this(UUID.randomUUID(), priorityValue, color, tab, scoreboard, chat, null, false);
  }
}
