package ui.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.EventDto;
import event.Event;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

/**
 * Static client for the event endpoints of the REST API.
 */
public class EventRestApiClient {
  static String defaultBaseUrl = "http://localhost:8080/api/events";
  static HttpClient client = HttpClient.newHttpClient();
  static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  /**
   * Gets a list of all the events.
   *
   * @return a {@link List} of all events, or an empty list if the request failed
   * @throws IOException if the request fails
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public static List<Event> getAllEvents() throws IOException, InterruptedException {
    // First build the request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(defaultBaseUrl)).GET().build();
    // Send the request
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      List<EventDto> eventDtos = objectMapper.readValue(
              response.body(),
              new TypeReference<List<EventDto>>() {}
      );
      return eventDtos.stream()
          .map(EventDto::toEvent)
          .toList();
    } else {
      System.err.println("Failed to fetch events. Status: " + response.statusCode());
    }
    // Returned if error
    return Collections.emptyList();
  }

  /**
   * Creates a new event.
   *
   * @param domainEvent the event to create
   * @return {@code true} if the event was created; {@code false} otherwise
   * @throws IOException if the request fails or the event cannot be serialized
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public static boolean createEvent(Event domainEvent) throws IOException, InterruptedException {
    // Firstly need to convert the event using the DTO, then write it as a json string.
    EventDto eventDto = EventDto.from(domainEvent);
    String jsonBody = objectMapper.writeValueAsString(eventDto);

    // Build the request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(defaultBaseUrl))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    // Send the request
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 201) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets an event by its ID.
   *
   * @param id the ID of the event
   * @return the event, or {@code null} if it was not found or the request failed
   * @throws IOException if the request fails or the response cannot be parsed
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public static Event getEventById(String id) throws IOException, InterruptedException {
    // First build the request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(defaultBaseUrl + "/" + id)).GET().build();
    // Send the request
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() == 200) {
      EventDto eventDto = objectMapper.readValue(response.body(), EventDto.class);
      return eventDto.toEvent();
    } else {
      return null;
    }
  }

  /**
   * Deletes an event by its ID.
   *
   * @param id the ID of the event
   * @return {@code true} if the event was deleted; {@code false} otherwise
   * @throws IOException if the request fails
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public static boolean deleteEventById(String id) throws IOException, InterruptedException {
    // First build the request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(defaultBaseUrl + "/" + id)).DELETE().build();
    // Send the request
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() == 204) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Updates an event by its ID.
   *
   * @param id the ID of the event to update
   * @param domainEvent the new event data
   * @return {@code true} if the event was updated; {@code false} otherwise
   * @throws IOException if the request fails or the event cannot be serialized
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public static boolean updateEventById(String id, Event domainEvent)
      throws IOException, InterruptedException {
    EventDto eventDto = EventDto.from(domainEvent);
    String jsonBody = objectMapper.writeValueAsString(eventDto);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(defaultBaseUrl + "/" + id))
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() == 204) {
      return true;
    } else {
      return false;
    }
  }
}
