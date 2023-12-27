package dev.buchstabet.prefixes.listeners;

import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.player.PlayerDataHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener
{

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    PlayerDataHolder playerDataHolder = Prefixes.getInstance().get(PlayerDataHolder.class);
    playerDataHolder.add(event.getPlayer());
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    PlayerDataHolder playerDataHolder = Prefixes.getInstance().get(PlayerDataHolder.class);
    playerDataHolder.removeIf(
        playerData -> playerData.getUuid().equals(event.getPlayer().getUniqueId()));

    //event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());

    playerDataHolder.forEach(playerListData -> playerListData.getPlayerListData().unregister(playerListData));
  }


}
