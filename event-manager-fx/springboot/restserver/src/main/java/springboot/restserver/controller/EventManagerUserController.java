package springboot.restserver.controller;

import dto.response.LoginRegisterResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dto.request.LoginRegisterRequestDTO;
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
}
