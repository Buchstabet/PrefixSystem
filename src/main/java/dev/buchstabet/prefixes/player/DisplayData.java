package dev.buchstabet.prefixes.player;

import lombok.Getter;

public class DisplayData
{

  @Getter private final String prefix, suffix;

  public DisplayData(String prefix, String suffix)
  {
    this.prefix = prefix;
    this.suffix = suffix;
  }
}
