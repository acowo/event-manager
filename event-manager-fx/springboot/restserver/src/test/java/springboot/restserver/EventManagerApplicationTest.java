package springboot.restserver;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

/**
 * Tests the Spring Boot application startup.
 */
class EventManagerApplicationTest {

  /**
   * Verifies that the application starts without throwing an exception.
   */
  @Test
  void mainStartsApplication() {
    assertDoesNotThrow(
            () -> EventManagerApplication.main(
                    new String[] {"--spring.main.web-application-type=none"}));
  }
}