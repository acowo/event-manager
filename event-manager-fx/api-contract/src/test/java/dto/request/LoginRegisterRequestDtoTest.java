package dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Test class for LoginRegisterRequestDto.
 */
class LoginRegisterRequestDtoTest {
  /**
   * Test the constructor and accessor methods of LoginRegisterRequestDto.
   */
  @Test
  void testConstructorAndAccessor() {
    String username = "test-username";
    String password = "test-password";

    LoginRegisterRequestDto request = new LoginRegisterRequestDto(username, password);

    assertNotNull(request);
    assertEquals(username, request.username());
    assertEquals(password, request.password());
  }
}
