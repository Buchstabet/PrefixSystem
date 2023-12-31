package dev.buchstabet.prefixes.prefixcolor;

import dev.buchstabet.prefixes.utils.DisplayNameType;
import java.util.UUID;

public class OneColorPrefix extends AbstractPrefixColor
{

  private final String color;

  public OneColorPrefix(UUID uuid, String displayname, String permission, String color)
  {
    super(uuid, displayname, permission);
    this.color = color;
  }

  @Override
  public String colorize(String input, DisplayNameType type)
  {
    return super.colorize(color + input, type);
  }

}
