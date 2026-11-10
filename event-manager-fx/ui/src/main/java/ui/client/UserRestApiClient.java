package ui.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.google.gson.Gson;
import dto.request.LoginRegisterRequestDto;
import dto.response.LoginRegisterResponseDto;
import dto.response.UserGetEventsResponseDto;
import ui.auth.ClientSession;

public class UserRestApiClient {

  private final HttpClient httpClient;
  private final Gson gson;
  private static final String baseUrl = "http://localhost:8080/api/users";

  public UserRestApiClient() {
    this.httpClient = HttpClient.newHttpClient();
    this.gson = new Gson();
  }

  public LoginRegisterResponseDto login(String username, String password) throws IOException, InterruptedException {
    LoginRegisterRequestDto loginRequestDTO = new LoginRegisterRequestDto(username, password);
    String json = gson.toJson(loginRequestDTO);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/login"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), LoginRegisterResponseDto.class);
  }

  public LoginRegisterResponseDto register(String username, String password) throws IOException, InterruptedException {
    LoginRegisterRequestDto registerRequestDTO = new LoginRegisterRequestDto(username, password);
    String json = gson.toJson(registerRequestDTO);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/register"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), LoginRegisterResponseDto.class);
  }

  /**
   * Opts the current user set in ClientSession into an event.
   * @param eventId
   * @return true if user successfully opted in an event; false if the user was already opted in
   * or someting else went wrong.
   *
   * @throws IOException
   * @throws InterruptedException
   */
  public boolean optIn(String eventId) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/me/events/" + eventId))
        .header("Authorization", ClientSession.getAuthToken())
        .PUT(HttpRequest.BodyPublishers.noBody())
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return response.statusCode() == 204;
  }

  public boolean optOut(String eventId) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/me/events/" + eventId))
        .header("Authorization", ClientSession.getAuthToken())
        .PUT(HttpRequest.BodyPublishers.noBody())
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return response.statusCode() == 204;
  }

  public Map<String, Boolean> getUserEvents() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/me/events"))
        .header("Authorization", ClientSession.getAuthToken())
        .GET()
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    UserGetEventsResponseDto dto = gson.fromJson(response.body(), UserGetEventsResponseDto.class);
    if (response.statusCode() != 200){
      return Map.of(); //Maybe add a check later on for unauthorized error code?
    }
    return dto.events();
  }

}
