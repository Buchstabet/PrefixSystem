package dev.buchstabet.prefixes.player;

import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.team.TeamHolder;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class PlayerListData
{

  private final Player player;
  private final Map<dev.buchstabet.prefixes.team.Team, Team> teams = new HashMap<>();

  public PlayerListData(Player player)
  {
    this.player = player;
    if (player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard()))
      player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

    TeamHolder teamHolder = Prefixes.getInstance().get(TeamHolder.class);
    teamHolder.getTeams().forEach(team -> teams.put(team, getTeam(team)));
  }

  public void initPlayers()
  {
    Prefixes.getInstance().get(PlayerDataHolder.class).forEach(this::update);
  }

  public void update(PlayerData other)
  {
    Team team = getTeam(other.getTeam());
    team.addEntry(other.getName());
  }

  private Team getTeam(dev.buchstabet.prefixes.team.Team team)
  {
    Team t = player.getScoreboard().getTeam(team.getPriorityValue() + team.getId().toString());
    if (t == null)
      return player.getScoreboard()
          .registerNewTeam(team.getPriorityValue() + team.getId().toString());

    t.setPrefix(team.getScoreboard().getPrefix());
    t.setSuffix(team.getScoreboard().getSuffix());
    t.setColor(team.getColor());

    return t;
  }

  public void unregister(PlayerData player)
  {
    getTeam(player.getTeam()).removeEntry(player.getName());
  }
}
