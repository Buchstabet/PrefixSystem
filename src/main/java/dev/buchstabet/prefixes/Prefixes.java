package dev.buchstabet.prefixes;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import dev.buchstabet.prefixes.command.ColorCommand;
import dev.buchstabet.prefixes.listeners.PlayerJoinListener;
import dev.buchstabet.prefixes.player.DisplayData;
import dev.buchstabet.prefixes.player.PlayerDataHolder;
import dev.buchstabet.prefixes.prefixcolor.MultiColorPrefix;
import dev.buchstabet.prefixes.prefixcolor.OneColorPrefix;
import dev.buchstabet.prefixes.prefixcolor.PrefixColor;
import dev.buchstabet.prefixes.prefixcolor.PrefixColorHolder;
import dev.buchstabet.prefixes.prefixcolor.PrefixNameSuffixColor;
import dev.buchstabet.prefixes.team.Team;
import dev.buchstabet.prefixes.team.TeamHolder;
import dev.buchstabet.prefixes.utils.Database;
import dev.buchstabet.prefixes.utils.GsonManager;
import dev.buchstabet.prefixes.utils.itembuilder.ItemBuilderManager;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class Prefixes extends JavaPlugin
{

  private final Map<Class<?>, Object> instanceHolder = new HashMap<>();
  @Getter private static Prefixes instance;

  @Override
  public void onDisable()
  {

  }

  @Override
  public void onEnable()
  {
    ItemBuilderManager itemBuilderManager = new ItemBuilderManager();
    register(ItemBuilderManager.class, itemBuilderManager);
    getServer().getPluginManager().registerEvents(itemBuilderManager, this);

    saveDefaultConfig();

    GsonManager gsonManager = new GsonManager();
    register(gsonManager);
    gsonManager.registerTypeAdapter(gsonBuilder -> {
      Map<String, Class<? extends PrefixColor>> classes = new HashMap<>();
      classes.put("multi_color_prefix", MultiColorPrefix.class);
      classes.put("one_color_prefix", OneColorPrefix.class);
      classes.put("prefix_name_suffix", PrefixNameSuffixColor.class);

      gsonBuilder.registerTypeAdapter(PrefixColor.class, new PrefixColor.Serializer(classes));
    });

    loadConfig(TeamHolder.class, new File("plugins/prefixes", "teams.json"), () -> {
      Team player = new Team(UUID.randomUUID(), 99, ChatColor.GRAY,
          new DisplayData("&7Spieler | ", ""), new DisplayData("&7", ""),
          new DisplayData("&7Spieler &8- &f", ""), null, true);

      Team admin = new Team(UUID.randomUUID(), 1, ChatColor.DARK_RED,
          new DisplayData("&4Op | ", ""), new DisplayData("&7", ""),
          new DisplayData("&4Operator &8- &f", ""), "prefix.operator", true);
      return new TeamHolder(player.getId(), Arrays.asList(player, admin));
    });
    loadConfig(PrefixColorHolder.class, new File("plugins/prefixes", "colors.json"), () -> {
      PrefixColorHolder prefixColors = new PrefixColorHolder();
      prefixColors.add(new MultiColorPrefix(UUID.randomUUID(), "Multi color prefix",
          "prefixes.color.multivcolor", List.of("&c", "&4", "&f"), 2));
      prefixColors.add(
          new OneColorPrefix(UUID.randomUUID(), "One color prefix", "prefixes.color.onecolor",
              "&c"));
      prefixColors.add(new PrefixNameSuffixColor(UUID.randomUUID(), "Prefix name suffix color",
          "prefixes.color.pns", "&6", "&f", "&e"));
      return prefixColors;
    });

    ConfigurationSection databaseConfig = getConfig().getConfigurationSection("database");
    if (databaseConfig == null) {
      return;
    }

    Database database = new Database(databaseConfig.getString("hostname"),
        databaseConfig.getInt("port"), 2, databaseConfig.getString("database"),
        databaseConfig.getString("username"), databaseConfig.getString("password"));
    HikariDataSource hikariDataSource = database.start();
    register(hikariDataSource);

    getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

    ColorCommand colorCommand = new ColorCommand();
    Objects.requireNonNull(getCommand("color")).setExecutor(colorCommand);
    getServer().getPluginManager().registerEvents(colorCommand, this);

    PlayerDataHolder playerData = new PlayerDataHolder();
    Bukkit.getOnlinePlayers().forEach(playerData::add);

    register(playerData);
    playerData.forEach(playerData1 -> playerData1.updatePrefix(null));

    int updateTime = getConfig().getInt("updatetime");
    PrefixUpdateTask prefixUpdateTask = new PrefixUpdateTask();
    Bukkit.getScheduler().runTaskTimer(this, prefixUpdateTask, 0L, updateTime * 20L);
    register(PrefixUpdateTask.class, prefixUpdateTask);
    register(VisualizationProperties.class,
        loadProperties("visualization.properties", VisualizationProperties.class));

    System.out.println(Arrays.stream(ChatColor.values()).map(Enum::name).collect(Collectors.joining("\n")));
  }

  @SneakyThrows
  public static <T extends Properties> T loadProperties(String path, Class<T> clazz)
  {
    if (path == null) {
      return null;
    }

    URL url = Prefixes.class.getClassLoader().getResource(path);
    if (url == null) {
      return null;
    }

    URLConnection urlConnection = url.openConnection();
    urlConnection.setUseCaches(false);
    T properties = clazz.newInstance();
    try (InputStream in = urlConnection.getInputStream(); InputStreamReader inputStreamReader = new InputStreamReader(
        in, StandardCharsets.UTF_8)) {
      properties.load(inputStreamReader);
    }
    return properties;
  }

  @Override
  public void onLoad()
  {
    instance = this;
  }

  public <T> T get(Class<T> clazz)
  {
    return find(clazz).orElse(null);
  }

  public <T> Optional<T> find(Class<T> clazz)
  {
    Object o = instanceHolder.get(clazz);
    if (clazz.isInstance(o)) {
      return Optional.of(clazz.cast(o));
    }
    return Optional.empty();
  }

  public void register(Class<?> clazz, Object t)
  {
    instanceHolder.putIfAbsent(clazz, t);
  }

  public void register(Object t)
  {
    register(t.getClass(), t);
  }

  public void unregister(Class<?> clazz)
  {
    instanceHolder.remove(clazz);
  }

  public <T> T getAndUnregister(Class<T> clazz)
  {
    return (T) instanceHolder.remove(clazz);
  }

  public <T> Optional<T> findAndUnregister(Class<T> clazz)
  {
    return Optional.ofNullable(getAndUnregister(clazz));
  }

  public <T> void overwrite(Class<T> clazz, T t)
  {
    instanceHolder.put(clazz, t);
  }

  public <T> void ifPresent(Class<T> clazz, Consumer<T> consumer)
  {
    find(clazz).ifPresent(consumer);
  }

  public boolean contains(Class<?> clazz)
  {
    return instanceHolder.containsKey(clazz);
  }

  @SneakyThrows
  public <T> T loadConfig(Class<T> clazz, File file, Callable<T> callable)
  {
    Gson gson = get(GsonManager.class).createBuilder().setPrettyPrinting().create();

    T t;
    if (file.createNewFile()) {
      t = callable.call();
      try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
        fileWriter.write(gson.toJson(t));
        fileWriter.flush();
      }
    } else {
      t = gson.fromJson(
          new String(Files.readAllBytes(Paths.get(file.toURI())), StandardCharsets.UTF_8), clazz);
    }
    if (t == null) {
      t = callable.call();
    }
    overwrite(clazz, t);
    return t;
  }
}
