package ui.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.net.httpserver.HttpServer;
import dto.EventDto;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.auth.ClientSession;

/**
 * Test class for EventRestApiClient.
 */
class EventRestApiClientTest {
  private static final LocalDateTime EVENT_TIME = LocalDateTime.of(2026, 12, 1, 18, 0);
  private static final String EVENT_JSON =
      "{\"eventId\":\"event-1\",\"title\":\"Concert\","
          + "\"place\":\"Trondheim\",\"time\":\"2026-12-01T18:00:00\"}";

  private HttpServer server;
  private EventRestApiClient api;
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
   * @throws IOException if the local HTTP server cannot be created
   */
  @BeforeEach
  void startServer() throws IOException {
    server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
    server.createContext("/events", exchange -> {
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

    api = new EventRestApiClient(
        "http://localhost:" + server.getAddress().getPort() + "/events");
    responseStatus = 200;
    responseBody = "[]";
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
   * Tests the getAllEvents method.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void getAllEventsReturnsDeserializedEvents() throws Exception {
    responseBody = "[" + EVENT_JSON + "]";

    List<EventDto> events = api.getAllEvents();

    assertEquals(1, events.size());
    assertEquals("event-1", events.get(0).eventId());
    assertEquals("Concert", events.get(0).title());
    assertEquals("GET", lastMethod);
    assertEquals("/events", lastPath);
  }

  /**
   * Tests the getAllEvents method when the status is not 200.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void getAllEventsReturnsEmptyListForNonOkStatus() throws Exception {
    responseStatus = 500;

    assertTrue(api.getAllEvents().isEmpty());
    assertEquals("GET", lastMethod);
  }

  /*
   * Tests the createEvent method when the status is not 201.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void createEventPostsJsonAndReturnsTrueForCreatedStatus() throws Exception {
    responseStatus = 201;

    boolean created = api.createEvent("event-1", "Concert", "Trondheim", EVENT_TIME);

    assertTrue(created);
    assertEquals("POST", lastMethod);
    assertEquals("/events", lastPath);
    assertEquals("application/json", lastContentType);
    assertEquals("test-token", lastAuthorization);
    assertTrue(lastBody.contains("\"eventId\":\"event-1\""));
  }

  /**
   * Tests the createEvent method when the status is not 201.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void createEventReturnsFalseForNonCreatedStatus() throws Exception {
    responseStatus = 400;

    assertFalse(api.createEvent("event-1", "Concert", "Trondheim", EVENT_TIME));
    assertEquals("POST", lastMethod);
  }

  /**
   * Tests the getEventById method.
   *
   * @throws Exception if an error occurs during the test.
   */

  @Test
  void getEventByIdReturnsDeserializedEvent() throws Exception {
    responseBody = EVENT_JSON;

    EventDto event = api.getEventById("event-1");

    assertEquals("event-1", event.eventId());
    assertEquals("Concert", event.title());
    assertEquals("GET", lastMethod);
    assertEquals("/events/event-1", lastPath);
  }

  /**
   * Tests the getEventById method when the status is not 200.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void getEventByIdReturnsNullForNonOkStatus() throws Exception {
    responseStatus = 404;

    assertNull(api.getEventById("missing"));
    assertEquals("GET", lastMethod);
    assertEquals("/events/missing", lastPath);
  }

  /**
   * Tests the deleteEventById method when the status is not 204.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void deleteEventByIdReturnsTrueForNoContentStatus() throws Exception {
    responseStatus = 204;

    assertTrue(api.deleteEventById("event-1"));
    assertEquals("DELETE", lastMethod);
    assertEquals("/events/event-1", lastPath);
  }

  /**
   * Tests the deleteEventById method when the status is not 204.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void deleteEventByIdReturnsFalseForOtherStatuses() throws Exception {
    responseStatus = 404;

    assertFalse(api.deleteEventById("missing"));
    assertEquals("DELETE", lastMethod);
  }

  /**
   * Tests the updateEventById method when the status is not 204.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void updateEventByIdPutsJsonAndReturnsTrueForNoContentStatus() throws Exception {
    responseStatus = 204;

    boolean updated = api.updateEventById("event-1", "Concert", "Trondheim", EVENT_TIME);

    assertTrue(updated);
    assertEquals("PUT", lastMethod);
    assertEquals("/events/event-1", lastPath);
    assertEquals("application/json", lastContentType);
    assertTrue(lastBody.contains("\"eventId\":\"event-1\""));
    assertTrue(lastBody.contains("\"title\":\"Concert\""));
  }

  /**
   * Tests the updateEventById method when the status is not 204.
   *
   * @throws Exception if an error occurs during the test.
   */
  @Test
  void updateEventByIdReturnsFalseForOtherStatuses() throws Exception {
    responseStatus = 400;

    assertFalse(api.updateEventById("event-1", "Concert", "Trondheim", EVENT_TIME));
    assertEquals("PUT", lastMethod);
  }
}
