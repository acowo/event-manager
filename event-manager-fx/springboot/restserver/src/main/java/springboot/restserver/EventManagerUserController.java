package springboot.restserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
