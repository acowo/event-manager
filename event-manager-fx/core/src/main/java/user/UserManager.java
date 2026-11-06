package user;

import java.util.Map;

import event.EventManager;

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
   * Creates a copy of the given UserManager.
   *
   * @param userManager a already initialized UserManager
   */
  public UserManager(UserManager userManager) {
    this.users = userManager.users.copy();
  }

  /**
   * Logs in using user credentials.
   *
   * @param username the user's username credential
   * @param password the user's password credential
   * @return {@code true} if login was successful; {@code false} if credentials were incorrect
   */
  public boolean login(String username, String password) {
    return (users.checkPassword(username, password));
  }

  /**
   * Creates a new user with the username and password and logs in with the same user credentials.
   *
   * @param username the user's username credential
   * @param password the user's password credential
   * @return {@code true} if registration and login was successful; {@code false} if user with the
   *     same username credential already exists
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
   * @return {@code true} if the user was successfully edited; {@code false} if the user does not
   *     exist
   */
  public boolean editUser(String username, User user) {
    if (!users.userExists(username)) {
      return false;
    }
    users.editUser(username, user);
    // UserSession is no longer in use, but i keep it here in case something breaks
    //    if (UserSession.getUser().getUsername().equals(username)) {
    //      UserSession.setUser(user);
    //    }

    return true;
  }

  /**
   * 
   * @param username
   * @param eventManager
   * @return {@code true} if the user was successfully deleted; {@code false} if the username is not matching
   */
  public boolean deleteUser(String username, EventManager eventManager) {
    User user = UserSession.getUser();
    if (!username.equals(user.getUsername())) {
      return false;
    }
    Map<String, Boolean> eventMap = user.getEventIds();
    eventMap.entrySet().forEach(s -> {
        if (s.getValue()) {
            removeEvent(s.getKey());
            eventManager.removeEvent(s.getKey());
        }
    });
    users.removeUser(username);
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
   *Gets user based on a username.
   *
   * @param username the username of the user.
   * @return User object with the matching username
   */
  public User getUser(String username) {
    return this.users.getUser(username);
  }

  /**
   * Opts a user in on an event.
   *
   * @param eventId the eventId of the event to opt into.
   * @param username the username of the user to be opted in.
   * @return {@code true} if the user is successfully opted in;
   *        {@code false} if user is already opted in.
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
   * Adds an event to a user as administrator (used when the user creates the event).
   *
   * @param eventId  the event the user created
   * @param username the creator
   * @return {@code true} if the user successfully gets opted in as admin;
   *        {@code false} if the user is already attending the event.
   */
  public boolean optInAsAdmin(String eventId, String username) {
    User user = getUser(username);
    if (user.isAttendingEvent(eventId)) {
      return false;
    }
    user.addEventId(eventId, true);
    users.editUser(username, user);
    return true;
  }

  /**
   * Opts a user out of an event.
   *
   * @param eventId the eventId of the event to opt out of.
   * @param username the username of the user to be opted out.
   * @return {@code true} if the user is successfully opted out;
   *        {@code false} if user is not attending the event.
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
