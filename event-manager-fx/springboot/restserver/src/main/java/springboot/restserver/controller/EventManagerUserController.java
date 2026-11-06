package springboot.restserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.restserver.service.EventManagerEventService;

@RestController
@RequestMapping("/api/users")
public class EventManagerUserController {

    private final EventManagerEventService eventManagerEventService;

    /**
     * Sets the service for this controller.
     * @param eventManagerEventService
     */
    public EventManagerUserController(EventManagerEventService eventManagerEventService) {
        this.eventManagerEventService = eventManagerEventService;
    }



}
