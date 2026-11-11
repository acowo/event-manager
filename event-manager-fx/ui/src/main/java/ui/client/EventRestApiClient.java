package ui.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.EventDto;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import ui.auth.ClientSession;

/**
 * Static client for the event endpoints of the REST API.
 */
public class EventRestApiClient {
  private String serverUri;
  static HttpClient client = HttpClient.newHttpClient();
  static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  /**
   * Constructor that sets the URI.
   *
   * @param uri URI that will be used to send requests.
   */
  public EventRestApiClient(String uri) {
    this.serverUri = uri;
  }

  /**
   * Gets a list of all the events.
   *
   * @return a {@link List} of all events in EventDto form, or an empty list if the request failed
   * @throws IOException if the request fails
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public List<EventDto> getAllEvents() throws IOException, InterruptedException {
    // First build the request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(serverUri)).GET().build();
    // Send the request
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      List<EventDto> eventDtos = objectMapper.readValue(
              response.body(),
              new TypeReference<List<EventDto>>() {}
      );
      return eventDtos;
    } else {
      System.err.println("Failed to fetch events. Status: " + response.statusCode());
    }
    // Returned if error
    return Collections.emptyList();
  }

  /**
   * Creates a new event by sending an HTTP POST request to the server.
   *
   * @param id the id of the event
   * @param title the title of the event
   * @param place the location where the event takes place
   * @param datetime the date and time when the event starts
   * @return {@code true} if the event was successfully created (HTTP 201); {@code false} otherwise
   * @throws IOException if the request fails or the payload cannot be serialized
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public boolean createEvent(
      String id, String title, String place, LocalDateTime datetime)
      throws IOException, InterruptedException {
    // Firstly need to create a dto, then serialize it.
    EventDto dto = new EventDto(id, title, place, datetime);
    String jsonBody = objectMapper.writeValueAsString(dto);

    // Build the request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(serverUri))
        .header("Content-Type", "application/json")
        .header("Authorization", ClientSession.getAuthToken())
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
  public EventDto getEventById(String id) throws IOException, InterruptedException {
    // First build the request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(serverUri + "/" + id)).GET().build();
    // Send the request
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() == 200) {
      EventDto eventDto = objectMapper.readValue(response.body(), EventDto.class);
      return eventDto;
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
  public boolean deleteEventById(String id) throws IOException, InterruptedException {
    // First build the request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(serverUri + "/" + id)).DELETE().build();
    // Send the request
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() == 204) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Updates an existing event by its ID by sending an HTTP PUT request to the server.
   *
   * @param id the ID of the event to update
   * @param title the updated title of the event
   * @param place the updated location where the event takes place
   * @param dateTime the updated date and time when the event starts
   * @return {@code true} if the event was successfully updated (HTTP 204); {@code false} otherwise
   * @throws IOException if the request fails or the payload cannot be serialized
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public boolean updateEventById(
      String id, String title, String place, LocalDateTime dateTime)
      throws IOException, InterruptedException {
    EventDto eventDto = new EventDto(id, title, place, dateTime);
    String jsonBody = objectMapper.writeValueAsString(eventDto);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(serverUri + "/" + id))
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
