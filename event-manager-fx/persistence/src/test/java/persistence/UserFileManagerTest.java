package persistence;

import java.util.Map;

import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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

    @TempDir
    Path tempDir;

    private UserFileManager userFileManager;

    @BeforeEach
    void setUp() {

        userFileManager = new UserFileManager(
            tempDir.resolve("userdata.json").toString()
        );

        user1 = new User(username1, password1);
        user2 = new User(username2, password2);
        user3 = new User(username3, password3);
    }


    @Test
    void testGetUser() {
        userFileManager.addUser(user1);

        User userResult = userFileManager.getUser(username1);

        assertEquals(userResult.getUsername(), username1);
        assertEquals(userResult.getPassword(), password1);
    }

    @Test
    void testUserExists() {
        assertFalse(userFileManager.userExists(username2));

        userFileManager.addUser(user2);

        assertTrue(userFileManager.userExists(username2));
    }

    @Test
    void testDuplicateUserThrowsException() {
        userFileManager.addUser(user3);

        assertThrows(IllegalArgumentException.class, () -> userFileManager.addUser(user3));
    }

    @Test
    void testRemoveUser() {
        userFileManager.addUser(user1);

        userFileManager.removeUser(username1);

        assertFalse(userFileManager.userExists(username1));
    }

    @Test
    void testRemoveNonexistentUserThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userFileManager.removeUser(username3));
    }

    @Test
    void testEditUser() {
        userFileManager.addUser(user1);

        User updatedUser = new User(username1, "new password");
        userFileManager.editUser(username1, updatedUser);

        User userResult = userFileManager.getUser(username1);

        assertEquals(userResult.getUsername(), username1);
        assertEquals(userResult.getPassword(), "new password");
    }

    @Test
    void testGetNonexistentUserThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userFileManager.getUser("Does Not Exist")
        );
    }

    @Test
    void testEditNonexistentUserThrowsException() {
        User updatedUser = new User(
                "Does Not Exist",
                "password"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> userFileManager.editUser(
                        "Does Not Exist",
                        updatedUser
                )
        );
    }

    @Test
    void testCheckPasswordReturnsTrueForCorrectPassword() {
        userFileManager.addUser(user1);

        assertTrue(
                userFileManager.check_password(
                        username1,
                        password1
                )
        );
    }

    @Test
    void testCheckPasswordReturnsFalseForWrongPassword() {
        userFileManager.addUser(user1);

        assertFalse(
                userFileManager.check_password(
                        username1,
                        "wrong password"
                )
        );
    }

    @Test
    void testCheckPasswordReturnsFalseForNonexistentUser() {
        assertFalse(
                userFileManager.check_password(
                        "Does Not Exist",
                        "password"
                )
        );
    }

    @Test
    void testNullPasswordThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userFileManager.check_password(
                        username1,
                        null
                )
        );
    }

    @Test
    void testUserPersistsToFile() {
        userFileManager.addUser(user1);

        UserFileManager reloadedManager = new UserFileManager(
                tempDir.resolve("userdata.json").toString()
        );

        assertTrue(reloadedManager.userExists(username1));

        User reloadedUser = reloadedManager.getUser(username1);

        assertEquals(username1, reloadedUser.getUsername());
        assertEquals(password1, reloadedUser.getPassword());
    }
    
}