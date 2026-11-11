package ui.auth;

/**
 * Holds the session state of the currently logged-in user on the client.
 */
public class ClientSession {
  private static String authToken;

  /**
   * Gets the authToken of the current user.
   *
   * @return the authentication token, or {@code null} if none has been set
   */
  public static String getAuthToken() {
    return authToken;
  }

  /**
   * Sets the authToken of the current user.
   *
   * @param authToken the authentication token
   */
  public static void setAuthToken(String authToken) {
    ClientSession.authToken = authToken;
  }

  /**
   * Clears the authToken of the current user.
   */
  public static void clearAuthToken() {
    authToken = "";
  }
}
