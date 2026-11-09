package ui.client;

import com.google.gson.Gson;
import dto.request.EditUserRequestDto;
import dto.request.LoginRegisterRequestDto;
import dto.response.EditUserResponseDto;
import dto.response.LoginRegisterResponseDto;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import ui.auth.ClientSession;

/**
 * Client for the user and event endpoints of the REST API.
 */
public class RestApiClient {

  private final HttpClient httpClient;
  private final Gson gson;
  private final String baseUrl = "http://localhost:8080/api/users";

  /**
   * Creates a client that talks to the API on localhost.
   */
  public RestApiClient() {
    this.httpClient = HttpClient.newHttpClient();
    this.gson = new Gson();
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
        .uri(URI.create(baseUrl + "/login")).header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json)).build();

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
        .uri(URI.create(baseUrl + "/register")).header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json)).build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), LoginRegisterResponseDto.class);
  }

  // TODO: Add edit user functionality :O
  /**
   * Sends a request to edit a user.
   *
   * @param authToken the token identifying the user
   * @return {@code true} if the edit succeeded
   * @throws IOException if the request fails
   * @throws InterruptedException if the thread is interrupted while waiting for the response
   */
  public boolean updateUserRequest(String authToken)
      throws IOException, InterruptedException {
    EditUserRequestDto editUserRequestDto = new EditUserRequestDto(authToken);
    String json = gson.toJson(editUserRequestDto);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/edit")).header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(json)).build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), EditUserResponseDto.class).success();
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
        .uri(URI.create(baseUrl + "/optIn/" + eventId))
        .header("Authorization", ClientSession.getAuthToken())
        .PUT(HttpRequest.BodyPublishers.noBody()).build();
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
        .uri(URI.create(baseUrl + "/optOut/" + eventId))
        .header("Authorization", ClientSession.getAuthToken())
        .PUT(HttpRequest.BodyPublishers.noBody()).build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return response.statusCode() == 204;
  }
}
