
package user;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Tests the login and registration behavior of {@link UserManager},
 * including successful operations and failure cases.
 */
public class UserManagerTest {

  /**
   * Helper method to assert that two User objects are the same based on their properties.
   *
   * @param user1 the first User object.
   * @param user2 the second User object.
   */
  void isSameUser(User user1, User user2) {
    assertEquals(user1.getUsername(), user2.getUsername());
    assertEquals(user1.getPassword(), user2.getPassword());
    assertEquals(user1.getEventIds(), user2.getEventIds());
  }

  /**
   * Test that the login method works correctly when the user provides valid credentials.
   */
  @Test
  void testLoginSuccessful() {
    UserRepository repository = new FakeUserRepository();
    User user = new User("testUser", "password");
    repository.addUser(user);

    UserManager manager = new UserManager(repository);

    assertTrue(manager.login("testUser", "password"));
  }

  /**
   * Test that the login method fails when the user provides an incorrect password.
   */
  @Test
  void testLoginFailsWithIncorrectPassword() {
    UserRepository repository = new FakeUserRepository();
    repository.addUser(new User("testUser", "password"));

    UserManager manager = new UserManager(repository);

    assertFalse(manager.login("testUser", "wrongPassword"));
  }

  /**
   * Test that the login method fails when the user does not exist in the repository.
   */
  @Test
  void testRegisterSuccessful() {
    UserRepository repository = new FakeUserRepository();
    UserManager manager = new UserManager(repository);

    assertTrue(manager.register("newUser", "password"));

    assertTrue(repository.userExists("newUser"));
    isSameUser(new User("newUser", "password"), repository.getUser("newUser"));
  }

  /**
   * Test that the register method fails when trying to register a user that already exists.
   */
  @Test
  void testRegisterFailsWhenUserAlreadyExists() {
    UserRepository repository = new FakeUserRepository();
    repository.addUser(new User("existingUser", "password"));

    UserManager manager = new UserManager(repository);

    assertFalse(manager.register("existingUser", "newPassword"));
  }

  /**
   * FakeUserRepository is a simple implementation of the UserRepository.
   */
  private static class FakeUserRepository implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public boolean checkPassword(String username, String password) {
      return users.containsKey(username)
          && users.get(username).getPassword().equals(password);
    }

    @Override
    public boolean userExists(String username) {
      return users.containsKey(username);
    }

    @Override
    public void addUser(User user) {
      users.put(user.getUsername(), user);
    }

    @Override
    public User getUser(String username) {
      return users.get(username);
    }

    @Override
    public void editUser(String username, User user) {
      users.put(username, user);
    }

    @Override
    public void removeEvent(String title) {
      users
          .values()
          .forEach(u -> {
            if (u.isAttendingEvent(title)) {
              u.removeEventId(title);
            }
          });
    }

    @Override
    public FakeUserRepository copy() {
      return this;
    }

    @Override
    public void removeUser(String username) {
      return;
    }
  }
}
