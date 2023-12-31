package dev.buchstabet.prefixes;

import dev.buchstabet.prefixes.player.PlayerData;
import dev.buchstabet.prefixes.player.PlayerDataHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PrefixUpdateTask implements Runnable
{

  @Override
  public void run()
  {
    PlayerDataHolder playerDataHolder = Prefixes.getInstance().get(PlayerDataHolder.class);
    playerDataHolder.forEach(PlayerData::updateTeam);
    playerDataHolder.forEach(playerData -> playerData.updatePrefix(
        playerDataHolder.stream().filter(PlayerData::isUpdate).toList()));
    playerDataHolder.forEach(playerData -> playerData.setUpdate(false));
  }
}
