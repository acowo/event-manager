package user;

/** UserSession works as a global variable for accessing which user is currently logged in. */
public class UserSession {
  private static User user;

  /**
   * Get the current user.
   *
   * @return A copy of the current user
   * @throws NullPointerException if User was not set previously
   */
  public static User getUser() {
    if (user == null) {
      throw new NullPointerException("User was not set");
    }
    return new User(user);
  }

  /**
   * Sets the current user.
   *
   * @param user User object to be set
   * @throws NullPointerException if user is null
   */
  public static void setUser(User user) {
    if (user == null) {
      throw new NullPointerException("user cannot be null");
    }

    UserSession.user = new User(user);
  }

  /** Sets the current user to null. */
  public static void logout() {
    UserSession.user = null;
  }
}
