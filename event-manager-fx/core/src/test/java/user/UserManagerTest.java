
package user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the login and registration behavior of {@link UserManager}, including successful operations
 * and failure cases.
 */
public class UserManagerTest {


  void isSameUser(User user1, User user2) {
    assertEquals(user1.getUsername(), user2.getUsername());
    assertEquals(user1.getPassword(), user2.getPassword());
    assertEquals(user1.getEventIds(), user2.getEventIds());
  }

  @Test
  void testLoginSuccessful() {
    UserRepository repository = new FakeUserRepository();
    User user = new User("testUser", "password");
    repository.addUser(user);

    UserManager manager = new UserManager(repository);

    assertTrue(manager.login("testUser", "password"));
    isSameUser(user, UserSession.getUser());
  }

  @Test
  void testLoginFailsWithIncorrectPassword() {
    UserRepository repository = new FakeUserRepository();
    repository.addUser(new User("testUser", "password"));

    UserManager manager = new UserManager(repository);

    assertFalse(manager.login("testUser", "wrongPassword"));
  }

  @Test
  void testRegisterSuccessful() {
    UserRepository repository = new FakeUserRepository();
    UserManager manager = new UserManager(repository);

    assertTrue(manager.register("newUser", "password"));

    assertTrue(repository.userExists("newUser"));
    isSameUser(repository.getUser("newUser"), UserSession.getUser());
  }

  @Test
  void testRegisterFailsWhenUserAlreadyExists() {
    UserRepository repository = new FakeUserRepository();
    repository.addUser(new User("existingUser", "password"));

    UserManager manager = new UserManager(repository);

    assertFalse(manager.register("existingUser", "newPassword"));
  }

  @AfterEach
  void tearDown() {
    UserSession.logout();
  }

  private static class FakeUserRepository implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public boolean checkPassword(String username, String password) {
      return users.containsKey(username) && users.get(username).getPassword().equals(password);
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
  }
}
