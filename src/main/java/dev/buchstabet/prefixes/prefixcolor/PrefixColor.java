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
import java.util.UUID;
import lombok.SneakyThrows;

public interface PrefixColor
{

  UUID getId();

  String getPermission();

  String colorize(String input, DisplayNameType type);

  String getDisplayname();

  class Serializer implements JsonDeserializer<PrefixColor>, JsonSerializer<PrefixColor>
  {


    @SneakyThrows
    @Override
    public PrefixColor deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
      JsonObject obj = jsonElement.getAsJsonObject();
      Class<?> aClass = Class.forName(obj.get("type").getAsString());
      return jsonDeserializationContext.deserialize(obj.get("data"), aClass);
    }

    @Override
    public JsonElement serialize(PrefixColor prefixColor, Type type,
        JsonSerializationContext jsonSerializationContext)
    {
      JsonObject obj = new JsonObject();
      obj.addProperty("type", prefixColor.getClass().getName());
      obj.add("data", jsonSerializationContext.serialize(prefixColor));
      return obj;
    }
  }

}
