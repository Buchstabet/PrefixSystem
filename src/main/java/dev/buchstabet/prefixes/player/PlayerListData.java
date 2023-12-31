package dev.buchstabet.prefixes.player;

import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.team.TeamHolder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

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

  public void reload(@Nullable List<PlayerData> updated)
  {
    (updated == null ? Prefixes.getInstance().get(PlayerDataHolder.class) : updated).forEach(
        playerData -> {
          unregister(playerData);
          register(playerData);
        });
  }

  public void register(PlayerData other)
  {
    Team team = getTeam( other.getTeam());
    team.addEntry(other.getName());
  }

  private Team getTeam(dev.buchstabet.prefixes.team.Team team)
  {
    Team tabTeam = player.getScoreboard()
        .getTeam(team.getPriorityValue() + team.getId().toString());
    if (tabTeam == null)
      return player.getScoreboard()
          .registerNewTeam(team.getPriorityValue() + team.getId().toString());

    tabTeam.setPrefix(team.getScoreboard().getPrefix());
    tabTeam.setSuffix(team.getScoreboard().getSuffix());
    tabTeam.setColor(team.getColor());

    return tabTeam;
  }

  public void unregister(PlayerData player)
  {
    getTeam(player.getTeam()).removeEntry(player.getName());
  }
}
