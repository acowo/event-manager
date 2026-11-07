
package ui.controller;

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
import user.UserManager;
import user.UserRepository;

/** Controller for the login and registration page. */
public class LoginPageController {

  @FXML private TextField loginPageUsernameField;

  @FXML private TextField loginPagePasswordField;

  @FXML private Button loginPageRegisterButton;

  @FXML private Button loginPageLoginButton;

  @FXML private Text loginPageInfotextField;

  private final UserManager manager;

  private final String calendarView;


  /**
   * Default constructor for LoginPageController,
   * initializing with a new UserManager and default calendar view.
   * The default calendar view is set to "/ui/Calendar.fxml".
   */
  public LoginPageController() {
    this(new UserFileManager(), "/ui/Calendar.fxml");
    
  }

  /**
   * Constructor for LoginPageController with specified UserManager and calendar view.
   *
   * @param userRepository the user repository for managing user data
   * @param calendarView path to the calendar FXML resource
   */
  public LoginPageController(UserRepository userRepository, String calendarView) {
    this.manager = new UserManager(Objects.requireNonNull(userRepository));
    this.calendarView = Objects.requireNonNull(calendarView);
  }

  /**
   * Handles the controller logic needed when a user presses the login button.
   * Uses imports from the Core and persistence modules.
   *
   * @throws IOException if the calendar view cannot be loaded
   */
  @FXML
  public void handleLogin() throws IOException {
    String username = loginPageUsernameField.getText();
    String password = loginPagePasswordField.getText();

    if (isNotEmpty(username, password)) {
      if (manager.login(username, password)) {
        changeScene();
      } else {
        loginPageInfotextField.setText("An error has occured");
        loginPageInfotextField.setFill(Color.RED);
      }
    }
  }

  /**
   * Handles the controller logic needed when a user presses the register button.
   *
   * @throws IOException if the calendar view cannot be loaded
   */
  @FXML
  public void handleRegistration() throws IOException {
    String username = loginPageUsernameField.getText();
    String password = loginPagePasswordField.getText();

    if (isNotEmpty(username, password)) {
      if (manager.register(username, password)) {
        changeScene();
      } else {
        loginPageInfotextField.setText("A user by that name already exists");
        loginPageInfotextField.setFill(Color.RED);
      }
    }
  }

  /**
   * Switches to the configured calendar view after successful authentication.
   *
   * @throws IOException if the view cannot be loaded or the window is invalid
   */
  @FXML
  private void changeScene() throws IOException {
    URL resource = LoginPageController.class.getResource(calendarView);

    if (resource == null) {
      throw new IOException("Calendar view not found: " + calendarView);
    }

    Parent root = FXMLLoader.load(resource);

    Window window = loginPageRegisterButton.getScene().getWindow();

    if (window instanceof Stage) {
      Stage stage = (Stage) window;
      stage.getScene().setRoot(root);
      return;
    }

    throw new IOException("Window is not a Stage");
  }

  /**
   * Checks whether the username and password fields are populated.
   *
   * @param username the username entered
   * @param password the password entered
   * @return true if both fields are populated, false otherwise
   */
  @FXML
  private boolean isNotEmpty(String username, String password) {
    if (username.length() < 1) {
      loginPageInfotextField.setText("Please enter a username");
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
