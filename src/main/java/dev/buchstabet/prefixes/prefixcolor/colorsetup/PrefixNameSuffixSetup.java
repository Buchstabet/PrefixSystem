package dev.buchstabet.prefixes.prefixcolor.colorsetup;

import dev.buchstabet.prefixes.prefixcolor.PrefixColor;
import dev.buchstabet.prefixes.prefixcolor.PrefixNameSuffixColor;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class PrefixNameSuffixSetup implements PrefixColorSetup
{

  @Override
  public String getCommandUsage()
  {
    return "<Farbe des Prefix> <Farbe des Namens> <Farbe des Suffix>";
  }

  @Override
  public PrefixColor handleAddCommand(@NotNull String @NotNull [] args)
  {
    if (args.length != 7) {
      return null;
    }

    return new PrefixNameSuffixColor(UUID.randomUUID(), args[3], "prefix.use.color.pns", args[4], args[5], args[6]);
  }

}
