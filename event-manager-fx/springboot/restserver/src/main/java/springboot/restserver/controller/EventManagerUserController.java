package springboot.restserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springboot.restserver.dto.LoginRegisterRequestDTO;
import springboot.restserver.service.EventManagerService;

@RestController
@RequestMapping("/api/users")
public class EventManagerUserController {

    private final EventManagerService eventManagerService;

    /**
     * Sets the service for this controller.
     * @param eventManagerService
     */
    public EventManagerUserController(EventManagerService eventManagerService) {
        this.eventManagerService = eventManagerService;
    }


   @PostMapping("/login")
    public ResponseEntity<Boolean> handleLoginRequest(
            @RequestBody LoginRegisterRequestDTO loginRequest){
            boolean success = this.eventManagerService.login(loginRequest.username(), loginRequest.password());
            if (success){
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.status(401).body(false);
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> handleRegisterRequest(
            @RequestBody LoginRegisterRequestDTO loginRequest){
        boolean success = this.eventManagerService.register(loginRequest.username(), loginRequest.password());
        if (success){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(401).body(false);
    }
}
