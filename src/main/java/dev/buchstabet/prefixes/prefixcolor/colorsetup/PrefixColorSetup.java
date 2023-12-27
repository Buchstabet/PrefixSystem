package dev.buchstabet.prefixes.prefixcolor.colorsetup;

import dev.buchstabet.prefixes.prefixcolor.PrefixColor;
import org.jetbrains.annotations.NotNull;

public interface PrefixColorSetup
{

  PrefixColor handleAddCommand(@NotNull String @NotNull [] args);

  String getCommandUsage();

}
