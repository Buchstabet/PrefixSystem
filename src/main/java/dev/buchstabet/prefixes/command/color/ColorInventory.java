package dev.buchstabet.prefixes.command.color;

import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.player.PlayerData;
import dev.buchstabet.prefixes.player.PlayerDataHolder;
import dev.buchstabet.prefixes.prefixcolor.PrefixColor;
import dev.buchstabet.prefixes.prefixcolor.PrefixColorHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class ColorInventory
{

  private static final int ITEMS_IN_PAGE = 9;

  private final Inventory inventory;
  private final Map<ItemStack, PrefixColor> map = new HashMap<>();
  private final List<InventoryPage> pages = new ArrayList<>();
  private final PlayerData data;
  private final Player player;

  private final ItemStack nextPage, previousPage;
  private int currentPage = 0;

  public ColorInventory(Player player)
  {
    this.player = player;
    Optional<PlayerData> optional = Prefixes.getInstance().get(PlayerDataHolder.class)
        .find(player.getUniqueId());

    if (optional.isEmpty()) {
      throw new IllegalStateException("Player has no DataPlayer (" + player.getName() + ")");
    }

    data = optional.get();
    inventory = Bukkit.createInventory(null, 9 * 3, "§3§lFarbauswahl");

    previousPage = new ItemStack(Material.GHAST_TEAR);
    ItemMeta previousPageItemMeta = previousPage.getItemMeta();
    previousPageItemMeta.setDisplayName("Vorherige Seite");
    previousPage.setItemMeta(previousPageItemMeta);

    nextPage = new ItemStack(Material.GHAST_TEAR);
    ItemMeta nextPageItemMeta = nextPage.getItemMeta();
    nextPageItemMeta.setDisplayName("Nächste Seite");
    nextPage.setItemMeta(nextPageItemMeta);

    paintBackground();
    loadPages();
    setPage(0);

    player.openInventory(inventory);
  }

  private void paintBackground()
  {
    ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    ItemMeta itemMeta = background.getItemMeta();
    Objects.requireNonNull(itemMeta).setDisplayName("§3");
    background.setItemMeta(itemMeta);

    for (int i = 0; i < 9; i++) {
      inventory.setItem(i, background);
      inventory.setItem(i + 18, background);
    }

    inventory.setItem(18, previousPage);
    inventory.setItem(26, nextPage);
  }

  private void setPage(int page)
  {
    pages.stream().filter(inventoryPage -> inventoryPage.getPage() == page).findAny()
        .ifPresent(inventoryPage -> {
          currentPage = page;

          for (int i = 0; i < ITEMS_IN_PAGE; i++) {
            inventory.clear(i + 9);
          }

          for (int i = 0; i < inventoryPage.getStacks().size() && i < ITEMS_IN_PAGE; i++) {
            inventory.setItem(i + 9, inventoryPage.getStacks().get(i));
          }
        });
  }

  private void loadPages()
  {
    List<PrefixColor> colors = Prefixes.getInstance().get(PrefixColorHolder.class).stream().filter(
        prefixColor -> prefixColor.getPermission() == null || player.hasPermission(
            prefixColor.getPermission())).toList();
    int requiredPages = (int) Math.ceil((double) colors.size() / ITEMS_IN_PAGE);

    for (int i = 0; i < requiredPages; i++) {
      List<ItemStack> itemStacks = colors.subList(i * ITEMS_IN_PAGE,
              Math.min(i * ITEMS_IN_PAGE + ITEMS_IN_PAGE - 1, colors.size() - 1)).stream()
          .map(prefixColor -> {
            ItemStack item = new ItemStack(Material.MOJANG_BANNER_PATTERN);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(prefixColor.getDisplayname());
            item.setItemMeta(itemMeta);
            map.put(item, prefixColor);
            return item;
          }).collect(Collectors.toList());
      pages.add(new InventoryPage(i, itemStacks));
    }
  }


  public void handleClick(InventoryClickEvent event)
  {
    if (this.nextPage.equals(event.getCurrentItem())) {
      setPage(currentPage + 1);
    }

    if (this.previousPage.equals(event.getCurrentItem())) {
      setPage(currentPage - 1);
    }
  }
}
