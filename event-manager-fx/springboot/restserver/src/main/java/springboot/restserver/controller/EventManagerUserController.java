package springboot.restserver.controller;


import dto.response.LoginRegisterResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import dto.request.LoginRegisterRequestDTO;
import org.springframework.web.server.ResponseStatusException;
import springboot.restserver.service.EventManagerService;

@RestController
@RequestMapping("/api/users")
public class EventManagerUserController {

  private final EventManagerService eventManagerService;

  /**
   * Sets the service for this controller.
   *
   * @param eventManagerService
   */
  public EventManagerUserController(EventManagerService eventManagerService) {
    this.eventManagerService = eventManagerService;
  }

  @PostMapping("/login")
  public LoginRegisterResponseDTO handleLoginRequest(@RequestBody LoginRegisterRequestDTO loginRequest){
    boolean success = this.eventManagerService.login(loginRequest.username(), loginRequest.password());
    return new LoginRegisterResponseDTO(loginRequest.username(), success);
  }

  @PostMapping("/register")
  public LoginRegisterResponseDTO handleRegisterRequest(@RequestBody LoginRegisterRequestDTO registerRequest){
    boolean success = this.eventManagerService.register(registerRequest.username(), registerRequest.password());
    return new LoginRegisterResponseDTO(registerRequest.username(), success);
  }

  @PutMapping("/optIn/{eventId}")
  @ResponseStatus(HttpStatus.NO_CONTENT) //Added synchronized because apparently simultaneous writes can overwrite each other!
  public synchronized void optIn(@RequestHeader("Authorization") String token, @PathVariable String eventId) {
    if (eventManagerService.get(eventId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);   //Event doesnt exist, just for extra safety
    }
    if (!eventManagerService.optIn(eventId, token)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);    //User already attends event
    }
  }

  @PutMapping("/optOut/{eventId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public synchronized void optOut(@RequestHeader("Authorization") String token, @PathVariable String eventId) {
    if (eventManagerService.get(eventId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);   //Event doesnt exist, just for safety
    }
    if (!eventManagerService.optOut(eventId, token)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);    //User didnt attend to begin with...
    }
  }

}
