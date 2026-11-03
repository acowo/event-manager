package user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests user creation, validation, and management of usernames, passwords, and event IDs. */
public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "testPassword");
    }

    @Test
    void testCreateUser() {
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertTrue(user.getEventIdList().isEmpty());
    }

    @Test
    void testSetUsername() {
        user.setUsername("newUsername");

        assertEquals("newUsername", user.getUsername());
    }

    @Test
    void testNullUsernameThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> user.setUsername(null)
        );
    }

    @Test
    void testBlankUsernameThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> user.setUsername(" ")
        );
    }

    @Test
    void testSetPassword() {
        user.setPassword("newPassword");

        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void testNullPasswordThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> user.setPassword(null)
        );
    }

    @Test
    void testBlankPasswordThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> user.setPassword(" ")
        );
    }

    @Test
    void testAddEventId() {
        user.addEventId("event1");

        assertTrue(user.isAttendingEvent("event1"));
        assertEquals(List.of("event1"), user.getEventIdList());
    }

    @Test
    void testAddNullEventIdThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> user.addEventId(null)
        );
    }

    @Test
    void testAddBlankEventIdThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> user.addEventId(" ")
        );
    }

    @Test
    void testRemoveEventId() {
        user.addEventId("event1");

        user.removeEventId("event1");

        assertFalse(user.isAttendingEvent("event1"));
    }

    @Test
    void testRemoveNullEventIdThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> user.removeEventId(null)
        );
    }

    @Test
    void testRemoveBlankEventIdThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> user.removeEventId(" ")
        );
    }

    @Test
    void testRemoveNonexistentEventIdThrowsException() {
        assertThrows(
                IllegalStateException.class,
                () -> user.removeEventId("event1")
        );
    }

    @Test
    void testIsAttendingEventReturnsTrueWhenAttending() {
        user.addEventId("event1");

        assertTrue(user.isAttendingEvent("event1"));
    }

    @Test
    void testIsAttendingEventReturnsFalseWhenNotAttending() {
        assertFalse(user.isAttendingEvent("event1"));
    }

    @Test
    void testIsAttendingEventWithNullThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> user.isAttendingEvent(null)
        );
    }

    @Test
    void testIsAttendingEventWithBlankIdThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> user.isAttendingEvent(" ")
        );
    }

    @Test
    void testGetEventIdListReturnsCopy() {
        user.addEventId("event1");

        List<String> events = user.getEventIdList();

        assertThrows(
                UnsupportedOperationException.class,
                () -> events.add("event2")
        );

        assertEquals(List.of("event1"), user.getEventIdList());
    }
}