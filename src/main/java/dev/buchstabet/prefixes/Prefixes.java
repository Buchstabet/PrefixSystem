package dev.buchstabet.prefixes;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import dev.buchstabet.prefixes.command.PrefixCommand;
import dev.buchstabet.prefixes.listeners.PlayerJoinListener;
import dev.buchstabet.prefixes.player.DisplayData;
import dev.buchstabet.prefixes.player.PlayerData;
import dev.buchstabet.prefixes.player.PlayerDataHolder;
import dev.buchstabet.prefixes.prefixcolor.PrefixColor;
import dev.buchstabet.prefixes.prefixcolor.PrefixColorHolder;
import dev.buchstabet.prefixes.team.Team;
import dev.buchstabet.prefixes.team.TeamHolder;
import dev.buchstabet.prefixes.utils.Database;
import dev.buchstabet.prefixes.utils.GsonManager;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
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
    saveDefaultConfig();

    GsonManager gsonManager = new GsonManager();
    register(gsonManager);
    gsonManager.registerTypeAdapter(gsonBuilder -> {
      gsonBuilder.registerTypeAdapter(PrefixColor.class, new PrefixColor.Serializer());
    });

    loadConfig(TeamHolder.class, new File("plugins/prefixes", "teams.json"), () -> {
      Team team = new Team(UUID.randomUUID(), 99, new DisplayData("§7Spieler ~ %p "),
          new DisplayData("§7%p "), new DisplayData("&7Spieler &8- &f%p &7(default)"), null);
      return new TeamHolder(team.getId(), Arrays.asList(team));
    });
    loadConfig(PrefixColorHolder.class, new File("plugins/prefixes", "colors.json"),
        PrefixColorHolder::new);

    ConfigurationSection databaseConfig = getConfig().getConfigurationSection("database");
    if (databaseConfig == null) {
      return;
    }

    Database database = new Database(databaseConfig.getString("hostname"),
        databaseConfig.getInt("port"), 2, databaseConfig.getString("database"),
        databaseConfig.getString("username"), databaseConfig.getString("password"));
    HikariDataSource hikariDataSource = database.start();
    System.out.println(hikariDataSource);
    register(hikariDataSource);

    getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    Objects.requireNonNull(getCommand("prefix")).setExecutor(new PrefixCommand());
    PlayerDataHolder playerData = new PlayerDataHolder();
    Bukkit.getOnlinePlayers().forEach(playerData::add);

    register(playerData);
    playerData.forEach(PlayerData::updatePrefix);
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
      try (FileWriter fileWriter = new FileWriter(file)) {
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