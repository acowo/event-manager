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

    // ==================== EVENT LOGIC ====================

    public synchronized List<EventDto> getAll() {
        return eventManager.getEvents().values().stream()
                .sorted(Comparator.comparing(Event::getTime))
                .map(EventDto::from)
                .toList();
    }

    public Optional<EventDto> get(String eventId) {
        return Optional.ofNullable(eventManager.getEvents().get(eventId)).map(EventDto::from);
    }

    public boolean create(EventDto dto) {
        return eventManager.addEvent(dto.toEvent());
    }

    public boolean update(String eventId, EventDto dto) {
        return eventManager.editEvent(eventId, dto.toEvent());
    }

    public boolean delete(String eventId) {
        if (!eventManager.removeEvent(eventId)) {
            return false;
        }
        userManager.removeEvent(eventId);
        return true;
    }


    // ==================== USER LOGIC ====================


}
