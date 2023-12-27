package dev.buchstabet.prefixes.prefixcolor.colorsetup;

import dev.buchstabet.prefixes.prefixcolor.MultiColorPrefix;
import dev.buchstabet.prefixes.prefixcolor.PrefixColor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class MultiColorPrefixSetup implements PrefixColorSetup
{

  @Override
  public String getCommandUsage()
  {
    return "[Buchstaben pro Farbe] [Farben getrennt]";
  }

  @Override
  public PrefixColor handleAddCommand(@NotNull String @NotNull [] args)
  {
    if (args.length < 5) {
      return null;
    }

    int skipAfter = Integer.parseInt(args[3]);
    List<String> colors = new ArrayList<>();
    for (int i = 4; i <= args.length - 1; i++) {
      colors.add(args[i]);
    }

    return new MultiColorPrefix(UUID.randomUUID(), "prefix.use.color.multicolor", colors,
        skipAfter);
  }

}
