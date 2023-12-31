package dev.buchstabet.prefixes.command;

import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.prefixcolor.PrefixColor;
import dev.buchstabet.prefixes.prefixcolor.PrefixColorHolder;
import dev.buchstabet.prefixes.prefixcolor.colorsetup.PrefixColorSetup;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PrefixCommand implements CommandExecutor
{

  private final Map<String, PrefixColorSetup> setups = new HashMap<>();

  public void registerSetup(String name, PrefixColorSetup setup)
  {
    setups.putIfAbsent(name, setup);
  }

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
      @NotNull String s, @NotNull String[] args)
  {
    if (!(commandSender instanceof Player player)) {
      return false;
    }

    if (args.length == 0) {
      sendHelp(player);
    }

    if (args.length > 1 && args[0].equalsIgnoreCase("color")) {

      if (args.length > 3 && args[1].equalsIgnoreCase("add")) {
        PrefixColorSetup colorSetup = setups.get(args[2]);
        if (colorSetup == null) {
          sendHelp(player);
          return false;
        }

        PrefixColor prefixColor = colorSetup.handleAddCommand(args);
        if (prefixColor == null) {
          player.sendMessage("§cEs ist ein Fehler aufgetreten.");
          sendHelp(player);
          return false;
        }

        PrefixColorHolder prefixColors = Prefixes.getInstance().get(PrefixColorHolder.class);
        prefixColors.add(prefixColor);
        prefixColors.store();
        player.sendMessage("§aNeue farbe gespeichert als " + prefixColor.getId() + ".");
      }



    }

    return false;
  }

  private void sendHelp(Player player)
  {
    setups.forEach(
        (s, setup) -> player.sendMessage("/prefix color add <displayname> " + s + " " + setup.getCommandUsage()));
  }
}
