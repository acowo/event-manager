package dto.response;

import java.util.Map;

/**
 * Response body for all the events a user is attending.
 *
 * @param events a {@link Map} of the eventIds
 *               and wether or not the user is admin.
 */
public record UserGetEventsResponseDto(Map<String, Boolean> events) {
    public UserGetEventsResponseDto(Map<String, Boolean> events) {
        this.events = Map.copyOf(events);
    }

    public Map<String, Boolean> getEvents() {
        return Map.copyOf(events);
    }
}
