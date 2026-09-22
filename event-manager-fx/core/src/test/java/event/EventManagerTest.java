package event;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class EventManagerTest {

    private EventManager eventManager;
    private Event event;

    private static class FakeEventRepository implements EventRepository {

        private final Map<String, Event> events = new HashMap<>();

        @Override
        public Event getEvent(String title) {
            if (!eventExists(title)) {
                throw new IllegalArgumentException(
                        "Event does not exist: " + title
                );
            }
            return events.get(title);
        }

        @Override
        public boolean eventExists(String title) {
            return events.containsKey(title);
        }

        @Override
        public void addEvent(Event event) {
            events.put(event.getTitle(), event);
        }

        @Override
        public void removeEvent(String title) {
            events.remove(title);
        }

        @Override
        public void editEvent(String title, Event event) {
            events.put(title, event);
        }

        @Override
        public Map<String, Event> getEvents() {
            return new HashMap<>(events);
        }
    }

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
    void testRemoveEvent() {
        eventManager.addEvent(event);

        assertTrue(eventManager.removeEvent("Test Event"));
        assertFalse(eventManager.removeEvent("Test Event"));
    }

    @Test
    void testEditEvent() {
        eventManager.addEvent(event);

        Event editedEvent = new Event(
                "Test Event",
                "Oslo",
                LocalDateTime.of(2026, 11, 1, 20, 0)
        );

        assertTrue(eventManager.editEvent("Test Event", editedEvent));

        Event result = eventManager.getEvent("Test Event");

        assertEquals("Oslo", result.getPlace());
        assertEquals(
            LocalDateTime.of(2026, 11, 1, 20, 0),
            result.getTime()
        );
    }

    @Test
    void testEditNonexistentEvent() {
        Event editedEvent = new Event(
                "Does Not Exist",
                "Oslo",
                LocalDateTime.of(2026, 11, 1, 20, 0)
        );

        assertFalse(
                eventManager.editEvent("Does Not Exist", editedEvent)
        );
    }

    @Test
    void testGetEvent() {
        eventManager.addEvent(event);

        Event result = eventManager.getEvent("Test Event");

        assertEquals("Test Event", result.getTitle());
    }

    @Test
    void testGetEvents() {
        eventManager.addEvent(event);

        Map<String, Event> result = eventManager.getEvents();

        assertTrue(result.containsKey("Test Event"));
    }
}
