package springboot.restserver.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import springboot.restserver.service.EventManagerEventService;


@RestController
@RequestMapping("/api/events")
public class EventManagerEventController {

    private final EventManagerEventService eventManagerEventService;

    /**
     * Sets the service for this controller.
     * @param eventManagerEventService
     */
    public EventManagerEventController(EventManagerEventService eventManagerEventService) {
        this.eventManagerEventService = eventManagerEventService;
    }

    // ==================== GET MAPPINGS ====================



    // ==================== POST MAPPING ====================

    // ==================== PUT MAPPING (UPDATE) ====================

    // ==================== DELETE MAPPING ====================

}
