package ui.client;

import event.Event;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.EventDto;

public class EventRestAPIClient {
  static String defaultBaseUrl = "http://localhost:8080/api/events";
  static HttpClient client = HttpClient.newHttpClient();
  static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  public static List<Event> getAllEvents() throws IOException, InterruptedException {
    //First build the request
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(defaultBaseUrl)).GET().build();
    //Send the request
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
    //Returned if error
    return Collections.emptyList();
  }

  public static boolean createEvent(Event domainEvent) throws IOException, InterruptedException {
    //Firstly need to convert the event using the DTO, then write it as a json string.
    EventDto eventDto = EventDto.from(domainEvent);
    String jsonBody = objectMapper.writeValueAsString(eventDto);

    //Build the request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(defaultBaseUrl))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    //Send the request
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 201) {
      return true;
    } else {
      return false;
    }
  }

  public static Event getEventById(String id) throws IOException, InterruptedException {
    //First build the request
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(defaultBaseUrl + "/" + id)).GET().build();
    //Send the request
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() == 200) {
      EventDto eventDto = objectMapper.readValue(response.body(), EventDto.class);
      return eventDto.toEvent();
    } else {
      return null;
    }
  }

  public static boolean deleteEventById(String id) throws IOException, InterruptedException {
    //First build the request
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(defaultBaseUrl + "/" + id)).DELETE().build();
    //Send the request
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() == 204) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean updateEventById(String id, Event domainEvent) throws IOException, InterruptedException {
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
