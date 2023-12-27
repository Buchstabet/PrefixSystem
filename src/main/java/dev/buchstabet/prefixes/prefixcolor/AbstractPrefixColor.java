package dev.buchstabet.prefixes.prefixcolor;

import dev.buchstabet.prefixes.utils.ColorUtil;
import dev.buchstabet.prefixes.utils.DisplayNameType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractPrefixColor implements PrefixColor
{

  private final UUID uuid;

  @Override
  public UUID getId()
  {
    return uuid;
  }

  @Override
  public String colorize(String input, DisplayNameType type)
  {
    return ColorUtil.colorize(input);
  }
}
