package dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Test class for EditUserRequestDto.
 */
class EditUserRequestDtoTest {

  /**
   * Test the constructor and accessor methods of EditUserRequestDto.
   */
  @Test
  void testConstructorAndAccessor() {
    String token = "test-token";

    EditUserRequestDto request =
            new EditUserRequestDto(token);

    assertNotNull(request);
    assertEquals(token, request.authToken());
  }
}
