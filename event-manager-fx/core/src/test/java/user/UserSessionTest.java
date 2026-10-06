package user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class UserSessionTest {

    @AfterEach
    void tearDown() {
        UserSession.logout();
    }

    void isSameUser(User user1, User user2) {
        assertTrue(user1.getUsername().equals(user2.getUsername()));
        assertTrue(user1.getPassword().equals(user2.getPassword()));
        assertTrue(user1.getEventIdList().equals(user2.getEventIdList()));
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