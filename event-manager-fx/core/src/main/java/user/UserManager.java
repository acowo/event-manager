
package user;

/**
 * UserManager implements domain logic for managing user aspects.
 *
 * <ul>
 *   <li>Login
 *   <li>Registration
 *   <li>Communicating with persistence
 * </ul>
 */
public class UserManager {

  private final UserRepository users;

  /**
   * Creates a UserManager object from a UserRepository.
   *
   * @param users An implementation of a UserRepository
   */
  public UserManager(UserRepository users) {
    this.users = users.copy();
  }

  /**
   * Logs in using user credentials.
   *
   * @param username the user's username credential
   * @param password the user's password credential
   * @return {@code true} if login was successful;
   *     {@code false} if credentials were incorrect
   */
  public boolean login(String username, String password) {
    return (users.checkPassword(username, password));
  }

  /**
   * Creates a new user with the given username and password.
   *
   * @param username the user's username
   * @param password the user's password
   * @return {@code true} if registration was successful;
   *     {@code false} if the username already exists
   * @throws NullPointerException if username is null
   * @throws IllegalArgumentException if username is blank
   */
  public boolean register(String username, String password) {
    if (users.userExists(username)) {
      return false;
    }

    User user = new User(username, password);
    users.addUser(user);
    return true;
  }

  /**
   * Edits a user by giving a username to update and an updated User-object.
   *
   * @param username the user to be changed
   * @param user the updated user object
   * @return {@code true} if the user was successfully edited;
   *     {@code false} if the user does not exist
   */
  public boolean editUser(String username, User user) {
    if (!users.userExists(username)) {
      return false;
    }
    users.editUser(username, user);
    return true;
  }

  /**
   * Removes an event from all users.
   *
   * @param eventId the Event UUID to be removed
   */
  public void removeEvent(String eventId) {
    users.removeEvent(eventId);
  }

  /**
   * Retrieves a user by username.
   *
   * @param username the username of the user
   * @return the user with the matching username
   */
  public User getUser(String username) {
    return this.users.getUser(username);
  }

  /**
   * Adds a user to an event if they are not already attending.
   *
   * @param eventId the ID of the event
   * @param username the username of the user
   * @return {@code true} if the user was added;
   *     {@code false} if already attending
   */
  public boolean optIn(String eventId, String username) {
    User user = getUser(username);
    if (user.isAttendingEvent(eventId)) {
      return false;
    }
    user.addEventId(eventId, false);
    users.editUser(username, user);
    return true;
  }

  /**
   * Removes a user from an event if they are attending.
   *
   * @param eventId the ID of the event
   * @param username the username of the user
   * @return {@code true} if the user was removed;
   *     {@code false} if not attending
   */
  public boolean optOut(String eventId, String username) {
    User user = getUser(username);
    if (!user.isAttendingEvent(eventId)) {
      return false;
    }
    user.removeEventId(eventId);
    users.editUser(username, user);
    return true;
  }
}
