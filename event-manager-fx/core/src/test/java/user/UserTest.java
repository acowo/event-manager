package user;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests user creation, validation, and management of usernames, passwords, and event IDs. */
public class UserTest {

  private User user;

  /**
   * Sets up a test environment before each test.
   */
  @BeforeEach
  void setUp() {
    user = new User("testUser", "testPassword");
  }

  /**
   * Test that a User can be created with valid parameters.
   */
  @Test
  void testCreateUser() {
    assertEquals("testUser", user.getUsername());
    assertEquals("testPassword", user.getPassword());
    assertTrue(user.getEventIds().isEmpty());
  }

  /**
   * Test that creating a User with a null or blank username throws an exception.
   */
  @Test
  void testSetUsername() {
    user.setUsername("newUsername");

    assertEquals("newUsername", user.getUsername());
  }

  /**
   * Test that setting a User's username to null throws an exception.
   */
  @Test
  void testNullUsernameThrowsException() {
    assertThrows(NullPointerException.class, () -> user.setUsername(null));
  }

  /**
   * Test that setting a User's username to a blank string throws an exception.
   */
  @Test
  void testBlankUsernameThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> user.setUsername(" "));
  }

  /**
   * Test that setting a User's password to null throws an exception.
   */
  @Test
  void testSetPassword() {
    user.setPassword("newPassword");

    assertEquals("newPassword", user.getPassword());
  }

  /**
   * Test that setting a User's password to null throws an exception.
     */
  @Test
  void testNullPasswordThrowsException() {
    assertThrows(NullPointerException.class, () -> user.setPassword(null));
  }

  /**
   * Test that setting a User's password to a blank string throws an exception.
   */
  @Test
  void testBlankPasswordThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> user.setPassword(" "));
  }

  /**
   * Test adding an event ID to the User's event list.
   */
  @Test
  void testAddEventId() {
    user.addEventId("event1", true);

    assertTrue(user.isAttendingEvent("event1"));
    assertEquals(Map.of("event1", true), user.getEventIds());
  }

  /**
   * Test that adding a null event ID to the User's event list throws an exception.
   */
  @Test
  void testAddNullEventIdThrowsException() {
    assertThrows(
            NullPointerException.class,
            () -> user.addEventId(null, false)
    );
  }

  /**
   * Test that adding a blank event ID to the User's event list throws an exception.
   */
  @Test
  void testAddBlankEventIdThrowsException() {
    assertThrows(
            IllegalArgumentException.class,
            () -> user.addEventId(" ", false)
    );
  }

  /**
   * Test removing an event ID from the User's event list.
   */
  @Test
  void testRemoveEventId() {
    user.addEventId("event1", true);

    user.removeEventId("event1");

    assertFalse(user.isAttendingEvent("event1"));
  }

  /**
   * Test that removing a null event ID from the User's event list throws an exception.
   */
  @Test
  void testRemoveNullEventIdThrowsException() {
    assertThrows(NullPointerException.class, () -> user.removeEventId(null));
  }

  /**
   * Test that removing a blank event ID from the User's event list throws an exception.
   */
  @Test
  void testRemoveBlankEventIdThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> user.removeEventId(" "));
  }

  /**
   * Test that removing a non-existent event ID from the User's event list throws an exception.
   */
  @Test
  void testRemoveNonexistentEventIdThrowsException() {
    assertThrows(IllegalStateException.class, () -> user.removeEventId("event1"));
  }

  /**
   * Test that isAttendingEvent returns true when the user is attending the event.
   */
  @Test
  void testIsAttendingEventReturnsTrueWhenAttending() {
    user.addEventId("event1", true);

    assertTrue(user.isAttendingEvent("event1"));
  }

  /**
   * Test that isAttendingEvent returns false when the user is not attending the event.
   */
  @Test
  void testIsAttendingEventReturnsFalseWhenNotAttending() {
    assertFalse(user.isAttendingEvent("event1"));
  }

  /**
   * Test that isAttendingEvent throws an exception when the event ID is null.
   */
  @Test
  void testIsAttendingEventWithNullThrowsException() {
    assertThrows(NullPointerException.class, () -> user.isAttendingEvent(null));
  }

  /**
   * Test that isAttendingEvent throws an exception when the event ID is blank.
   */
  @Test
  void testIsAttendingEventWithBlankIdThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> user.isAttendingEvent(" "));
  }

  /**
   * Test that getEventIds returns a copy of the event IDs map, not the original.
   */
  @Test
  void testGetEventIdListReturnsCopy() {
    user.addEventId("event1", false);

    Map<String, Boolean> events = user.getEventIds();

    assertThrows(
            UnsupportedOperationException.class,
            () -> events.put("event2", false)
    );

    assertEquals(Map.of("event1", false), user.getEventIds());
  }
}
