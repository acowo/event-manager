package user;

public class UserSession {
    private static User user;

    public static void setUser(User user) {
        if (user == null) {
            throw new NullPointerException("user cannot be null");
        }

        UserSession.user = new User(user);
    }

    public static User getUser() {
        if (user == null) {
            throw new NullPointerException("User was not set");
        }
        return new User(user);
    }

    public static void logout() {
        UserSession.user = null;
    }
}
