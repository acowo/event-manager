package persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import event.Event;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.io.TempDir;
import java.time.LocalDateTime;
import java.util.Map;
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
    }

    @Test
    void testAddAndGetEvent() {
        eventFileManager.addEvent(event);

        Event result = eventFileManager.getEvent("Test Event");

        assertEquals("Test Event", result.getTitle());
        assertEquals("Trondheim", result.getPlace());
        assertEquals(
                LocalDateTime.of(2026, 10, 1, 18, 0),
                result.getTime()
        );
    }

    @Test
    void testEventExists() {
        assertFalse(eventFileManager.eventExists("Test Event"));

        eventFileManager.addEvent(event);

        assertTrue(eventFileManager.eventExists("Test Event"));
    }

    @Test
    void testGetEvents() {
        eventFileManager.addEvent(event);

        Map<String, Event> events = eventFileManager.getEvents();

        assertTrue(events.containsKey("Test Event"));
        assertEquals(
                event.getTitle(),
                events.get("Test Event").getTitle()
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
                () -> eventFileManager.getEvent("Does Not Exist")
        );
    }

    @Test
    void testRemoveEvent() {
        eventFileManager.addEvent(event);

        assertTrue(eventFileManager.eventExists("Test Event"));

        eventFileManager.removeEvent("Test Event");

        assertFalse(eventFileManager.eventExists("Test Event"));
    }

    @Test
    void testRemoveNonexistentEventThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> eventFileManager.removeEvent("Does Not Exist")
        );
    }

    @Test
    void testEditEvent() {
        eventFileManager.addEvent(event);

        Event editedEvent = new Event(
                "Test Event",
                "Oslo",
                LocalDateTime.of(2026, 11, 1, 20, 0)
        );

        eventFileManager.editEvent("Test Event", editedEvent);

        Event result = eventFileManager.getEvent("Test Event");

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
                () -> eventFileManager.editEvent(
                        "Does Not Exist",
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

        assertTrue(reloadedManager.eventExists("Test Event"));

        Event reloadedEvent = reloadedManager.getEvent("Test Event");

        assertEquals("Test Event", reloadedEvent.getTitle());
        assertEquals("Trondheim", reloadedEvent.getPlace());
        assertEquals(
            LocalDateTime.of(2026, 10, 1, 18, 0),
            reloadedEvent.getTime()
        );

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
    void testNewManagerStartsEmpty() {
        Path file = tempDir.resolve("new-events.json");
        EventFileManager manager = new EventFileManager(file.toString());
        assertTrue(manager.getEvents().isEmpty());
    }

    @Test
    void testSuccesfullCopy() {
        eventFileManager.addEvent(event);
        EventFileManager copy = eventFileManager.copy();

        Event copiedEvent = copy.getEvent(event.getTitle());
        assertEquals(event.getTitle(), copiedEvent.getTitle());
        assertEquals(event.getPlace(), copiedEvent.getPlace());
        assertEquals(event.getTime(), copiedEvent.getTime());
        assertEquals(eventFileManager.getEvents().keySet(), copy.getEvents().keySet());
    }

    @Test
    void testLoadFromDirectoryReturnsEmptyMap() {
        EventFileManager manager = new EventFileManager(tempDir.toString());
        assertTrue(manager.getEvents().isEmpty());
    }

    @Test
    void testConstructorUsesEmptyMapWhenLoadingFails() throws IOException {
        Path file = tempDir.resolve("invalid.json");
        Files.writeString(file, "{invalid json");
        EventFileManager manager = new EventFileManager(file.toString());
        assertTrue(manager.getEvents().isEmpty());
    }

}
