package dev.buchstabet.prefixes.player;

import dev.buchstabet.prefixes.utils.ColorUtil;

public class DisplayData
{

  private final String prefix, suffix;

  public DisplayData(String prefix, String suffix)
  {
    this.prefix = prefix;
    this.suffix = suffix;
  }

  public String getPrefix()
  {
    return ColorUtil.colorize(prefix);
  }

  public String getSuffix()
  {
    return ColorUtil.colorize(suffix);
  }
}
