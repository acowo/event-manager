package persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import event.Event;
import java.nio.file.Path;
import java.nio.file.Files;
import org.junit.jupiter.api.io.TempDir;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventFileManagerTest {

    @TempDir
    Path tempDir;

    private EventFileManager eventFileManager;
    private Event event;

    @BeforeEach
    void setUp() {
        eventFileManager = new EventFileManager(
            tempDir.resolve("eventdata.json").toString()
        );

        event = new Event(
                "Test Event",
                "Trondheim",
                LocalDateTime.of(2026, 10, 1, 18, 0)
        );

        // Make sure tests start with no test event.
        if (eventFileManager.eventExists(event.getEventId())) {
            eventFileManager.removeEvent(event.getEventId());
        }
    }

    @Test
    void testAddAndGetEvent() {
        eventFileManager.addEvent(event);

        Event result = eventFileManager.getEvent(event.getEventId());

        assertEquals("Test Event", result.getTitle());
        assertEquals("Trondheim", result.getPlace());
        assertEquals(
                LocalDateTime.of(2026, 10, 1, 18, 0),
                result.getTime()
        );
    }

    @Test
    void testEventExists() {
        assertFalse(eventFileManager.eventExists(event.getEventId()));

        eventFileManager.addEvent(event);

        assertTrue(eventFileManager.eventExists(event.getEventId()));
    }

    @Test
    void testGetEvents() {
        eventFileManager.addEvent(event);

        Map<String, Event> events = eventFileManager.getEvents();

        assertTrue(events.containsKey(event.getEventId()));
        assertEquals(
                event.getTitle(),
                events.get(event.getEventId()).getTitle()
        );
    }

    @Test
    void testDuplicateEventThrowsException() {
        eventFileManager.addEvent(event);

        assertThrows(
                IllegalArgumentException.class,
                () -> eventFileManager.addEvent(event)
        );
    }

    @Test
    void testGetNonexistentEventThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> eventFileManager.getEvent(UUID.randomUUID().toString())
        );
    }

    @Test
    void testRemoveEvent() {
        eventFileManager.addEvent(event);

        assertTrue(eventFileManager.eventExists(event.getEventId()));

        eventFileManager.removeEvent(event.getEventId());

        assertFalse(eventFileManager.eventExists(event.getEventId()));
    }

    @Test
    void testRemoveNonexistentEventThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> eventFileManager.removeEvent(UUID.randomUUID().toString())
        );
    }

    @Test
    void testEditEvent() {
        eventFileManager.addEvent(event);

        Event editedEvent = new Event(event.getEventId(),
                "Test Event",
                "Oslo",
                LocalDateTime.of(2026, 11, 1, 20, 0)
        );

        eventFileManager.editEvent(event.getEventId(), editedEvent);

        Event result = eventFileManager.getEvent(event.getEventId());

        assertEquals("Test Event", result.getTitle());
        assertEquals("Oslo", result.getPlace());
        assertEquals(
                LocalDateTime.of(2026, 11, 1, 20, 0),
                result.getTime()
        );
    }

    @Test
    void testEditNonexistentEventThrowsException() {
        Event editedEvent = new Event(event.getEventId(),
                "Does Not Exist",
                "Oslo",
                LocalDateTime.of(2026, 11, 1, 20, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> eventFileManager.editEvent(
                        UUID.randomUUID().toString(),
                        editedEvent
                )
        );
    }

    @Test 
    void testEventPersistToFile() {
        eventFileManager.addEvent(event);

        EventFileManager reloadedManager = new EventFileManager(
            tempDir.resolve("eventdata.json").toString()
        );

        assertTrue(reloadedManager.eventExists(event.getEventId()));

        Event reloadedEvent = reloadedManager.getEvent(event.getEventId());

        assertEquals("Test Event", reloadedEvent.getTitle());
        assertEquals("Trondheim", reloadedEvent.getPlace());
        assertEquals(
            LocalDateTime.of(2026, 10, 1, 18, 0),
            reloadedEvent.getTime()
        );

    }



    @Test
    void sameNamesRemainIndependentAfterReloadAndEdit() {
        Event other = new Event(event.getTitle(), "Bergen", event.getTime());
        eventFileManager.addEvent(event);
        eventFileManager.addEvent(other);
        EventFileManager reloaded = eventFileManager.copy();
        assertEquals(2, reloaded.getEvents().size());
        assertThrows(IllegalArgumentException.class,
                () -> eventFileManager.editEvent(event.getEventId(), other));
        assertEquals(event.getTitle(), eventFileManager.copy().getEvent(event.getEventId()).getTitle());
        assertEquals(event.getEventId(), eventFileManager.copy().getEvent(event.getEventId()).getEventId());
        Event replacement = new Event(event.getEventId(), "Renamed", "Oslo", event.getTime());
        reloaded.editEvent(event.getEventId(), replacement);
        reloaded = reloaded.copy();
        assertEquals(event.getEventId(), reloaded.getEvent(event.getEventId()).getEventId());
        assertTrue(reloaded.eventExists(replacement.getEventId()));
        assertEquals(2, reloaded.getEvents().size());
        assertEquals("Renamed", reloaded.getEvent(event.getEventId()).getTitle());
        assertEquals("Bergen", reloaded.getEvent(other.getEventId()).getPlace());
        reloaded.removeEvent(event.getEventId());
        assertTrue(reloaded.copy().eventExists(other.getEventId()));
        assertFalse(reloaded.copy().eventExists(event.getEventId()));
    }

    @Test
    void attendanceUsesUuidAndSurvivesReload() {
        Event other = new Event(event.getTitle(), "Bergen", event.getTime());
        eventFileManager.addEvent(event);
        eventFileManager.addEvent(other);
        UserFileManager users = new UserFileManager(tempDir.resolve("userdata.json").toString());
        user.User attendee = new user.User("test", "test");
        attendee.addEventId(event.getEventId(), true);
        users.addUser(attendee);
        assertTrue(users.copy().getUser("test").isAttendingEvent(event.getEventId()));
        assertFalse(users.getUser("test").isAttendingEvent(other.getEventId()));
        users.removeEvent(other.getEventId());
        assertTrue(users.getUser("test").isAttendingEvent(event.getEventId()));
        users.removeEvent(event.getEventId());
        assertTrue(users.copy().getUser("test").getEventIds().isEmpty());
    }

    @Test
    void testLoadEmptyFile() throws Exception {
        Path file = tempDir.resolve("empty.json");
        Files.createFile(file);

        EventFileManager manager = new EventFileManager(file.toString());

        assertTrue(manager.getEvents().isEmpty());
    }

    @Test
    void testLoadNullJson() throws Exception {
        Path file = tempDir.resolve("null.json");
        Files.writeString(file, "null");

        EventFileManager manager = new EventFileManager(file.toString());

        assertTrue(manager.getEvents().isEmpty());
    }

    @Test
    void testSaveToInvalidPathThrowsException() {
        Path invalidPath = tempDir.resolve("does-not-exist").resolve("eventdata.json");

        EventFileManager manager = new EventFileManager(invalidPath.toString());

        assertThrows(RuntimeException.class, () -> manager.addEvent(event));
    }

    @Test
    void testDefaultConstructor() {
        EventFileManager manager = new EventFileManager();
        assertTrue(manager.getEvents() != null);
    }

    @Test
    void testSuccesfullCopy() {
        eventFileManager.addEvent(event);
        EventFileManager copy = eventFileManager.copy();

        Event copiedEvent = copy.getEvent(event.getEventId());
        assertEquals(event.getTitle(), copiedEvent.getTitle());
        assertEquals(event.getPlace(), copiedEvent.getPlace());
        assertEquals(event.getTime(), copiedEvent.getTime());
        assertEquals(eventFileManager.getEvents().keySet(), copy.getEvents().keySet());
    }
}
