package dev.buchstabet.prefixes.scoreboard.v2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardHandler
{

  private final Scoreboard scoreboard;
  private final Objective sidebarObjective;

  public ScoreboardHandler(String displayname)
  {
    this(displayname, Bukkit.getScoreboardManager().getMainScoreboard());
  }

  public ScoreboardHandler(String displayname, Scoreboard scoreboard)
  {

    this.scoreboard = scoreboard;

    Objective sidebar = scoreboard.getObjective("sidebar");
    if (sidebar != null) {
      sidebar.unregister();
    }
    sidebarObjective = scoreboard.registerNewObjective("sidebar", "dummy", displayname);
    sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

  }

  public void setSidebarScore(int slot, String content)
  {
    if (slot < 0) {
      throw new IllegalArgumentException("slot must be > 0");
    }

    if (slot > 16) {
      throw new IllegalArgumentException("slot must be < 16");
    }

    Team team = getOrCreateTeam("sidebar" + slot);
    String emptyEntry = getEmptyEntry(slot);

    if (content == null) {
      scoreboard.resetScores(emptyEntry);
      return;
    }

    team.setPrefix(content);
    team.addEntry(emptyEntry);
    sidebarObjective.getScore(emptyEntry).setScore(slot);
  }

  public void addPlayer(Player player, String teamName, String prefix, String suffix,
      ChatColor color)
  {
    var team = getOrCreateTeam(teamName);
    team.setPrefix(prefix);
    team.setSuffix(suffix);
    team.setColor(color);
    team.addEntry(player.getName());
  }

  public void removePlayer(Player player)
  {
    Team team = scoreboard.getEntryTeam(player.getName());
    if (team == null)
      return;
    team.removeEntry(player.getName());
  }

  public String getEmptyEntry(int slot)
  {
    return ChatColor.values()[slot].toString() + ChatColor.values()[slot + 1];
  }

  private Team getOrCreateTeam(String name)
  {
    Team team = scoreboard.getTeam(name);
    if (team == null) {
      team = scoreboard.registerNewTeam(name);
    }

    return team;
  }
}
