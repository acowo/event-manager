package ui.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.net.httpserver.HttpServer;
import dto.response.LoginRegisterResponseDto;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.auth.ClientSession;

/**
 * Test class for UserRestApiClient.
 */
class UserRestApiClientTest {
  private HttpServer server;
  private UserRestApiClient api;
  private int responseStatus;
  private String responseBody;
  private String lastMethod;
  private String lastPath;
  private String lastBody;
  private String lastContentType;
  private String lastAuthorization;

  /**
   * Starts a local HTTP server for testing.
   *
   * <p>The server records each request's method, path, body and headers,
   * then responds with the configured status and response body.
   *
   * @throws IOException if the local HTTP server cannot be created.
   */
  @BeforeEach
  void startServer() throws IOException {
    server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
    server.createContext("/", exchange -> {
      lastMethod = exchange.getRequestMethod();
      lastPath = exchange.getRequestURI().getPath();
      lastBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
      lastContentType = exchange.getRequestHeaders().getFirst("Content-Type");
      lastAuthorization = exchange.getRequestHeaders().getFirst("Authorization");

      byte[] response = responseBody.getBytes(StandardCharsets.UTF_8);
      if (responseStatus == 204) {
        exchange.sendResponseHeaders(responseStatus, -1);
      } else {
        exchange.sendResponseHeaders(responseStatus, response.length);
        exchange.getResponseBody().write(response);
      }
      exchange.close();
    });
    server.start();

    api = new UserRestApiClient(
        "http://localhost:" + server.getAddress().getPort() + "/users");
    responseStatus = 200;
    responseBody = "{}";
    ClientSession.setAuthToken("test-token");
  }

  /**
   * Stops the local HTTP server after each test.
   * And clears the authentication token in ClientSession.
   */
  @AfterEach
  void stopServer() {
    server.stop(0);
    ClientSession.setAuthToken(null);
  }

  /**
   * Tests the login method.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void loginPostsCredentialsAndReturnsResponse() throws Exception {
    responseBody = "{\"id\":\"user-1\",\"success\":true}";

    LoginRegisterResponseDto result = api.login("alice", "secret");

    assertEquals("user-1", result.id());
    assertTrue(result.success());
    assertEquals("POST", lastMethod);
    assertEquals("/users/login", lastPath);
    assertEquals("application/json", lastContentType);
    assertTrue(lastBody.contains("\"username\":\"alice\""));
    assertTrue(lastBody.contains("\"password\":\"secret\""));
  }

  /**
   * Tests the register method.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void registerPostsCredentialsAndReturnsResponse() throws Exception {
    responseBody = "{\"id\":\"user-2\",\"success\":true}";

    LoginRegisterResponseDto result = api.register("bob", "password");

    assertEquals("user-2", result.id());
    assertTrue(result.success());
    assertEquals("POST", lastMethod);
    assertEquals("/users/register", lastPath);
    assertEquals("application/json", lastContentType);
    assertTrue(lastBody.contains("\"username\":\"bob\""));
    assertTrue(lastBody.contains("\"password\":\"password\""));
  }

  /**
   * Tests the optIn method when the status is 204.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void optInReturnsTrueForNoContentAndSendsAuthorization() throws Exception {
    responseStatus = 204;

    assertTrue(api.optIn("event-1"));
    assertEquals("PUT", lastMethod);
    assertEquals("/users/me/events/event-1", lastPath);
    assertEquals("test-token", lastAuthorization);
  }

  /**
   * Tests the optIn method when the status is not 204.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void optInReturnsFalseForOtherStatuses() throws Exception {
    responseStatus = 409;

    assertFalse(api.optIn("event-1"));
  }

  /**
   * Tests the optIn method when the request fails due to an IOException.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void optOutReturnsTrueForNoContentAndSendsAuthorization() throws Exception {
    responseStatus = 204;

    assertTrue(api.optOut("event-1"));
    assertEquals("DELETE", lastMethod);
    assertEquals("/users/me/events/event-1", lastPath);
    assertEquals("test-token", lastAuthorization);
  }

  /**
   * Tests the optOut method when the status is not 204.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void optOutReturnsFalseForOtherStatuses() throws Exception {
    responseStatus = 404;

    assertFalse(api.optOut("event-1"));
  }
  
  /**
   * Tests the getUserEvents method.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void getUserEventsReturnsEventsMap() throws Exception {
    responseBody = "{\"events\":{\"event-1\":true,\"event-2\":false}}";

    Map<String, Boolean> events = api.getUserEvents();

    assertEquals(Map.of("event-1", true, "event-2", false), events);
    assertEquals("GET", lastMethod);
    assertEquals("/users/me/events", lastPath);
    assertEquals("test-token", lastAuthorization);
  }

  /**
   * Tests the getUserEvents method when the status is not 200.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void getUserEventsReturnsEmptyMapForNonOkStatus() throws Exception {
    responseStatus = 401;
    responseBody = "{\"events\":{}}";

    assertTrue(api.getUserEvents().isEmpty());
  }

  /**
   * Tests the deleteUser method when the status is 200.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void deleteUserReturnsTrueForOkStatus() {
    responseStatus = 200;

    assertTrue(api.deleteUser());
    assertEquals("DELETE", lastMethod);
    assertEquals("/users/me", lastPath);
    assertEquals("test-token", lastAuthorization);
  }

  /**
   * Tests the deleteUser method when the status is not 200.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void deleteUserReturnsFalseForNonOkStatus() {
    responseStatus = 204;

    assertFalse(api.deleteUser());
  }

  /**
   * Tests the deleteUser method when the request fails due to an IOException.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void deleteUserReturnsFalseWhenRequestFails() throws IOException {
    server.stop(0);

    assertFalse(api.deleteUser());
  }
}
