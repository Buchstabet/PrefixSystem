package dev.buchstabet.prefixes.player;

import lombok.Getter;

public class DisplayData
{

  @Getter private final String format;
  @Getter private final String prefix, suffix;

  public DisplayData(String format)
  {
    this.format = format;
    String[] strings = format.split("%p");

    if (strings.length != 2) {
      throw new IllegalArgumentException("Input not in the correct format.");
    }

    this.prefix = strings[0];
    this.suffix = strings[1];
  }
}
