package dev.buchstabet.prefixes.scoreboard;

import dev.buchstabet.prefixes.Prefixes;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TestScoreboard extends ScoreboardBuilder
{

  boolean b = false;

  public TestScoreboard(Player player)
  {
    super(player, "  §6§lScoreboard  ");

    new BukkitRunnable()
    {
      @Override
      public void run()
      {
        if (b)
          setScore("ist cool", 0);
        else
          setScore("Relaisstellwerk.de", 0);
        b = !b;
      }
    }.runTaskTimer(Prefixes.getInstance(), 20, 20);
  }

  @Override
  public void createScoreboard()
  {
    setScore("Relaisstellwerk.de", 0);
  }

  @Override
  public void update()
  {

  }
}
