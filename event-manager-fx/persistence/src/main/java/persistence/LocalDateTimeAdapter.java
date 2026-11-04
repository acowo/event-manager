package persistence;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Used by Gson to serialize {@link LocalDateTime} objects to <a
 * href="https://en.wikipedia.org/wiki/ISO_8601">ISO 8601</a> text strings.
 */
public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

  @Override
  public void write(JsonWriter out, LocalDateTime value) throws IOException {
    if (value == null) {
      out.nullValue();
    } else {
      out.value(value.toString());
    }
  }

  @Override
  public LocalDateTime read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    return LocalDateTime.parse(in.nextString());
  }
}
