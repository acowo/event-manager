package ui.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import dto.request.EditUserRequestDTO;
import dto.request.LoginRegisterRequestDTO;
import dto.response.EditUserResponseDTO;
import dto.response.LoginRegisterResponseDTO;
import ui.auth.ClientSession;

public class RestAPIClient {

  private final HttpClient httpClient;
  private final Gson gson;
  private final String baseUrl = "http://localhost:8080/api/users";

  public RestAPIClient() {
    this.httpClient = HttpClient.newHttpClient();
    this.gson = new Gson();
  }

  public LoginRegisterResponseDTO login(String username, String password) throws IOException, InterruptedException {
    LoginRegisterRequestDTO loginRequestDTO = new LoginRegisterRequestDTO(username, password);
    String json = gson.toJson(loginRequestDTO);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/login"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), LoginRegisterResponseDTO.class);
  }

  public LoginRegisterResponseDTO register(String username, String password) throws IOException, InterruptedException {
    LoginRegisterRequestDTO registerRequestDTO = new LoginRegisterRequestDTO(username, password);
    String json = gson.toJson(registerRequestDTO);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/register"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), LoginRegisterResponseDTO.class);
  }

  //TODO: Add edit user functionality :O
  public boolean updateUserRequest(String authToken) throws IOException, InterruptedException {
    EditUserRequestDTO editUserRequestDTO = new EditUserRequestDTO(authToken);
    String json = gson.toJson(editUserRequestDTO);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/edit"))
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(json))
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return gson.fromJson(response.body(), EditUserResponseDTO.class).success();
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
        .uri(URI.create(baseUrl + "/optIn/" + eventId))
        .header("Authorization", ClientSession.getAuthToken())
        .PUT(HttpRequest.BodyPublishers.noBody())
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return response.statusCode() == 204;
  }

  public boolean optOut(String eventId) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/optOut/" + eventId))
        .header("Authorization", ClientSession.getAuthToken())
        .PUT(HttpRequest.BodyPublishers.noBody())
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return response.statusCode() == 204;
  }



}
