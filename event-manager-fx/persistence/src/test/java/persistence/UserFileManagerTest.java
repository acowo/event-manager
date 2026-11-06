package persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import user.User;

/** Tests for {@link UserFileManager}. */
public class UserFileManagerTest {

  @TempDir Path tempDir;
  private User user1;
  private User user2;
  private User user3;
  private String username1 = "test username1";
  private String username2 = "test username2";
  private String username3 = "test username3";
  private String password1 = "test password1";
  private String password2 = "test password2";
  private String password3 = "test password3";
  private UserFileManager userFileManager;

  @BeforeEach
  void setUp() {

    userFileManager = new UserFileManager(tempDir.resolve("userdata.json").toString());

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
    assertThrows(IllegalArgumentException.class, () -> userFileManager.getUser("Does Not Exist"));
  }

  @Test
  void testEditNonexistentUserThrowsException() {
    User updatedUser = new User("Does Not Exist", "password");

    assertThrows(
        IllegalArgumentException.class,
        () -> userFileManager.editUser("Does Not Exist", updatedUser));
  }

  @Test
  void testCheckPasswordReturnsTrueForCorrectPassword() {
    userFileManager.addUser(user1);

    assertTrue(userFileManager.checkPassword(username1, password1));
  }

  @Test
  void testCheckPasswordReturnsFalseForWrongPassword() {
    userFileManager.addUser(user1);

    assertFalse(userFileManager.checkPassword(username1, "wrong password"));
  }

  @Test
  void testCheckPasswordReturnsFalseForNonexistentUser() {
    assertFalse(userFileManager.checkPassword("Does Not Exist", "password"));
  }

  @Test
  void testNullPasswordThrowsException() {
    assertThrows(
        IllegalArgumentException.class, () -> userFileManager.checkPassword(username1, null));
  }

  @Test
  void testUserPersistsToFile() {
    userFileManager.addUser(user1);

    UserFileManager reloadedManager =
        new UserFileManager(tempDir.resolve("userdata.json").toString());

    assertTrue(reloadedManager.userExists(username1));

    User reloadedUser = reloadedManager.getUser(username1);

    assertEquals(username1, reloadedUser.getUsername());
    assertEquals(password1, reloadedUser.getPassword());
  }

  @Test
  void testFileCreation() {
    Path file = tempDir.resolve("users.json");
    UserFileManager manager = new UserFileManager(file.toString());

    assertTrue(manager.getUsers().isEmpty());
    assertTrue(Files.exists(file));
  }

  @Test
  void testFileCreationFailsWhenParentDirectoryIsMissing() {
    Path file = tempDir.resolve("missing-folder/users.json");
    UserFileManager manager = new UserFileManager(file.toString());

    RuntimeException exception = assertThrows(RuntimeException.class, manager::getUsers);

    assertEquals("Could not create user file", exception.getMessage());
    assertInstanceOf(IOException.class, exception.getCause());
  }

  @Test
  void testSaveUsersIfNull() {
    assertThrows(NullPointerException.class, () -> userFileManager.saveUsers(null));
  }

  @Test
  void testSuccesfullCopy() {
    userFileManager.addUser(user1);
    UserFileManager copy = userFileManager.copy();

    assertEquals(username1, copy.getUser(username1).getUsername());
    assertEquals(password1, copy.getUser(username1).getPassword());
    assertEquals(userFileManager.getUsers().keySet(), copy.getUsers().keySet());
  }

  @Test
  void testConstructorWithDefaultFilePath() {
    assertDoesNotThrow(() -> new UserFileManager());
  }

  @Test
  void testRemoveEvent() {

    user1.addEventId("test Event", true);
    user2.addEventId("test Event", true);

    userFileManager.addUser(user1);
    userFileManager.addUser(user2);

    userFileManager.removeEvent("test Event");
    assertFalse(userFileManager.getUser(username1).isAttendingEvent("test Event"));
    assertFalse(userFileManager.getUser(username2).isAttendingEvent("test Event"));
    assertEquals(2, userFileManager.getUsers().size());
  }

  @Test
  void testSaveUsersCreatesFile() {
    Path file = tempDir.resolve("new-users.json");
    UserFileManager manager = new UserFileManager(file.toString());

    manager.saveUsers(Map.of(username1, user1));

    assertTrue(Files.exists(file));
    assertEquals(password1, manager.getUser(username1).getPassword());
  }

  @Test
  void testSaveUsersThrowsWhenPathIsDirectory() {
    UserFileManager manager = new UserFileManager(tempDir.toString());

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> manager.saveUsers(Map.of()));

    assertEquals("Could not write to file", exception.getMessage());
    assertInstanceOf(IOException.class, exception.getCause());
  }

  @Test
  void testGetUsersThrowsWhenPathIsDirectory() {
    UserFileManager manager = new UserFileManager(tempDir.toString());

    RuntimeException exception = assertThrows(RuntimeException.class, manager::getUsers);

    assertEquals("An error has occurred", exception.getMessage());
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
