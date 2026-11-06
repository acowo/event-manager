package springboot.restserver.dto;

import event.Event;
import java.time.LocalDateTime;

public record EventDto(String eventId, String title, String place, LocalDateTime time) {

    public static EventDto from(Event event) {
        return new EventDto(event.getEventId(), event.getTitle(), event.getPlace(), event.getTime());
    }

    public Event toEvent() {
        return new Event(eventId,title, place, time);
    }
}