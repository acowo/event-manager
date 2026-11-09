package dto.response;

import java.util.Map;

public record UserGetEventsResponseDTO(Map<String, Boolean> events) {
}
