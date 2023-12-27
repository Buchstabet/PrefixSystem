package dev.buchstabet.prefixes.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public abstract class ScoreboardBuilder
{

  private final Scoreboard scoreboard;
  private final Objective objective;

  private final Player player;

  public ScoreboardBuilder(Player player, String displayname)
  {
    this.player = player;

    if (player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
      player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    this.scoreboard = player.getScoreboard();
    Objective display = scoreboard.getObjective("display");
    if (display != null)
      display.unregister();
    objective = this.scoreboard.registerNewObjective("display", "dummy", displayname);
    this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    createScoreboard();
  }

  public void setDisplayname(String displayname)
  {
    this.objective.setDisplayName(displayname);
  }

  public void setScore(String content, int score)
  {
    getTeamByScore(score).setPrefix(content);
    showScore(score);
  }

  public void removeScore(int score)
  {
    hideScore(score);
  }

  public abstract void createScoreboard();

  public abstract void update();

  public String getEntryNameByScore(int score)
  {
    return ChatColor.values()[score].toString() + ChatColor.values()[score + 1];
  }

  private Team getTeamByScore(int score)
  {
    String entryNameByScore = getEntryNameByScore(score);
    Team team = scoreboard.getEntryTeam(entryNameByScore);

    if (team != null) {
      return team;
    }

    team = scoreboard.registerNewTeam("TEAM_" + score);
    team.addEntry(entryNameByScore);
    return team;
  }

  private void showScore(int score)
  {
    String entryNameByScore = getEntryNameByScore(score);
    Score objectiveScore = objective.getScore(entryNameByScore);
    if (objectiveScore.isScoreSet()) {
      return;
    }

    objectiveScore.setScore(score);
  }

  private void hideScore(int score)
  {
    String entryNameByScore = getEntryNameByScore(score);
    if (!objective.getScore(entryNameByScore).isScoreSet()) {
      return;
    }

    scoreboard.resetScores(entryNameByScore);
  }
}
