package springboot.restserver.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import springboot.restserver.service.EventManagerService;


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

    // ==================== GET MAPPINGS ====================



    // ==================== POST MAPPING ====================

    // ==================== PUT MAPPING (UPDATE) ====================

    // ==================== DELETE MAPPING ====================

}
