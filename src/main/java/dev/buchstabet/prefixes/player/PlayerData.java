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
@RequiredArgsConstructor
public class PlayerData
{

  @Getter private final UUID uuid;
  @Getter private final String name;
  private Team team, customTeam;
  @Getter private PrefixColor color;
  @Getter private PlayerListData playerListData;
  @Getter private boolean update;

  public void updatePrefix(@Nullable List<PlayerData> updated)
  {
    Player player = Bukkit.getPlayer(uuid);
    if (player == null) {
      return;
    }

    DisplayData tab = getTeam().getTab();
    DisplayData chat = getTeam().getChat();

    String tabPrefix = getColorize(tab.getPrefix(), DisplayNameType.PREFIX);
    String tabName = getColorize(player.getName(), DisplayNameType.NAME);
    String tabSuffix = getColorize(tab.getSuffix(), DisplayNameType.SUFFIX);
    player.setPlayerListName(tabPrefix + tabName + tabSuffix);

    String chatPrefix = getColorize(chat.getPrefix(), DisplayNameType.PREFIX);
    String chatName = getColorize(player.getName(), DisplayNameType.NAME);
    String chatSuffix = getColorize(chat.getSuffix(), DisplayNameType.SUFFIX);
    player.setDisplayName(chatPrefix + chatName + chatSuffix + "§r");

    getPlayerListData().reload(updated);
  }

  private String getColorize(String s, DisplayNameType type)
  {
    if (color == null || !getTeam().isUseCustomColors()) {
      return ColorUtil.colorize(s);
    }

    return color.colorize(ColorUtil.removeColorCodes(s), type);
  }

  public void updateTeam()
  {
    setTeam(Prefixes.getInstance().get(TeamHolder.class).loadTeam(Bukkit.getPlayer(uuid)));
  }

  public Team getTeam()
  {
    return customTeam == null ? team : customTeam;
  }

  public void setTeam(Team team)
  {
    this.team = team;
    setUpdate(true);
  }

  public void setCustomTeam(Team customTeam)
  {
    this.customTeam = customTeam;
    setUpdate(true);
  }

}