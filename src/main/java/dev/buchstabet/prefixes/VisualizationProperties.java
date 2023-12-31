package dev.buchstabet.prefixes;

import dev.buchstabet.prefixes.utils.ColorUtil;
import java.util.Properties;

public class VisualizationProperties extends Properties
{

  @Override
  public String getProperty(String key)
  {
    return ColorUtil.colorize(super.getProperty(key));
  }
}
