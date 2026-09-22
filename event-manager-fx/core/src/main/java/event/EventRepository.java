package event;

import java.util.Map;

/**
 * Defines the operations for storing and retrieving events.
 */
public interface EventRepository {

    /**
     * Gets an event by its title.
     *
     * @param title the title of the event
     * @return the event with the given title
     * @throws IllegalArgumentException if the event does not exist
     */
    Event getEvent(String title);

    /**
     * Checks whether an event with the given title exists.
     *
     * @param title the title of the event
     * @return true if the event exists, otherwise false
     */
    boolean eventExists(String title);

    /**
     * Adds an event.
     *
     * @param event the event to add
     * @throws IllegalArgumentException if the event already exists
     */
    void addEvent(Event event);

    /**
     * Removes an event by its title.
     *
     * @param title the title of the event to remove
     * @throws IllegalArgumentException if the event does not exist
     */
    void removeEvent(String title);

    /**
     * Updates an existing event.
     *
     * @param title the title of the event to update
     * @param event the updated event
     * @throws IllegalArgumentException if the event does not exist
     */
    void editEvent(String title, Event event);

    /**
     * Gets all stored events.
     *
     * @return a map containing all events, indexed by title
     */
    Map<String, Event> getEvents();
}