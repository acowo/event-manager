
package event;

import java.util.Map;

/** EventManager implements core domain logic for event managing. */
public class EventManager {

  private final EventRepository events;

  /**
   * Creates a new EventManager.
   *
   * @param events A place where events are stored, must implement EventRepository
   *     interface
   */
  public EventManager(EventRepository events) {
    this.events = events.copy();
  }

  /**
   * Adds an event to the EventRepository.
   *
   * @param event Event to be added
   * @return {@code true} if event was successfully added;
   *     {@code false} if the event had already been added previously
   */
  public boolean addEvent(Event event) {
    if (events.eventExists(event.getEventId())) {
      return false;
    }
    events.addEvent(event);
    return true;
  }

  /**
   * Removes an event from the EventRepository.
   *
   * @param eventId identifier of Event to be removed
   * @return {@code true} if the event was successfully removed;
   *     {@code false} if event was not added previously
   */
  public boolean removeEvent(String eventId) {
    if (!events.eventExists(eventId)) {
      return false;
    }

    events.removeEvent(eventId);
    return true;
  }

  /**
   * Updates an existing Event using a new Event object.
   *
   * @param eventId Identifier of the event to be edited
   * @param event The new Event object containing updated data
   * @return {@code true} if the event was successfully edited;
   *     {@code false} if the event does not exist
   */
  public boolean editEvent(String eventId, Event event) {
    if (!events.eventExists(eventId)) {
      return false;
    }

    events.editEvent(eventId, event);
    return true;
  }

  /**
   * Gets the Event object with a matching identifier.
   *
   * @param eventId UUID of the event to get
   * @return event with the corresponding UUID
   * @throws IllegalArgumentException if no event with the identifier exists
   */
  public Event getEvent(String eventId) {
    return events.getEvent(eventId);
  }

  /**
   * Gets a map of all the events.
   *
   * @return map of all events with UUID keys
   */
  public Map<String, Event> getEvents() {
    return events.getEvents();
  }
}
