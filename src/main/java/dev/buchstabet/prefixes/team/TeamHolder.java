package dev.buchstabet.prefixes.team;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class TeamHolder
{

  private final UUID defaultTeam;
  private final List<Team> teams;

  public Team loadTeam(Player player)
  {
    List<Team> teams = this.teams.stream().filter(team -> team.getPermission() == null || player.hasPermission(team.getPermission()))
        .collect(Collectors.toList());

    Team selected = null;
    for (Team team : teams) {
      if (selected == null) {
        selected = team;
        continue;
      }

      if (selected.getPriorityValue() < team.getPriorityValue()) {
        selected = team;
      }
    }

    return selected;
  }
}
