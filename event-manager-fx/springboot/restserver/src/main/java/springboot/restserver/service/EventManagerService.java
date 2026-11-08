package springboot.restserver.service;

import dto.EventDto;
import event.Event;
import event.EventManager;
import org.springframework.stereotype.Service;
import user.UserManager;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class EventManagerService {

  private final EventManager eventManager;
  private final UserManager userManager;

  public EventManagerService(EventManager eventManager, UserManager userManager) {
    this.eventManager = eventManager;
    this.userManager = userManager;
  }

  // ==================== EVENT LOGIC ====================

  public synchronized List<EventDto> getAll() {
    return eventManager.getEvents().values().stream().sorted(Comparator.comparing(Event::getTime)).map(EventDto::from)
        .toList();
  }

  public Optional<EventDto> get(String eventId) {
    return Optional.ofNullable(eventManager.getEvents().get(eventId)).map(EventDto::from);
  }

  public boolean create(EventDto dto) {
    return eventManager.addEvent(dto.toEvent());
  }

  public boolean update(String eventId, EventDto dto) {
    return eventManager.editEvent(eventId, dto.toEvent());
  }

  public boolean delete(String eventId) {
    if (!eventManager.removeEvent(eventId)) {
      return false;
    }
    userManager.removeEvent(eventId);
    return true;
  }


  // ==================== USER LOGIC ====================
  /**
   * Resolves a username from the given authentication token.
   * <p>
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

  public boolean login(String username, String password) {
    return userManager.login(username, password);
  }

  public boolean register(String username, String password) {
    return userManager.register(username, password);
  }

//  public boolean editUser(String newUsername, String authToken) {
//    User user = userManager.getUser(authToken);
//    return userManager.editUser(newUsername, user);
//  }

  /**
   *
   * @param eventId
   * @param authToken
   * @return false if user was already opted in the event; true otherwise
   */
  public boolean optIn(String eventId, String authToken){
    return userManager.optIn(eventId, resolveUsername(authToken));
  }

  public boolean optOut(String eventId, String authToken){
    return userManager.optOut(eventId, resolveUsername(authToken));
  }
}
