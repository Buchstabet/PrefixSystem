package dev.buchstabet.prefixes.command;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

public class ColorCommand implements CommandExecutor, Listener
{

  private final Map<Player, ColorInventory> playerColorInventoryMap = new HashMap<>();

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args)
  {
    if (!(sender instanceof Player player)) {
      return false;
    }
    playerColorInventoryMap.put(player, new ColorInventory(player));
    return true;
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryClose(InventoryCloseEvent event)
  {
    ColorInventory inventory = playerColorInventoryMap.get((Player) event.getPlayer());
    if (inventory == null || !inventory.getInventory().equals(event.getInventory())) {
      return;
    }

    playerColorInventoryMap.remove((Player) event.getPlayer());
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryClick(InventoryClickEvent event)
  {
    ColorInventory inventory = playerColorInventoryMap.get((Player) event.getView().getPlayer());

    if (inventory == null) {
      return;
    }

    event.setCancelled(true);
    inventory.handleClick(event);
  }
}
