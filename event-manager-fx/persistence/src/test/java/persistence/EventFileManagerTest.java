package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import event.Event;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventFileManagerTest {

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event(
                "Test Event",
                "Trondheim",
                LocalDateTime.of(2026, 10, 1, 18, 0)
        );

        // Make sure tests start with no test event.
        if (EventFileManager.eventExists("Test Event")) {
            EventFileManager.removeEvent("Test Event");
        }
    }

    @Test
    void testAddAndGetEvent() {
        EventFileManager.addEvent(event);

        Event result = EventFileManager.getEvent("Test Event");

        assertEquals("Test Event", result.getTitle());
        assertEquals("Trondheim", result.getPlace());
        assertEquals(
                LocalDateTime.of(2026, 10, 1, 18, 0),
                result.getTime()
        );
    }

    @Test
    void testEventExists() {
        assertFalse(EventFileManager.eventExists("Test Event"));

        EventFileManager.addEvent(event);

        assertTrue(EventFileManager.eventExists("Test Event"));
    }

    @Test
    void testGetEvents() {
        EventFileManager.addEvent(event);

        Map<String, Event> events = EventFileManager.getEvents();

        assertTrue(events.containsKey("Test Event"));
        assertEquals(event.getTitle(), events.get("Test Event").getTitle());
    }

    @Test
    void testDuplicateEventThrowsException() {
        EventFileManager.addEvent(event);

        assertThrows(
                IllegalArgumentException.class,
                () -> EventFileManager.addEvent(event)
        );
    }

    @Test
    void testGetNonexistentEventThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> EventFileManager.getEvent("Does Not Exist")
        );
    }

    @Test
    void testRemoveEvent() {
        EventFileManager.addEvent(event);

        assertTrue(EventFileManager.eventExists("Test Event"));

        EventFileManager.removeEvent("Test Event");

        assertFalse(EventFileManager.eventExists("Test Event"));
    }

    @Test
    void testRemoveNonexistentEventThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> EventFileManager.removeEvent("Does Not Exist")
        );
    }

    @Test
    void testEditEvent() {
        EventFileManager.addEvent(event);

        Event editedEvent = new Event(
                "Test Event",
                "Oslo",
                LocalDateTime.of(2026, 11, 1, 20, 0)
        );

        EventFileManager.editEvent("Test Event", editedEvent);

        Event result = EventFileManager.getEvent("Test Event");

        assertEquals("Test Event", result.getTitle());
        assertEquals("Oslo", result.getPlace());
        assertEquals(
                LocalDateTime.of(2026, 11, 1, 20, 0),
                result.getTime()
        );
    }

    @Test
    void testEditNonexistentEventThrowsException() {
        Event editedEvent = new Event(
                "Does Not Exist",
                "Oslo",
                LocalDateTime.of(2026, 11, 1, 20, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> EventFileManager.editEvent(
                        "Does Not Exist",
                        editedEvent
                )
        );
    }
}
