package event;
import java.time.LocalDateTime;

public class Event {
    private String title;
    private String place;
    private LocalDateTime time;

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

    public  String getTitle(){
      return title;
    }

    public String getPlace(){
      return place;
    }

    public LocalDateTime getTime(){
      return time;
    }


}
