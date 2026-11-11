package dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Test class for EditUserResponseDto.
 */
class EditUserResponseDtoTest {

  /**
   * Test the constructor and accessor methods of EditUserResponseDto.
   */
  @Test
  void testConstructorAndAccessor() {
    boolean success = false;

    EditUserResponseDto request =
            new EditUserResponseDto(success);

    assertNotNull(request);
    assertEquals(success, request.success());
  }
}