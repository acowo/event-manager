package user;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class UserSessionTest {

    @AfterEach
    void tearDown() {
        UserSession.logout();
    }

    @Test
    void testSetAndGetUser() {
        User user = new User("testUser", "password");

        UserSession.setUser(user);

        assertSame(user, UserSession.getUser());
    }

    @Test
    void testSetNullUserThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> UserSession.setUser(null)
        );
    }

    @Test
    void testGetUserWhenLoggedOutThrowsException() {
        UserSession.logout();

        assertThrows(
                NullPointerException.class,
                UserSession::getUser
        );
    }

    @Test
    void testLogout() {
        User user = new User("testUser", "password");
        UserSession.setUser(user);

        UserSession.logout();

        assertThrows(
                NullPointerException.class,
                UserSession::getUser
        );
    }
}