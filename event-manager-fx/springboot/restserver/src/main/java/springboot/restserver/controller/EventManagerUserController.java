package springboot.restserver.controller;

import dto.request.LoginRegisterRequestDto;
import dto.response.LoginRegisterResponseDto;
import dto.response.UserGetEventsResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springboot.restserver.service.EventManagerService;

/**
 * REST controller exposing login and registration operations for users.
 */
@RestController
@RequestMapping("/api/users")
public class EventManagerUserController {

  private final EventManagerService eventManagerService;

  /**
   * Creates a controller backed by the given service.
   *
   * @param eventManagerService the service that handles user operations
   */
  public EventManagerUserController(EventManagerService eventManagerService) {
    this.eventManagerService = eventManagerService;
  }

  /**
   * Handles a login request.
   *
   * @param loginRequest the username and password to authenticate
   * @return the username and whether the login succeeded
   */
  @PostMapping("/login")
  public LoginRegisterResponseDto handleLoginRequest(
      @RequestBody LoginRegisterRequestDto loginRequest) {
    boolean success = this.eventManagerService.login(
        loginRequest.username(),
        loginRequest.password());
    return new LoginRegisterResponseDto(loginRequest.username(), success);
  }

  /**
   * Handles a registration request.
   *
   * @param registerRequest the username and password for the new user
   * @return the username and whether the registration succeeded
   */
  @PostMapping("/register")
  public LoginRegisterResponseDto handleRegisterRequest(
      @RequestBody LoginRegisterRequestDto registerRequest) {
    boolean success = this.eventManagerService.register(
        registerRequest.username(),
        registerRequest.password());
    return new LoginRegisterResponseDto(registerRequest.username(), success);
  }

  /**
   * Opts a user in to an event.
   *
   * @param token   the Authorization header identifying the user
   * @param eventId the ID of the event
   * @throws ResponseStatusException 404 if the event doesn't exist;
   *        409 if the user already attends
   */
  @PutMapping("/me/events/{eventId}")
  @ResponseStatus(HttpStatus.NO_CONTENT) //Added synchronized because apparently simultaneous writes can overwrite each other!
  public synchronized void optIn(@RequestHeader("Authorization") String token, @PathVariable("eventId") String eventId) {
    if (eventManagerService.get(eventId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      // Event doesnt exist, just for extra safety
    }
    if (!eventManagerService.optIn(eventId, token)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
      // User already attends event
    }
  }

  /**
   * Opts a user out of an event.
   *
   * @param token   the Authorization header identifying the user
   * @param eventId the ID of the event
   * @throws ResponseStatusException 404 if the event doesn't exist;
   *        409 if the user wasn't attending
   */
  @DeleteMapping("/me/events/{eventId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public synchronized void optOut(
      @RequestHeader("Authorization") String token,
      @PathVariable("eventId") String eventId) {
    if (eventManagerService.get(eventId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      // Event doesnt exist, just for safety
    }
    if (!eventManagerService.optOut(eventId, token)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
      // User didnt attend to begin with...
    }
  }

  @GetMapping("/me/events")
  @ResponseStatus(HttpStatus.OK)
  public UserGetEventsResponseDto getUserEvents(@RequestHeader("Authorization") String token){
    try {
      return new UserGetEventsResponseDto(eventManagerService.getUserEvents(token));
    } catch (IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }
}
