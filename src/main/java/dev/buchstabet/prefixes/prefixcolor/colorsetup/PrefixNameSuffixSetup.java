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
    return "[Farbe des Prefix] [Farbe des Namens] [Farbe des Suffix]";
  }

  @Override
  public PrefixColor handleAddCommand(@NotNull String @NotNull [] args)
  {
    if (args.length != 6) {
      return null;
    }

    return new PrefixNameSuffixColor(UUID.randomUUID(), "prefix.use.color.pns", args[3], args[4], args[5]);
  }

}
