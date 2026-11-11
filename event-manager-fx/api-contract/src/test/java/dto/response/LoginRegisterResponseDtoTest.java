package dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Test class for LoginRegisterResponseDto.
 */
class LoginRegisterResponseDtoTest {

  /**
   * Test the constructor and accessor methods of LoginRegisterResponseDto.
   */
  @Test
  void testConstructorAndAccessor() {
    String id = "test-id";
    boolean success = true;

    LoginRegisterResponseDto response = new LoginRegisterResponseDto(id, success);

    assertNotNull(response);
    assertEquals(id, response.id());
    assertEquals(success, response.success());
  }
}
