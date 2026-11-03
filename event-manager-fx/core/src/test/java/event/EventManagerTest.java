package event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class EventManagerTest {

  private EventManager eventManager;
  private Event event;

    @BeforeEach
    void setUp() {
        EventRepository repository = new FakeEventRepository();
        eventManager = new EventManager(repository);

        event = new Event(
            "Test Event",
            "Trondheim",
            LocalDateTime.of(2026, 10, 1, 18, 0)
        );
    }

    @Test
    void testAddEvent() {
        assertTrue(eventManager.addEvent(event));
        assertFalse(eventManager.addEvent(event));
    }

    @Test
    void allowsSameTitleAndReplacesEventAtExistingKey() {
        Event other = new Event(event.getTitle(), "Bergen", event.getTime());
        assertTrue(eventManager.addEvent(event));
        assertTrue(eventManager.addEvent(other));
        assertEquals(2, eventManager.getEvents().size());
        Event replacement = new Event(event.getEventId(), "Renamed", "Oslo", event.getTime());
        assertTrue(eventManager.editEvent(event.getEventId(), replacement));
        assertEquals(replacement, eventManager.getEvent(event.getEventId()));
        assertEquals(event.getEventId(), eventManager.getEvent(event.getEventId()).getEventId());
        assertEquals(2, eventManager.getEvents().size());
        assertEquals("Bergen", eventManager.getEvent(other.getEventId()).getPlace());
        assertTrue(eventManager.removeEvent(event.getEventId()));
        assertEquals(other.getEventId(), eventManager.getEvent(other.getEventId()).getEventId());
    }

    @Test 
    void testRemoveEvent() {
        eventManager.addEvent(event);

        assertTrue(eventManager.removeEvent(event.getEventId()));
        assertFalse(eventManager.removeEvent(event.getEventId()));
    }

    @Test
    void testEditEvent() {
        eventManager.addEvent(event);

        Event editedEvent = new Event(event.getEventId(),
                "Test Event",
                "Oslo",
                LocalDateTime.of(2026, 11, 1, 20, 0)
        );

        assertTrue(eventManager.editEvent(event.getEventId(), editedEvent));

        Event result = eventManager.getEvent(event.getEventId());

        assertEquals("Oslo", result.getPlace());
        assertEquals(
            LocalDateTime.of(2026, 11, 1, 20, 0),
            result.getTime()
        );
    }

    @Test
    void testEditNonexistentEvent() {
        Event editedEvent = new Event(event.getEventId(),
                "Does Not Exist",
                "Oslo",
                LocalDateTime.of(2026, 11, 1, 20, 0)
        );

        assertFalse(
                eventManager.editEvent(UUID.randomUUID().toString(), editedEvent)
        );
    }

    @Test
    void testGetEvent() {
        eventManager.addEvent(event);

        Event result = eventManager.getEvent(event.getEventId());

        assertEquals("Test Event", result.getTitle());
    }

    @Test
    void testGetEvents() {
        eventManager.addEvent(event);

        Map<String, Event> result = eventManager.getEvents();

    assertTrue(result.containsKey(event.getEventId()));
  }

  private static class FakeEventRepository implements EventRepository {

    private final Map<String, Event> events = new HashMap<>();

    @Override
    public Event getEvent(String title) {
      if (!eventExists(title)) {
        throw new IllegalArgumentException("Event does not exist: " + title);
      }
      return events.get(title);
    }

    @Override
    public boolean eventExists(String eventId) {
      return events.containsKey(eventId);
    }

    @Override
    public void addEvent(Event event) {
      events.put(event.getEventId(), event);
    }

    @Override
    public void removeEvent(String eventId) {
      events.remove(eventId);
    }

    @Override
    public void editEvent(String eventId, Event event) {
      events.put(eventId, event);
    }

    @Override
    public Map<String, Event> getEvents() {
      return new HashMap<>(events);
    }

    @Override
    public FakeEventRepository copy() {
      return this;
    }
  }
}
