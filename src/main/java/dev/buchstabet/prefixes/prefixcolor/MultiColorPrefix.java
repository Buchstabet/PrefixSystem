package dev.buchstabet.prefixes.prefixcolor;

import dev.buchstabet.prefixes.utils.DisplayNameType;
import java.util.List;
import java.util.UUID;

public class MultiColorPrefix extends AbstractPrefixColor
{

  private final List<String> colors;
  private final int skipAfter;

  public MultiColorPrefix(UUID uuid, String permission, List<String> colors, int skipAfter)
  {
    super(uuid, permission);
    this.colors = colors;
    this.skipAfter = skipAfter;
  }

  @Override
  public String colorize(String input, DisplayNameType type)
  {
    StringBuilder stringBuilder = new StringBuilder();

    int skipId = 0, color = 0;
    char[] charArray = input.toCharArray();
    stringBuilder.append(colors.get(color));

    for (int i = 0; i < input.length(); i++) {
      char c = charArray[i];
      stringBuilder.append(c);

      skipId++;
      if (skipAfter == skipId) {
        skipId = 0;
        color++;
        if (color == colors.size()) {
          color = 0;
        }
        stringBuilder.append(colors.get(color));
      }
    }

    return super.colorize(stringBuilder.toString(), type);
  }


}
