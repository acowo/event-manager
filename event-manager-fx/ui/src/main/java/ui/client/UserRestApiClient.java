package ui.client;

import com.google.gson.Gson;
import dto.request.LoginRegisterRequestDto;
import dto.response.LoginRegisterResponseDto;
import dto.response.UserGetEventsResponseDto;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import ui.auth.ClientSession;

/**
 * Client for the user endpoints of the REST API.
 */
public class UserRestApiClient {

  private final HttpClient httpClient;
  private final Gson gson;
  private String serverUri;

  /**
   * Creates a client that talks to the API on localhost.
   *
   * @param uri URI for the server
   */
  public UserRestApiClient(String uri) {
    this.httpClient = HttpClient.newHttpClient();
    this.gson = new Gson();
    this.serverUri = uri;
  }

  /**
   * Logs in a user.
   *
   * @param username the username
   * @param password the password
   * @return the username and whether the login succeeded
   */
  public LoginRegisterResponseDto login(String username, String password)
      throws IOException, InterruptedException {
    LoginRegisterRequestDto loginRequestDto = new LoginRegisterRequestDto(username, password);
    String json = gson.toJson(loginRequestDto);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(serverUri + "/login"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), LoginRegisterResponseDto.class);
  }

  /**
   * Registers a new user.
   *
   * @param username the username for the new user
   * @param password the password for the new user
   * @return the username and whether the registration succeeded
   * @throws IOException if the request fails
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public LoginRegisterResponseDto register(String username, String password)
      throws IOException, InterruptedException {
    LoginRegisterRequestDto registerRequestDto = new LoginRegisterRequestDto(username, password);
    String json = gson.toJson(registerRequestDto);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(serverUri + "/register"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), LoginRegisterResponseDto.class);
  }

  /**
   * Opts the current user set in ClientSession into an event.
   *
   * @param eventId the event to opt into
   * @return {@code true} if the user successfully opted in;
   *        {@code false} if the user was already
   *        opted in or something else went wrong
   * @throws IOException if the request fails
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public boolean optIn(String eventId) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(serverUri + "/me/events/" + eventId))
        .header("Authorization", ClientSession.getAuthToken())
        .PUT(HttpRequest.BodyPublishers.noBody())
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return response.statusCode() == 204;
  }

  /**
   * Opts the current user set in ClientSession out of an event.
   *
   * @param eventId the event to opt out of
   * @return {@code true} if the user successfully opted out;
   *        {@code false} if the user was not
   *        opted in or something else went wrong
   * @throws IOException if the request fails
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public boolean optOut(String eventId) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(serverUri + "/me/events/" + eventId))
        .header("Authorization", ClientSession.getAuthToken())
        .DELETE()
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return response.statusCode() == 204;
  }

  /**
   * Gets all events that the current user in ClientSession is attending.
   *
   * @return a map of all events the user is attending with matching admin value.
   * @throws IOException if the request fails
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public Map<String, Boolean> getUserEvents() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(serverUri + "/me/events"))
        .header("Authorization", ClientSession.getAuthToken())
        .GET()
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    UserGetEventsResponseDto dto = gson.fromJson(response.body(), UserGetEventsResponseDto.class);
    if (response.statusCode() != 200) {
      return Map.of(); // Maybe add a check later on for unauthorized error code?
    }
    return dto.events();
  }

  /**
   * Deletes a user.
   *
   * @return {@code true} if user is successfully deleted;
   *        {@code false} if else.
   */
  public boolean deleteUser() {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(serverUri + "/me"))
        .header("Authorization", ClientSession.getAuthToken())
        .DELETE()
        .build();

    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        return false;
      }
      return true;
    } catch (Exception e) {
      return false;
    }

  }

}
