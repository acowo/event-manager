package event;

import java.util.Map;

public class EventManager {
    
    private final EventRepository events;

    public EventManager(EventRepository events) {
        this.events = events.copy();
    }

    public boolean addEvent(Event event) {
        if (events.eventExists(event.getTitle())) {
            return false;
        }

        events.addEvent(event);
        return true;
    }

    public boolean removeEvent(String title) {
        if (!events.eventExists(title)) {
            return false;
        }

        events.removeEvent(title);
        return true;
    }

    public boolean editEvent(String title, Event event) {
        if(!events.eventExists(title)) {
            return false;
        }

        events.editEvent(title, event);
        return true;
    }

    public Event getEvent(String title) {
        return events.getEvent(title);
    }

    public Map<String, Event> getEvents() {
        return events.getEvents();
    }
}
