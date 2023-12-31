package dev.buchstabet.prefixes.command.color;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class InventoryPage
{

  private final int page;
  private final List<ItemStack> stacks;

}
