package user;

public class UserSession {
    public static User user;


    public static void setUser(String username, String password) {
        user = new User(username, password);
    }

    public static User getUser() {
        return UserSession.user;
    }

}