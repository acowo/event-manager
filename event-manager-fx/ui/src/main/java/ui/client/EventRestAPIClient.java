package ui.client;

import event.Event;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class EventRestAPIClient {
  static String defaultBaseUrl = "http://localhost:8080/api/events";
  static HttpClient client = HttpClient.newHttpClient();

  public static List<Event> getAllEvents() {
    //First build the request
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/api/events")).GET().build();

    //HttpResponse<String> response = client.send(request, )
    return null;
  }



}
