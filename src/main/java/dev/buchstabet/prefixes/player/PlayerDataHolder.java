package dev.buchstabet.prefixes.player;

import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.listeners.PrefixPlayerDataLoadedEvent;
import dev.buchstabet.prefixes.prefixcolor.PrefixColorHolder;
import dev.buchstabet.prefixes.team.TeamHolder;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerDataHolder extends ArrayList<PlayerData>
{

  public CompletableFuture<PlayerData> load(Player player)
  {
    PrefixColorHolder prefixColors = Prefixes.getInstance().get(PrefixColorHolder.class);
    TeamHolder teamHolder = Prefixes.getInstance().get(TeamHolder.class);

    return prefixColors.loadColor(player.getUniqueId()).thenApply(prefixColor -> {
      PlayerData playerData = new PlayerData(player.getUniqueId(), player.getName());
      playerData.setTeam(teamHolder.loadTeam(player));
      prefixColor.ifPresent(color -> {
        if (color.getPermission() != null && !player.hasPermission(color.getPermission()))
          return;
        playerData.setColor(color);
      });
      return playerData;
    });
  }

  public Optional<PlayerData> find(UUID uuid)
  {
    return stream().filter(playerData -> playerData.getUuid().equals(uuid)).findFirst();
  }

  public void add(Player player)
  {
    Optional<PlayerData> optional = find(player.getUniqueId());
    if (optional.isPresent()) {
      return;
    }

    CompletableFuture<PlayerData> load = load(player);
    load.whenComplete((playerData, throwable) -> {
      if (throwable != null) {
        throwable.printStackTrace();
        return;
      }

      if (playerData == null) {
        return;
      }

      Bukkit.getScheduler().runTask(Prefixes.getInstance(), () -> {
        playerData.setPlayerListData(new PlayerListData(player));
        this.add(playerData);
        playerData.updatePrefix(null);

        Prefixes.getInstance().get(PlayerDataHolder.class)
            .forEach(pd -> pd.getPlayerListData().register(playerData));

        Bukkit.getServer().getPluginManager()
            .callEvent(new PrefixPlayerDataLoadedEvent(player, playerData));
      });
    });
  }
}
