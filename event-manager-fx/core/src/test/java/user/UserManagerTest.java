package user;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UserManagerTest {

    private static class FakeUserRepository implements UserRepository {

        private final Map<String, User> users = new HashMap<>();

        @Override
        public boolean check_password(String username, String password) {
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
    }

    @Test
    void testLoginSuccessful() {
        FakeUserRepository repository = new FakeUserRepository();
        User user = new User("testUser", "password");
        repository.addUser(user);

        UserManager manager = new UserManager(repository);

        assertTrue(manager.login("testUser", "password"));
        assertSame(user, UserSession.getUser());
    }

    @Test
    void testLoginFailsWithIncorrectPassword() {
        FakeUserRepository repository = new FakeUserRepository();
        repository.addUser(new User("testUser", "password"));

        UserManager manager = new UserManager(repository);

        assertFalse(manager.login("testUser", "wrongPassword"));
    }

    @Test
    void testRegisterSuccessful() {
        FakeUserRepository repository = new FakeUserRepository();
        UserManager manager = new UserManager(repository);

        assertTrue(manager.register("newUser", "password"));

        assertTrue(repository.userExists("newUser"));
        assertSame(repository.getUser("newUser"), UserSession.getUser());
    }

    @Test
    void testRegisterFailsWhenUserAlreadyExists() {
        FakeUserRepository repository = new FakeUserRepository();
        repository.addUser(new User("existingUser", "password"));

        UserManager manager = new UserManager(repository);

        assertFalse(manager.register("existingUser", "newPassword"));
    }
}