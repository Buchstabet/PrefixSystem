package dev.buchstabet.prefixes.prefixcolor.colorsetup;

import dev.buchstabet.prefixes.prefixcolor.OneColorPrefix;
import dev.buchstabet.prefixes.prefixcolor.PrefixColor;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class OneColorPrefixSetup implements PrefixColorSetup
{

  @Override
  public String getCommandUsage()
  {
    return "<Farbe des Tags>";
  }

  @Override
  public PrefixColor handleAddCommand(@NotNull String @NotNull [] args)
  {
    if (args.length != 5) {
      return null;
    }

    return new OneColorPrefix(UUID.randomUUID(), args[3], "prefix.use.color.onecolor", args[4]);
  }

}
