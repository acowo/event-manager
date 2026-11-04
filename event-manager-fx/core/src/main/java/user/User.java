package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String,Boolean> eventIds = new HashMap<>();
    private String username;
    private String password;


    /**
     * Creates a new User object with an empty attending events list 
     * @param username identifier of User
     * @param password password of User 
     * @throws NullPointerException if username is null
     * @throws IllegalArgumentException if username is blank 
     */
    public User(String username, String password){
        setUsername(username);
        setPassword(password);
    }

    /**
     * Creates a copy of an existing User 
     * @param user User object to be copied 
     */
    public User(User user) {
      setUsername(user.getUsername());
      setPassword(user.getPassword());
      this.eventIds = new HashMap<>(user.getEventIds());
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
     * Adds event to this users attending events using event identifier
     * @param eventId identifier of Event to be added 
     * @throws NullPointerException if eventId is null
     * @throws IllegalArgumentException if eventId is blank
     */
    public void addEventId(String eventId, Boolean admin){
      if (eventId == null){
        throw new NullPointerException("eventId cannot be null");
      }
      if (eventId.isBlank()){
        throw new IllegalArgumentException("eventId cannot be blank");
      }
      this.eventIds.put(eventId, admin);
    }

    /**
     * Removes event from user's attending events using the event identifier 
     * @param eventId identifier of event to remove
     * @throws NullPointerException if eventId is null
     * @throws IllegalArgumentException if eventId is blank
     * @throws IllegalStateException if user was not attending event previously
     */
    public void removeEventId(String eventId){
      if (eventId == null){
        throw new NullPointerException("eventId cannot be null");
      }
      if (eventId.isBlank()){
        throw new IllegalArgumentException("eventId cannot be blank");
      }

      if (this.eventIds == null || !this.eventIds.containsKey(eventId)){
        throw new IllegalStateException("eventId was not in eventIds");
      }
      this.eventIds.remove(eventId);

    }
    if (eventId.isBlank()) {
      throw new IllegalArgumentException("eventId cannot be blank");
    }
    this.eventIdList.add(eventId);
  }

    /**
     * Checks if user is attending event using event identifier
     * @param eventId identifier of event to check 
     * @return {@code true} if user is attending event;
     *  {@code false} if user is not attending event 
     * @throws NullPointerException if eventId is null
     * @throws IllegalArgumentException if eventId is blank
     */
    public boolean isAttendingEvent(String eventId){
      if (eventId == null){
        throw new NullPointerException("eventId cannot be null");
      }
      if (eventId.isBlank()){
        throw new IllegalArgumentException("eventId cannot be blank");
      }
      if (this.eventIds == null) {return false;}
      return this.eventIds.containsKey(eventId);
    }

    /**
     * Checks if user is administrating event using event identifier
     * @param eventId identifier of event to check
     * @throws NullPointerException if eventId is null
     * @throws IllegalArgumentException if eventId is blank
     * @return {@code true} if user is administrating event; {@code false} if user isn't attending event or is attending but isn't administrating
     */
    public boolean isAdministratingEvent(String eventId){
      if (eventId == null){
        throw new NullPointerException("eventId cannot be null");
      }
      if (eventId.isBlank()){
        throw new IllegalArgumentException("eventId cannot be blank");
      }
      if (!isAttendingEvent(eventId)) {return false;}

      return this.eventIds.get(eventId);
    }

    /**
     * 
     * @return unmodifiable map of identifiers of all events user is attending, with Boolean value of admin status for event
     */
    public Map<String, Boolean> getEventIds(){
      if (this.eventIds == null) {return Map.of();}
      return Map.copyOf(this.eventIds);

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
