package dev.buchstabet.prefixes.listeners;

import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.player.PlayerDataHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener
{

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    PlayerDataHolder playerDataHolder = Prefixes.getInstance().get(PlayerDataHolder.class);
    playerDataHolder.add(event.getPlayer());

  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    PlayerDataHolder playerDataHolder = Prefixes.getInstance().get(PlayerDataHolder.class);
    playerDataHolder.find(event.getPlayer().getUniqueId()).ifPresent(playerData -> {
      playerDataHolder.forEach(playerListData -> playerListData.getPlayerListData().unregister(playerData));
      playerDataHolder.remove(playerData);
    });

    //event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());

  }


}
