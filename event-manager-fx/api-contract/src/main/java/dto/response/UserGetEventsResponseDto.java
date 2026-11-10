package dto.response;

import java.util.Map;

/**
 * Response body for all the events a user is attending.
 *
 * @param events a {@link Map} of the eventIds
 *               and wether or not the user is admin.
 */
public record UserGetEventsResponseDto(Map<String, Boolean> events) {
  /**
   * Creates a response with an immutable copy of the given events.
   *
   * @param events a {@link Map} of event IDs to whether the user is an admin of that event
   */
  public UserGetEventsResponseDto(Map<String, Boolean> events) {
    this.events = Map.copyOf(events);
  }

  /**
   * Gets a map of all events for a user.
   *
   * @return a map with eventIds and matching admin value.
   */
  public Map<String, Boolean> getEvents() {
    return Map.copyOf(events);
  }
}
