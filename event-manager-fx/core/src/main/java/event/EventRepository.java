package event;

import java.util.Map;

/**
 * Defines the operations for storing and retrieving events.
 */
public interface EventRepository {

    /**
     * Gets an event by its UUID.
     *
     * @param eventId the UUID of the event
     * @return the event with the given UUID
     * @throws IllegalArgumentException if the event does not exist
     */
    Event getEvent(String eventId);

    /**
     * Checks whether an event with the given UUID exists.
     *
     * @param eventId the UUID of the event
     * @return true if the event exists, otherwise false
     */
    boolean eventExists(String eventId);

    /**
     * Adds an event.
     *
     * @param event the event to add
     * @throws IllegalArgumentException if the event already exists
     */
    void addEvent(Event event);

    /**
     * Removes an event by its UUID.
     *
     * @param eventId the UUID of the event to remove
     * @throws IllegalArgumentException if the event does not exist
     */
    void removeEvent(String eventId);

    /**
     * Updates an existing event. The updated event must have the same ID.
     *
     * @param eventId the UUID of the event to update
     * @param event the updated event
     * @throws IllegalArgumentException if the event does not exist
     */
    void editEvent(String eventId, Event event);

    /**
     * Gets all stored events.
     *
     * @return a map containing all events, indexed by UUID
     */
    Map<String, Event> getEvents();

    /**
     * Copies this class
     *
     * @return a EventRepository that is a copy
     */
    EventRepository copy();
}