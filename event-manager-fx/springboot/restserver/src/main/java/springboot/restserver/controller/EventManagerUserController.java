package springboot.restserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springboot.restserver.dto.EventDto.LoginRequestDTO;
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


   /*  @PostMapping("/login")
    public ResponseEntity<?> handleLogin(
            @RequestBody LoginRequestDTO loginRequest){
        
        
    } */



}
