package springboot.restserver.service;

import org.springframework.stereotype.Service;
import event.Event;
import event.EventManager;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import springboot.restserver.dto.EventDto;
import user.UserManager;


@Service
public class EventManagerService {

    private final EventManager eventManager;
    private final UserManager userManager;

    public EventManagerService(EventManager eventManager, UserManager userManager) {
        this.eventManager = eventManager;
        this.userManager = userManager;
    }

}
