package dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Test class for UserGetEventsRequestDto.
 */
class UserGetEventsRequestDtoTest {

  /**
   * Test the constructor and accessor methods of UserGetEventsRequestDto.
   */
  @Test
  void testConstructorAndAccessor() {
    String token = "test-token";

    UserGetEventsRequestDto request =
            new UserGetEventsRequestDto(token);

    assertNotNull(request);
    assertEquals(token, request.userToken());
  }
}
