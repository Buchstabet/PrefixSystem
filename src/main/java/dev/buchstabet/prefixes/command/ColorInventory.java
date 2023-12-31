package dev.buchstabet.prefixes.command;

import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.VisualizationProperties;
import dev.buchstabet.prefixes.player.PlayerData;
import dev.buchstabet.prefixes.player.PlayerDataHolder;
import dev.buchstabet.prefixes.prefixcolor.PrefixColor;
import dev.buchstabet.prefixes.prefixcolor.PrefixColorHolder;
import dev.buchstabet.prefixes.utils.DisplayNameType;
import dev.buchstabet.prefixes.utils.itembuilder.ItemBuilder;
import dev.buchstabet.prefixes.utils.itembuilder.ItemBuilder.Enchantment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
public class ColorInventory
{

  private static final int ITEMS_IN_PAGE = 9;

  private final Inventory inventory;
  private final Map<ItemStack, PrefixColor> map = new HashMap<>();
  private final List<InventoryPage> pages = new ArrayList<>();
  private final PlayerData data;
  private final Player player;

  private final ItemStack nextPage, previousPage, removeColor;
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
    VisualizationProperties properties = Prefixes.getInstance().get(VisualizationProperties.class);
    inventory = Bukkit.createInventory(null, 9 * 3,
        properties.getProperty("color.select.inventory.title"));

    previousPage = new ItemBuilder(Material.GHAST_TEAR).setDisplayname(
        properties.getProperty("color.select.item.previous.page")).build();
    nextPage = new ItemBuilder(Material.GHAST_TEAR).setDisplayname(
        properties.getProperty("color.select.item.next.page")).build();
    removeColor = new ItemBuilder(Material.BARRIER).setDisplayname(
        properties.getProperty("color.select.item.delete.color")).build();

    paintBackground();
    loadPages();
    setPage(0);

    player.openInventory(inventory);
  }

  private void paintBackground()
  {
    ItemStack background = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("§3")
        .build();

    for (int i = 0; i < 9; i++) {
      inventory.setItem(i, background);
      inventory.setItem(i + 18, background);
    }

    inventory.setItem(18, previousPage);
    inventory.setItem(26, nextPage);

    if (data.getColor() != null)
      inventory.setItem(22, removeColor);
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
          Math.min(i * ITEMS_IN_PAGE + ITEMS_IN_PAGE, colors.size())).stream().map(prefixColor -> {

        String prefix = "", name = player.getName(), suffix = "";
        if (data.getTeam() != null) {
          prefix = ChatColor.stripColor(data.getTeam().getTab().getPrefix());
          suffix = ChatColor.stripColor(data.getTeam().getTab().getSuffix());
        }

        String displayname =
            prefixColor.colorize(prefix, DisplayNameType.PREFIX) + prefixColor.colorize(name,
                DisplayNameType.NAME) + prefixColor.colorize(suffix, DisplayNameType.SUFFIX);

        VisualizationProperties properties = Prefixes.getInstance()
            .get(VisualizationProperties.class);
        ItemStack item = new ItemBuilder(
            Material.valueOf(properties.getProperty("color.select.entry.material"))).setDisplayname(
                prefixColor.getDisplayname()).addEnchantments(
                new Enchantment(org.bukkit.enchantments.Enchantment.ARROW_INFINITE, 99)).hideEnchants()
            .setLore(properties.getProperty("color.select.entry.preview").replace("%p", displayname)
                .split("%n")).build();
        map.put(item, prefixColor);
        return item;
      }).collect(Collectors.toList());
      pages.add(new InventoryPage(i, itemStacks));
    }
  }

  private void selectColor(PrefixColor prefixColor)
  {
    player.closeInventory();

    data.setColor(prefixColor);
    Prefixes.getInstance().get(PrefixColorHolder.class).updateColor(data)
        .whenComplete((unused, throwable) -> {
          if (throwable != null) {
            throwable.printStackTrace();
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 10);
            Bukkit.broadcast("[prefixes] §4Error: " + throwable.getMessage(),
                "prefixes.error.broadcast");
            return;
          }

          player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 10);
        });
  }


  public void handleClick(InventoryClickEvent event)
  {
    if (this.nextPage.equals(event.getCurrentItem())) {
      setPage(currentPage + 1);
      return;
    }

    if (this.previousPage.equals(event.getCurrentItem())) {
      setPage(currentPage - 1);
      return;
    }

    if (this.removeColor.equals(event.getCurrentItem())) {
      selectColor(null);
      return;
    }

    if (map.containsKey(event.getCurrentItem()))
      selectColor(map.get(event.getCurrentItem()));
  }
}
