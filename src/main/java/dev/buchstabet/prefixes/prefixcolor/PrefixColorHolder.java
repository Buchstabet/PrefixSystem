package dev.buchstabet.prefixes.prefixcolor;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import dev.buchstabet.prefixes.Prefixes;
import dev.buchstabet.prefixes.utils.GsonManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PrefixColorHolder extends ArrayList<PrefixColor>
{

  public CompletableFuture<Void> updateColor(UUID uuid, PrefixColor color)
  {
    return CompletableFuture.runAsync(() -> {
      HikariDataSource dataSource = Prefixes.getInstance().get(HikariDataSource.class);
      try (Connection connection = dataSource.getConnection()) {
        PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO player_selected_prefix (uuid, color) VALUES (?, ?) ON DUPLICATE KEY UPDATE color = ?;");
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, color.getId().toString());
        preparedStatement.setString(3, color.getId().toString());
        preparedStatement.execute();
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
    });
  }

  public CompletableFuture<Optional<PrefixColor>> loadColor(UUID uuid)
  {
    return CompletableFuture.supplyAsync(() -> {
      HikariDataSource dataSource = Prefixes.getInstance().get(HikariDataSource.class);
      try (Connection connection = dataSource.getConnection()) {
        PreparedStatement preparedStatement = connection.prepareStatement(
            "select `color` from `player_selected_prefix` where `uuid`=?");
        preparedStatement.setString(1, uuid.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
          return find(resultSet.getString(1));
        }
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
      return Optional.empty();
    });
  }

  public Optional<PrefixColor> find(String uuid)
  {
    return this.stream()
        .filter(prefixColor -> prefixColor.getId().toString().equals(uuid))
        .findFirst();
  }

  public void store()
  {
    File file = new File("plugins/prefixes", "colors.json");
    try (FileWriter writer = new FileWriter(file)) {
      Gson gson = Prefixes.getInstance().get(GsonManager.class).createBuilder().setPrettyPrinting().create();
      String json = gson.toJson(this);
      writer.write(json);
      writer.flush();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
