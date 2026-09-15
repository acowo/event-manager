package persistence;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import user.User;

public class UserFileManagerTest {

    private User user1;
    private User user2;
    private User user3;
    private String username1 = "test username1";
    private String username2 = "test username2";
    private String username3 = "test username3";
    private String password1 = "test password1";
    private String password2 = "test password2";
    private String password3 = "test password3";

    private void clearTestUsers() {
        if (UserFileManager.userExists(username1)) {
            UserFileManager.removeUser(username1);
        }
        if (UserFileManager.userExists(username2)) {
            UserFileManager.removeUser(username2);
        }
        if (UserFileManager.userExists(username3)) {
            UserFileManager.removeUser(username3);
        }
    }

    @BeforeEach
    void setUp() {
        user1 = new User(username1, password1);
        user2 = new User(username2, password2);
        user3 = new User(username3, password3);

        clearTestUsers();
    }

    @AfterEach
    void tearDown() {
        clearTestUsers();
    }

    @Test
    void testGetUsers() {
        UserFileManager.addUser(user1);
        UserFileManager.addUser(user2);
        UserFileManager.addUser(user3);

        Map<String, User> users = UserFileManager.getUsers();

        assertEquals(3, users.size());

        assertTrue(users.containsKey(username1));
        assertTrue(users.containsKey(username2));
        assertTrue(users.containsKey(username3));
    }

    @Test
    void testGetUser() {
        UserFileManager.addUser(user1);

        User userResult = UserFileManager.getUser(username1);

        assertEquals(userResult.getUsername(), username1);
        assertEquals(userResult.getPassword(), password1);
    }

    @Test
    void testUserExists() {
        assertFalse(UserFileManager.userExists(username2));

        UserFileManager.addUser(user2);

        assertTrue(UserFileManager.userExists(username2));
    }

    @Test
    void testDuplicateUserThrowsException() {
        UserFileManager.addUser(user3);

        assertThrows(IllegalArgumentException.class, () -> UserFileManager.addUser(user3));
    }

    @Test
    void testRemoveUser() {
        UserFileManager.addUser(user1);

        UserFileManager.removeUser(username1);

        assertFalse(UserFileManager.userExists(username1));
    }

    @Test
    void testRemoveNonexistentUserThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> UserFileManager.removeUser(username3));
    }

    @Test
    void testEditUser() {
        UserFileManager.addUser(user1);

        User updatedUser = new User(username1, "new password");
        UserFileManager.editUser(username1, updatedUser);

        User userResult = UserFileManager.getUser(username1);

        assertEquals(userResult.getUsername(), username1);
        assertEquals(userResult.getPassword(), "new password");
    }
}