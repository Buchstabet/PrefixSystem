package dev.buchstabet.prefixes.listeners;

import dev.buchstabet.prefixes.player.PlayerData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class PrefixPlayerDataLoadedEvent extends Event
{

  private static final HandlerList handlers = new HandlerList();

  @NotNull
  @Override
  public HandlerList getHandlers()
  {
    return handlers;
  }

  public static HandlerList getHandlerList()
  {
    return handlers;
  }

  private final Player player;
  private final PlayerData playerData;
}
