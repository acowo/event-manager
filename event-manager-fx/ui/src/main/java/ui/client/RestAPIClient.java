package ui.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import dto.request.LoginRegisterRequestDTO;
import dto.response.LoginRegisterResponseDTO;

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
}
