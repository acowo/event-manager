package persistence;

import event.Event;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class EventFileHandlerTest {

  @TempDir
  Path tempDir;

  @Test
  void testSaveAndLoadEvent() throws IOException {
    EventFileHandler handler = new EventFileHandler();

    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);
    Event originalEvent = new Event("Game Night", "Gløshaugen", time);

    Path file = tempDir.resolve("event.txt");

    handler.saveEvent(originalEvent, file);
    Event loadedEvent = handler.loadEvent(file);

    Assertions.assertEquals(originalEvent.getTitle(), loadedEvent.getTitle());
    Assertions.assertEquals(originalEvent.getPlace(), loadedEvent.getPlace());
    Assertions.assertEquals(originalEvent.getTime(), loadedEvent.getTime());
  }
}