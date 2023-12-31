package dev.buchstabet.prefixes;

import dev.buchstabet.prefixes.player.PlayerData;
import dev.buchstabet.prefixes.player.PlayerDataHolder;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PrefixUpdateTask implements Runnable
{

  @Override
  public void run()
  {
    PlayerDataHolder playerDataHolder = Prefixes.getInstance().get(PlayerDataHolder.class);
    List<PlayerData> updated = playerDataHolder.stream().filter(PlayerData::updateTeam).toList();
    playerDataHolder.forEach(playerData -> playerData.updatePrefix(updated));
  }
}
