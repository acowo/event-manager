package event;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EventTest {

  @Test
  void testCreateEvent() {
    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);

    Event event = new Event("Game Night", "Gløshaugen", time);

    Assertions.assertEquals("Game Night", event.getTitle());
    Assertions.assertEquals("Gløshaugen", event.getPlace());
    Assertions.assertEquals(time, event.getTime());
  }

  @Test
  void testNullTitleThrowsException() {
    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new Event(null, "Gløshaugen", time)
    );
  }

  @Test
  void testBlankTitleThrowsException() {
    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new Event(" ", "Gløshaugen", time)
    );
  }

  @Test
  void testNullPlaceThrowsException() {
    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new Event("Game Night", null, time)
    );
  }

  @Test
  void testBlankPlaceThrowsException() {
    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new Event("Game Night", " ", time)
    );
  }

  @Test
  void testNullTimeThrowsException() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new Event("Game Night", "Gløshaugen", null)
    );
  }

}