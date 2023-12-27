package dev.buchstabet.prefixes.team;

import dev.buchstabet.prefixes.player.DisplayData;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Team
{

  private final UUID id;
  private final int priorityValue;
  private final DisplayData tab, scoreboard, chat;
  private final String permission;

}
