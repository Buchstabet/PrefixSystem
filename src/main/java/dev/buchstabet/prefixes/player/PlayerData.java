package dev.buchstabet.prefixes.player;

import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.prefixcolor.PrefixColor;
import dev.buchstabet.prefixes.team.Team;
import dev.buchstabet.prefixes.team.TeamHolder;
import dev.buchstabet.prefixes.utils.ColorUtil;
import dev.buchstabet.prefixes.utils.DisplayNameType;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Setter
@Getter
@RequiredArgsConstructor
public class PlayerData
{

  private final UUID uuid;
  private final String name;
  private Team team;
  private PrefixColor color;
  private DisplayData customPlayerData;
  private PlayerListData playerListData;

  public void updatePrefix(@Nullable List<PlayerData> updated)
  {
    Player player = Bukkit.getPlayer(uuid);
    if (player == null) {
      return;
    }

    String tabPrefix = getColorize(team.getTab().getPrefix(), DisplayNameType.PREFIX);
    String tabName = getColorize(player.getName(), DisplayNameType.NAME);
    String tabSuffix = getColorize(team.getTab().getSuffix(), DisplayNameType.SUFFIX);
    player.setPlayerListName(tabPrefix + tabName + tabSuffix);

    String chatPrefix = getColorize(team.getChat().getPrefix(), DisplayNameType.PREFIX);
    String chatName = getColorize(player.getName(), DisplayNameType.NAME);
    String chatSuffix = getColorize(team.getChat().getSuffix(), DisplayNameType.SUFFIX);
    player.setDisplayName(chatPrefix + chatName + chatSuffix + "§r");

    getPlayerListData().reload(updated);
  }

  private String getColorize(String s, DisplayNameType type)
  {
    if (color == null) {
      return ColorUtil.colorize(s);
    }

    return color.colorize(ColorUtil.removeColorCodes(s), type);
  }

  public boolean updateTeam()
  {
    Team old = team;
    team = Prefixes.getInstance().get(TeamHolder.class).loadTeam(Bukkit.getPlayer(uuid));
    return !old.equals(team);
  }
}