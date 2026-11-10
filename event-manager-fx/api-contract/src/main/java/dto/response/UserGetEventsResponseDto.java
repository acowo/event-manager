package dto.response;

import java.util.Map;

public record UserGetEventsResponseDto(Map<String, Boolean> events) {
    public UserGetEventsResponseDto(Map<String, Boolean> events) {
        this.events = Map.copyOf(events);
    }

    public Map<String, Boolean> getEvents() {
        return Map.copyOf(events);
    }
}
