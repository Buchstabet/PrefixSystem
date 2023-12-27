package dev.buchstabet.prefixes.player;

import dev.buchstabet.prefixes.prefixcolor.PrefixColor;
import dev.buchstabet.prefixes.team.Team;
import dev.buchstabet.prefixes.utils.ColorUtil;
import dev.buchstabet.prefixes.utils.DisplayNameType;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Setter
@Getter
@RequiredArgsConstructor
public class PlayerData
{

  private final UUID uuid;
  private Team team;
  private PrefixColor color;
  private DisplayData customPlayerData;

  public void updatePrefix()
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
  }

  private String getColorize(String s, DisplayNameType type)
  {
    if (color == null) {
      return ColorUtil.colorize(s);
    }

    return color.colorize(s, type);
  }

}