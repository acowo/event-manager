package event;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for Event.
 */
class EventTest {

  /**
   * Test the creation of an Event object and verify its properties.
   */
  @Test
  void testCreateEvent() {
    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);

    String eventId = UUID.randomUUID().toString();
    Event event = new Event(eventId, "Game Night", "Gløshaugen", time);
    Assertions.assertEquals(eventId, event.getEventId());

    Assertions.assertEquals("Game Night", event.getTitle());
    Assertions.assertEquals("Gløshaugen", event.getPlace());
    Assertions.assertEquals(time, event.getTime());
  }

  /**
   * Test that creating an Event with a null or blank ID throws an IllegalArgumentException.
   */
  @Test
  void testInvalidIdThrowsException() {
    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> new Event(null, "Game Night", "Gløshaugen", time));
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> new Event(" ", "Game Night", "Gløshaugen", time));
  }

  /**
   * Test that creating an Event with a null or blank title throws an IllegalArgumentException.
   */
  @Test
  void testNullTitleThrowsException() {
    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> new Event(null, "Gløshaugen", time));
  }

  /**
   * Test that creating an Event with a null or blank place throws an IllegalArgumentException.
   */
  @Test
  void testBlankTitleThrowsException() {
    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> new Event(" ", "Gløshaugen", time));
  }

  /**
   * Test that creating an Event with a null place throws an IllegalArgumentException.
   */
  @Test
  void testNullPlaceThrowsException() {
    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> new Event("Game Night", null, time));
  }

  /**
   * Test that creating an Event with a null or blank place throws an IllegalArgumentException.
   */
  @Test
  void testBlankPlaceThrowsException() {
    LocalDateTime time = LocalDateTime.of(2026, 9, 20, 18, 0);

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> new Event("Game Night", " ", time));
  }

  /**
   * Test that creating an Event with a null time throws an IllegalArgumentException.
   */
  @Test
  void testNullTimeThrowsException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> new Event("Game Night", "Gløshaugen", null));
  }
}
