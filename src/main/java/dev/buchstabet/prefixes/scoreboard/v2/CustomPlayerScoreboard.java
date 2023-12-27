package dev.buchstabet.prefixes.scoreboard.v2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

@Getter
@RequiredArgsConstructor
public class CustomPlayerScoreboard
{

  private final Map<UUID, ScoreboardHandler> scoreboards = new HashMap<>();
  private final Map<Integer, String> defaults = new HashMap<>();
  private final String displayname;

  public void setSidebarScore(int slot, String content)
  {
    if (slot < 0) {
      throw new IllegalArgumentException("slot must be > 0");
    }

    if (slot > 16) {
      throw new IllegalArgumentException("slot must be < 16");
    }

    scoreboards.forEach(
        (uuid, scoreboardHandler) -> scoreboardHandler.setSidebarScore(slot, content));
  }

  public void setDefaultSidebarScore(int slot, String content)
  {
    setSidebarScore(slot, content);
    if (content == null)
      defaults.remove(slot);
    else
      defaults.put(slot, content);
  }

  public void remove(Player player)
  {
    scoreboards.remove(player.getUniqueId());
  }

  public ScoreboardHandler getScoreboard(Player player)
  {
    return scoreboards.computeIfAbsent(player.getUniqueId(), uuid -> {
      Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
      player.setScoreboard(scoreboard);

      ScoreboardHandler scoreboardHandler = new ScoreboardHandler(displayname, scoreboard);
      defaults.forEach(scoreboardHandler::setSidebarScore);
      return scoreboardHandler;
    });
  }

}
