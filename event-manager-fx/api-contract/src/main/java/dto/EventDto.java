package dto;

import event.Event;
import java.time.LocalDateTime;

/**
 * Data about a single event.
 *
 * @param eventId the unique identifier of the event
 * @param title the title of the event
 * @param place the location where the event takes place
 * @param time the date and time when the event starts
 */
public record EventDto(String eventId, String title, String place, LocalDateTime time) {

  /**
   * Returns new DTO from an {@link Event} object.
   *
   * @param event the event to be parsed
   * @return returns an EventDTO
   */
  public static EventDto from(Event event) {
    return new EventDto(event.getEventId(), event.getTitle(), event.getPlace(), event.getTime());
  }

  /**
   * Turns the record to an {@link Event} based on the stored fields.
   *
   * @return returns an {@link Event} object that has the same fields.
   */
  public Event toEvent() {
    return new Event(eventId, title, place, time);
  }
}
