
package ui.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientSessionTest {

  @BeforeEach
  void setUp() {
    ClientSession.setAuthToken(null);
  }

  @AfterEach
  void tearDown() {
    ClientSession.setAuthToken(null);
  }

  @Test
  void testSetAuthToken() {
    ClientSession.setAuthToken("test-token");

    assertEquals("test-token", ClientSession.getAuthToken());
  }

  @Test
  void testUpdateAuthToken() {
    ClientSession.setAuthToken("old-token");
    ClientSession.setAuthToken("new-token");

    assertEquals("new-token", ClientSession.getAuthToken());
  }

  @Test
  void testClearAuthToken() {
    ClientSession.setAuthToken("test-token");
    ClientSession.clearAuthToken();

    assertEquals("", ClientSession.getAuthToken());
  }

  @Test
  void testInitialAuthToken() {
    assertNull(ClientSession.getAuthToken());
  }
}
