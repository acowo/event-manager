package dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test class for UserGetEventsResponseDto.
 */
class UserGetEventsResponseDtoTest {

  /**
   * Test the constructor and accessor methods of UserGetEventsResponseDto.
   */
  @Test
  void testConstructorAndAccessor() {
    Map<String, Boolean> events = Map.of("event1", true, "event2", false);

    UserGetEventsResponseDto response =
            new UserGetEventsResponseDto(events);

    assertNotNull(response);
    assertEquals(events, response.events());
    assertEquals(events, response.getEvents());
  }
}
