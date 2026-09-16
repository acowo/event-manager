package event;

import java.time.LocalDateTime;

/**
 * Represents an event with a title, place, and time.
 */
public class Event {
    private String title;
    private String place;
    private LocalDateTime time;

    /**
     * Creates a new event.
     *
     * @param title the title of the event
     * @param place the place where the event takes place
     * @param time the date and time of the event
     * @throws IllegalArgumentException if the title or place is null or blank,
     *         or if the time is null
     */
    public Event(String title, String place, LocalDateTime time) {

      if (title == null || title.isBlank()) {
        throw new IllegalArgumentException("Title cannot be empty");
      }

      if (place == null || place.isBlank()) {
        throw new IllegalArgumentException("Place cannot be empty");
      }

      if (time == null) {
        throw new IllegalArgumentException("Time cannot be null");
      }

    this.title = title;
    this.place = place;
    this.time = time;
    }

    /**
     * Returns the title of the event.
     *
     * @return the event title
     */
    public  String getTitle(){
      return title;
    }

    /**
     * Returns the place where the event takes place.
     *
     * @return the event place
     */
    public String getPlace(){
      return place;
    }

    /**
     * Returns the date and time of the event.
     *
     * @return the event date and time
     */
    public LocalDateTime getTime(){
      return time;
    }



}
