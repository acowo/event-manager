package event;

import java.util.Map;

/**
 * 
 * EventManager implements core domain logic for event managing. 
 */
public class EventManager {
    
    private final EventRepository events;

    /**
     * Creates a new EventManager
     * @param events A place where events are stored, must implement EventRepository interface
     */
    public EventManager(EventRepository events) {
        this.events = events.copy();
    }

    /**
     * Adds an event to the EventRepository 
     * @param event Event to be added 
     * @return {@code true} if event was successfully added;
     * {@code false} if the event had already been added previously 
     */
    public boolean addEvent(Event event) {
        if (events.eventExists(event.getTitle())) {
            return false;
        }

        events.addEvent(event);
        return true;
    }

    /**
     * Removes an event from the EventRepository
     * @param title identifier of Event to be removed 
     * @return {@code true} if the event was successfully removed;
     * {@code false} if event was not added previously
     */
    public boolean removeEvent(String title) {
        if (!events.eventExists(title)) {
            return false;
        }

        events.removeEvent(title);
        return true;
    }

    /**
     * Updates an existing Event using a new Event object
     * @param title Name for the event to be edited 
     * @param event The new Event object containing updated data 
     * @return {@code true} if the event was successfully edited;
 *         {@code false} if the event does not exist
     */
    public boolean editEvent(String title, Event event) {
        if(!events.eventExists(title)) {
            return false;
        }

        events.editEvent(title, event);
        return true;
    }

    /**
     * Get the Event object with a matching identifier
     * @param title name/identifier of the event to get
     * @return event with the corresponding name/identifier
     * @throws IllegalArgumentException if no event with the identifier exists
     */
    public Event getEvent(String title) {
        return events.getEvent(title);
    }


    /**
     * Get a map of all the events 
     * @return map of all events with title/identifier keys
     */
    public Map<String, Event> getEvents() {
        return events.getEvents();
    }
}
