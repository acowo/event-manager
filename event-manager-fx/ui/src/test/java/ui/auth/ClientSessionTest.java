package ui.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ClientSession}.
 *
 * <p>Verifies that the authentication token can be set, updated, read and cleared.
 * Since {@link ClientSession} holds static state, the token is reset to {@code null}
 * before and after each test so that the tests do not affect each other.
 */
class ClientSessionTest {

  /**
   * Resets the authentication token to {@code null} before each test,
   * so every test starts from a clean session.
   */
  @BeforeEach
  void setUp() {
    ClientSession.setAuthToken(null);
  }

  /**
   * Resets the authentication token to {@code null} after each test,
   * so no state is left behind in the static session.
   */
  @AfterEach
  void tearDown() {
    ClientSession.setAuthToken(null);
  }

  /**
   * Tests that a token set with {@link ClientSession#setAuthToken(String)}
   * is returned by {@link ClientSession#getAuthToken()}.
   */
  @Test
  void testSetAuthToken() {
    ClientSession.setAuthToken("test-token");

    assertEquals("test-token", ClientSession.getAuthToken());
  }

  /**
   * Tests that setting a new token replaces the previously stored token.
   */
  @Test
  void testUpdateAuthToken() {
    ClientSession.setAuthToken("old-token");
    ClientSession.setAuthToken("new-token");

    assertEquals("new-token", ClientSession.getAuthToken());
  }

  /**
   * Tests that {@link ClientSession#clearAuthToken()} removes the stored token,
   * leaving an empty string.
   */
  @Test
  void testClearAuthToken() {
    ClientSession.setAuthToken("test-token");
    ClientSession.clearAuthToken();

    assertEquals("", ClientSession.getAuthToken());
  }

  /**
   * Tests that no token is stored when the session is fresh,
   * i.e. {@link ClientSession#getAuthToken()} returns {@code null}.
   */
  @Test
  void testInitialAuthToken() {
    assertNull(ClientSession.getAuthToken());
  }
}