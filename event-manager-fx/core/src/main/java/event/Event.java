package event;
import java.time.LocalDateTime;

public class Event {
    private String title;
    private String place;
    private LocalDateTime time;

    public Event(String title, String place, LocalDateTime time) {
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
