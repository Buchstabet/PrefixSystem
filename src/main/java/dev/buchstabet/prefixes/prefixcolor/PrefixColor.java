package dev.buchstabet.prefixes.prefixcolor;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.buchstabet.prefixes.utils.DisplayNameType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

public interface PrefixColor
{

  UUID getId();

  String getPermission();

  String colorize(String input, DisplayNameType type);

  String getDisplayname();

  @RequiredArgsConstructor
  class Serializer implements JsonDeserializer<PrefixColor>, JsonSerializer<PrefixColor>
  {

    private final Map<String, Class<? extends PrefixColor>> classes;

    @SneakyThrows
    @Override
    public PrefixColor deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
      JsonObject obj = jsonElement.getAsJsonObject();
      Class<?> aClass = classes.get(obj.get("type").getAsString());
      if (aClass == null)
        return null;
      return jsonDeserializationContext.deserialize(obj.get("data"), aClass);
    }

    @Override
    public JsonElement serialize(PrefixColor prefixColor, Type type,
        JsonSerializationContext jsonSerializationContext)
    {
      JsonObject obj = new JsonObject();
      classes.forEach((s, aClass) -> {
        if (!aClass.equals(prefixColor.getClass()))
          return;
        obj.addProperty("type", s);
      });

      if (obj.get("type") == null)
        return null;
      obj.add("data", jsonSerializationContext.serialize(prefixColor));
      return obj;
    }
  }

}
