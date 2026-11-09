package springboot.restserver.service;

import dto.EventDto;
import event.Event;
import event.EventManager;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import user.UserManager;


/**
 * Service for managing events and the users associated with them.
 */
@Service
public class EventManagerService {

  private final EventManager eventManager;
  private final UserManager userManager;

  /**
   * Creates a service backed by the given managers.
   *
   * @param eventManager the manager that stores and retrieves events
   * @param userManager  the manager that stores and retrieves users
   */
  public EventManagerService(EventManager eventManager, UserManager userManager) {
    this.eventManager = eventManager;
    this.userManager = userManager;
  }

  // ==================== EVENT LOGIC ====================

  /**
   * Gets a list of all {@code Event} objects the manager has.
   *
   * @return a list of all the events in the eventManager converted to {@link EventDto} records.
   */
  public synchronized List<EventDto> getAll() {
    return eventManager.getEvents().values()
        .stream().sorted(Comparator.comparing(Event::getTime)).map(EventDto::from)
        .toList();
  }

  /**
   * Gets an event based on its UUID.
   *
   * @param eventId the UUID of the event.
   * @return the event, or an empty {@code Optional} if no event with that ID exists
   */
  public Optional<EventDto> get(String eventId) {
    return Optional.ofNullable(eventManager.getEvents().get(eventId)).map(EventDto::from);
  }

  /**
   * Creates a new event based on a {@link EventDto} record.
   *
   * @param dto the {@link EventDto} with the event info.
   * @return {@code true} if the event is successfully created; {@code false} if else.
   */
  public boolean create(EventDto dto) {
    return eventManager.addEvent(dto.toEvent());
  }

  /**
   * Updates an event with new information.
   *
   * @param eventId the eventId of the event to be changed.
   * @param dto     the DTO with the updated information.
   * @return {@code true} if the event is successfully updated; {@code false} if else.
   */
  public boolean update(String eventId, EventDto dto) {
    return eventManager.editEvent(eventId, dto.toEvent());
  }

  /**
   * Deletes an event based on its UUID.
   *
   * @param eventId the {@link Event} UUID
   * @return {@code true} if the event got deleted; {@code false} if else.
   */
  public boolean delete(String eventId) {
    if (!eventManager.removeEvent(eventId)) {
      return false;
    }
    userManager.removeEvent(eventId);
    return true;
  }

  // ==================== USER LOGIC ====================
  /**
   * Resolves a username from the given authentication token. <br> <br>
   * <b>Note:</b> authToken is currently the same as username.
   * Better authentication and proper token validation may be added later, and this method
   * encapsulates the behavior so it's easy to do so.
   * Better security is not within the scope of this project.
   *
   * @param authToken the authentication token to process
   * @return the corresponding username
   */
  private String resolveUsername(String authToken) {
    return authToken;
  }

  /**
   * Logs into an existing user account.
   *
   * @param username the username
   * @param password the user password
   * @return {@code true} if the login in successful; {@code false} if else.
   */
  public boolean login(String username, String password) {
    return userManager.login(username, password);
  }

  /**
   * Registers a new user from username and password.
   *
   * @param username the username
   * @param password the user password
   * @return {@code true} if the registration is successful; {@code false if not}
   */
  public boolean register(String username, String password) {
    return userManager.register(username, password);
  }

  //  public boolean editUser(String newUsername, String authToken) {
  //    User user = userManager.getUser(authToken);
  //    return userManager.editUser(newUsername, user);
  //  }

  /**
   * Opts user in on an event.
   *
   * @param eventId the event to opt into.
   * @param authToken the user authToken.
   * @return {@code false} if user was already opted in the event;
   *        {@code true} otherwise.
   */
  public boolean optIn(String eventId, String authToken) {
    return userManager.optIn(eventId, resolveUsername(authToken));
  }

  /**
   * Opts user out of an event.
   *
   * @param eventId the event to opt out of.
   * @param authToken the user authToken.
   * @return {@code false} if user wasnt opted in the event;
   *        {@code true} otherwise.
   */
  public boolean optOut(String eventId, String authToken) {
    return userManager.optOut(eventId, resolveUsername(authToken));
  }
}
