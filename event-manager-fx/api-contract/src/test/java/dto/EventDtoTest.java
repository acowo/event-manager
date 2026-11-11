package dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import event.Event;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Test class for EventDto.
 */
class EventDtoTest {

  private final String eventId = "event-123";
  private final String title = "Java Workshop";
  private final String place = "Trondheim";
  private final LocalDateTime time = LocalDateTime.of(2026, 11, 15, 18, 0);

  /**
   * Test the constructor and accessor methods of EventDto.
   */
  @Test
  void testConstructorAndAccessors() {
    EventDto eventDto = new EventDto(eventId, title, place, time);

    assertNotNull(eventDto);
    assertEquals(eventId, eventDto.eventId());
    assertEquals(title, eventDto.title());
    assertEquals(place, eventDto.place());
    assertEquals(time, eventDto.time());
  }

  /**
   * Test the fromEvent method of EventDto.
   */
  @Test
  void testFromEvent() {
    Event event = new Event(eventId, title, place, time);

    EventDto eventDto = EventDto.from(event);

    assertNotNull(eventDto);
    assertEquals(eventId, eventDto.eventId());
    assertEquals(title, eventDto.title());
    assertEquals(place, eventDto.place());
    assertEquals(time, eventDto.time());
  }

  /**
   * Test the toEvent method of EventDto.
   */
  @Test
  void testToEvent() {
    EventDto eventDto = new EventDto(eventId, title, place, time);

    Event event = eventDto.toEvent();

    assertNotNull(event);
    assertEquals(eventId, event.getEventId());
    assertEquals(title, event.getTitle());
    assertEquals(place, event.getPlace());
    assertEquals(time, event.getTime());
  }

  /**
   * Test the conversion from Event to EventDto and back to Event.
   */
  @Test
  void testFromAndToEvent() {
    Event originalEvent = new Event(eventId, title, place, time);

    EventDto eventDto = EventDto.from(originalEvent);
    Event convertedEvent = eventDto.toEvent();

    assertEquals(originalEvent.getEventId(), convertedEvent.getEventId());
    assertEquals(originalEvent.getTitle(), convertedEvent.getTitle());
    assertEquals(originalEvent.getPlace(), convertedEvent.getPlace());
    assertEquals(originalEvent.getTime(), convertedEvent.getTime());
  }
}
