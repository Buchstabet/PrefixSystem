package dev.buchstabet.prefixes.prefixcolor;

import dev.buchstabet.prefixes.utils.DisplayNameType;
import java.util.UUID;

public class PrefixNameSuffixColor extends AbstractPrefixColor
{

  private final String prefixColor, nameColor, suffixColor;

  public PrefixNameSuffixColor(UUID uuid, String displayname, String permission, String prefixColor, String nameColor,
      String suffixColor)
  {
    super(uuid, displayname, permission);
    this.prefixColor = prefixColor;
    this.nameColor = nameColor;
    this.suffixColor = suffixColor;
  }

  @Override
  public String colorize(String input, DisplayNameType type)
  {
    switch (type) {
      case PREFIX -> {
        return prefixColor + input;
      }

      case NAME -> {
        return nameColor + input;
      }

      case SUFFIX -> {
        return suffixColor + input;
      }
    }

    return super.colorize(input, type);
  }
}
