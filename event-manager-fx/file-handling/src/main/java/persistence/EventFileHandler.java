package persistence;

import event.Event;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class EventFileHandler {

  public void saveEvent(Event event, Path path) throws IOException {
    List<String> lines = List.of(
        event.getTitle(),
        event.getPlace(),
        event.getTime().toString()
    );

    Files.write(path, lines);
  }

  public Event loadEvent(Path path) throws IOException {
    List<String> lines = Files.readAllLines(path);

    if (lines.size() < 3) {
      throw new IOException("Invalid event file");
    }

    String title = lines.get(0);
    String place = lines.get(1);
    LocalDateTime time = LocalDateTime.parse(lines.get(2));

    return new Event(title, place, time);
  }
}