package user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests user session management, including setting and retrieving the current user, handling
 * invalid states, and logging out.
 */

public class UserSessionTest {

  @AfterEach
  void tearDown() {
    UserSession.logout();
  }

    void isSameUser(User user1, User user2) {
        assertEquals(user1.getUsername(), user2.getUsername());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getEventIds(), user2.getEventIds());
    }

  @Test
  void testSetAndGetUser() {
    User user = new User("testUser", "password");

    UserSession.setUser(user);

    isSameUser(user, UserSession.getUser());
  }

  @Test
  void testSetNullUserThrowsException() {
    assertThrows(NullPointerException.class, () -> UserSession.setUser(null));
  }

  @Test
  void testGetUserWhenLoggedOutThrowsException() {
    UserSession.logout();

    assertThrows(NullPointerException.class, UserSession::getUser);
  }

  @Test
  void testLogout() {
    User user = new User("testUser", "password");
    UserSession.setUser(user);

    UserSession.logout();

    assertThrows(NullPointerException.class, UserSession::getUser);
  }
}
