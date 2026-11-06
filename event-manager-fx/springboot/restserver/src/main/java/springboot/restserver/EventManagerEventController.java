package springboot.restserver;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/api/events")
public class EventManagerEventController {

    private final EventManagerService eventManagerService;

    /**
     * Sets the service for this controller.
     * @param eventManagerService
     */
    public EventManagerEventController(EventManagerService eventManagerService) {
        this.eventManagerService = eventManagerService;
    }
}
