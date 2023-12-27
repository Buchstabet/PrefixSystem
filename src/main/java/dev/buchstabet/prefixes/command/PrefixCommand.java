package dev.buchstabet.prefixes.command;

import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.prefixcolor.MultiColorPrefix;
import dev.buchstabet.prefixes.prefixcolor.PrefixColorHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PrefixCommand implements CommandExecutor
{

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

      if (args.length > 2 && args[1].equalsIgnoreCase("add")) {
        if (args.length >= 5 && args[2].equalsIgnoreCase("MULTICOLOR")) {
          int skipAfter = Integer.parseInt(args[3]);
          List<String> colors = new ArrayList<>();
          for (int i = 4; i <= args.length - 1; i++) {
            colors.add(args[i]);
          }

          PrefixColorHolder prefixColors = Prefixes.getInstance().get(PrefixColorHolder.class);
          prefixColors.add(new MultiColorPrefix(UUID.randomUUID(), colors, skipAfter));
          prefixColors.store();
        }

        if (args.length > 3 && args[2].equalsIgnoreCase("PREFIX_NAME_SUFFIX")) {

        }

        if (args.length > 3 && args[2].equalsIgnoreCase("ONE_COLOR")) {

        }
      }

    }

    return false;
  }

  private void sendHelp(Player player)
  {
    player.sendMessage(
        "/prefix color add MULTICOLOR [Buchstaben pro Farbe] [Farben mit \";\" getrennt]");
    player.sendMessage(
        "/prefix color add PREFIX_NAME_SUFFIX [Farbe des Prefix] [Farbe des Namens] [Farbe des Suffix]");
    player.sendMessage("/prefix color add ONE_COLOR [Farbe des Tags]");
  }
}
