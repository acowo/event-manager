package dto.response;

import java.util.Map;

public record UserGetEventsResponseDto(Map<String, Boolean> events) {
}
