package ui.controller;

import dto.response.LoginRegisterResponseDto;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import persistence.UserFileManager;
import ui.auth.ClientSession;
import ui.client.UserRestApiClient;
import user.UserManager;

/** Controller for the login and registration page. */
public class LoginPageController {
  @FXML private TextField loginPageUsernameField;

  @FXML private TextField loginPagePasswordField;

  @FXML private Button loginPageRegisterButton;

  @FXML private Button loginPageLoginButton;

  @FXML private Text loginPageInfotextField;

  private UserManager manager = new UserManager(new UserFileManager());

  private final UserRestApiClient apiClient = new UserRestApiClient();

  /**
   * Handles the controller logic needed when a user presses the login button. Uses imports from the
   * Core and persistence modules. This function does not return a value.
   */
  @FXML
  public void handleLogin() throws IOException, InterruptedException {
    String username = loginPageUsernameField.getText();
    String password = loginPagePasswordField.getText();

    if (isNotEmpty(username, password)) {
      LoginRegisterResponseDto loginResponse = apiClient.login(username, password);
      if (loginResponse.success()) {
        ClientSession.setAuthToken(loginResponse.id());
        changeScene();
      } else {
        loginPageInfotextField.setText("An error has occured");
        loginPageInfotextField.setFill(Color.RED);
      }
    }
  }

  /**
   * Handles the controller logic needed when a user presses the register button. Uses imports from
   * the Core and persistence modules. This function does not return a value.
   */
  @FXML
  public void handleRegistration() throws IOException, InterruptedException {
    String username = loginPageUsernameField.getText();
    String password = loginPagePasswordField.getText();

    if (isNotEmpty(username, password)) {
      LoginRegisterResponseDto registerResponse = apiClient.register(username, password);
      if (registerResponse.success()) {
        ClientSession.setAuthToken(registerResponse.id());
        changeScene();
      } else {
        loginPageInfotextField.setText("A user by that name already exists");
        loginPageInfotextField.setFill(Color.RED);
      }
    }
  }

  /**
   * Handles the controller logic needed when a user successfully creates a session through a valid
   * account. Switches the scene away from the login page to the calendar view, can be changed in
   * the future. This function does not return a value.
   */
  @FXML
  private void changeScene() throws IOException {
    URL resource = LoginPageController.class.getResource("/ui/Calendar.fxml");
    Parent root = FXMLLoader.load(Objects.requireNonNull(resource));
    Window window = loginPageRegisterButton.getScene().getWindow();
    if (window instanceof Stage) {
      Stage stage = (Stage) window;
      stage.getScene().setRoot(root);
      return;
    }

    throw new IOException("Window is not a Stage");
  }

  /**
   * Handles the controller logic needed to check whether a user has populated the required fields.
   * Mainly used to save resources by preventing unnecessary calls to heavier functions when not
   * needed.
   *
   * @param username The string in the id field.
   * @param password The string in the password field.
   * @return True if fields are populated, false otherwise.
   */
  @FXML
  private boolean isNotEmpty(String username, String password) {
    if (username.length() < 1) {
      loginPageInfotextField.setText("Please enter a id");
      loginPageInfotextField.setFill(Color.RED);
      return false;
    }

    if (password.length() < 1) {
      loginPageInfotextField.setText("Please enter a password");
      loginPageInfotextField.setFill(Color.RED);
      return false;
    }
    return true;
  }
}
