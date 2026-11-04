package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class LocalDateTimeAdapterTest {

    private final LocalDateTimeAdapter adapter =
            new LocalDateTimeAdapter();

    @Test
    void testWriteLocalDateTime() throws Exception {
        LocalDateTime time =
                LocalDateTime.of(2026, 9, 22, 12, 30);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        adapter.write(jsonWriter, time);
        jsonWriter.close();

        assertEquals(
                "\"2026-09-22T12:30\"",
                stringWriter.toString()
        );
    }

    @Test
    void testWriteNull() throws Exception {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        adapter.write(jsonWriter, null);
        jsonWriter.close();

        assertEquals("null", stringWriter.toString());
    }

    @Test
    void testReadLocalDateTime() throws Exception {
        JsonReader jsonReader =
                new JsonReader(
                        new StringReader("\"2026-09-22T12:30\"")
                );

        LocalDateTime result = adapter.read(jsonReader);

        assertEquals(
                LocalDateTime.of(2026, 9, 22, 12, 30),
                result
        );
    }

    @Test
    void testReadNull() throws Exception {
        JsonReader jsonReader =
                new JsonReader(new StringReader("null"));

        LocalDateTime result = adapter.read(jsonReader);

        assertNull(result);
    }
}