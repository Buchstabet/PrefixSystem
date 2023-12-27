package dev.buchstabet.prefixes.player;

import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.prefixcolor.PrefixColorHolder;
import dev.buchstabet.prefixes.team.TeamHolder;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public class PlayerDataHolder extends ArrayList<PlayerData>
{

  public CompletableFuture<PlayerData> load(Player player)
  {
    PrefixColorHolder prefixColors = Prefixes.getInstance().get(PrefixColorHolder.class);
    TeamHolder teamHolder = Prefixes.getInstance().get(TeamHolder.class);

    return prefixColors.loadColor(player.getUniqueId()).thenApply(prefixColor -> {
      prefixColor.ifPresent(color -> player.sendMessage(color.getId() + " was selected"));

      PlayerData playerData = new PlayerData(player.getUniqueId());
      playerData.setTeam(teamHolder.loadTeam(player));
      prefixColor.ifPresent(playerData::setColor);
      return playerData;
    });
  }

  public Optional<PlayerData> find(UUID uuid)
  {
    return stream().filter(playerData -> playerData.getUuid().equals(uuid)).findFirst();
  }

  public void add(Player player)
  {
    Optional<PlayerData> optional = find(player.getUniqueId());
    if (optional.isPresent()) {
      return;
    }

    CompletableFuture<PlayerData> load = load(player);
    load.whenComplete((playerData, throwable) -> {
      if (throwable != null) {
        throwable.printStackTrace();
        return;
      }

      if (playerData == null) {
        player.sendMessage("Es wurde kein Team für dich gefunden.");
        return;
      }

      player.sendMessage("Dein Team ist nun " + playerData.getTeam().getId());
      this.add(playerData);
      playerData.updatePrefix();
    });
  }
}
