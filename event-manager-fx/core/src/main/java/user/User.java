package user;

import java.util.ArrayList;
import java.util.List;

/** A data class that represents a user within the system. */
public final class User {
  /** List of all identifiers for Events this User is attending. */
  private List<String> eventIdList = new ArrayList<>();

  private String username;
  private String password;

  /**
   * Creates a new User object with an empty attending events list.
   *
   * @param username identifier of User
   * @param password password of User
   * @throws NullPointerException if username is null
   * @throws IllegalArgumentException if username is blank
   */
  public User(String username, String password) {
    setUsername(username);
    setPassword(password);
  }

  /**
   * Creates a copy of an existing User.
   *
   * @param user User object to be copied
   */
  public User(User user) {
    setUsername(user.getUsername());
    setPassword(user.getPassword());
    this.eventIdList = new ArrayList<>(user.getEventIdList());
  }

  /**
   * Gets username of this User object.
   *
   * @return username of this User
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * Sets username of this User.
   *
   * @param username - username to be set
   * @throws NullPointerException if username is null
   * @throws IllegalArgumentException if username is blank
   */
  public void setUsername(String username) {

    if (username == null) {
      throw new NullPointerException("username cannot be null");
    }
    if (username.isBlank()) {
      throw new IllegalArgumentException("username cannot be blank");
    }
    this.username = username;
  }

  /**
   * Gets password of this User object.
   *
   * @return password of this User
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Sets password of this User.
   *
   * @param password Password to be set
   * @throws NullPointerException if password is null
   * @throws IllegalArgumentException if password is blank
   */
  public void setPassword(String password) {
    if (password == null) {
      throw new NullPointerException("password cannot be null");
    }
    if (password.isBlank()) {
      throw new IllegalArgumentException("password cannot be blank");
    }

    this.password = password;
  }

  /**
   * Adds event to this users attending events using event identifier.
   *
   * @param eventId identifier of Event to be added
   * @throws NullPointerException if eventId is null
   * @throws IllegalArgumentException if eventId is blank
   */
  public void addEventId(String eventId) {
    if (eventId == null) {
      throw new NullPointerException("eventId cannot be null");
    }
    if (eventId.isBlank()) {
      throw new IllegalArgumentException("eventId cannot be blank");
    }
    this.eventIdList.add(eventId);
  }

  /**
   * Removes event from user's attending events using the event identifier.
   *
   * @param eventId identifier of event to remove
   * @throws NullPointerException if eventId is null
   * @throws IllegalArgumentException if eventId is blank
   * @throws IllegalStateException if user was not attending event previously
   */
  public void removeEventId(String eventId) {
    if (eventId == null) {
      throw new NullPointerException("eventId cannot be null");
    }
    if (eventId.isBlank()) {
      throw new IllegalArgumentException("eventId cannot be blank");
    }

    if (!this.eventIdList.contains(eventId)) {
      throw new IllegalStateException("eventId was not in eventIdList");
    }
    this.eventIdList.remove(eventId);
  }

  /**
   * Checks if user is attending event using event identifier.
   *
   * @param eventId identifier of event to check
   * @return {@code true} if user is attending event; {@code false} if user is not attending event
   * @throws NullPointerException if eventId is null
   * @throws IllegalArgumentException if eventId is blank
   */
  public boolean isAttendingEvent(String eventId) {
    if (eventId == null) {
      throw new NullPointerException("eventId cannot be null");
    }
    if (eventId.isBlank()) {
      throw new IllegalArgumentException("eventId cannot be blank");
    }

    return this.eventIdList.contains(eventId);
  }

  /**
   * Gets all events user is attending.
   *
   * @return unmodifiable list of identifiers of all events user is attending
   */
  public List<String> getEventIdList() {
    return List.copyOf(this.eventIdList);
  }
}
