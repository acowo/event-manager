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

public class LoginPageController {
    @FXML
    private TextField loginPage_usernameField;

    @FXML
    private TextField loginPage_passwordField;

    @FXML
    private Button loginPage_registerButton;

    @FXML
    private Button loginPage_loginButton;

    @FXML
    private Text loginPage_infotextField;

    private UserManager manager = new UserManager(new UserFileManager());

    /**
   * Handles the controller logic needed when a user presses the login button. Uses imports from the
   * Core and persistence modules. This function does not return a value.
     */
    @FXML
    private void handleLogin() throws Exception {
        String username = loginPage_usernameField.getText();
        String password = loginPage_passwordField.getText();

        if (isNotEmpty(username, password)) {
            if (manager.login(username, password)) {
                changeScene();
            } else {
                loginPage_infotextField.setText("An error has occured");
                loginPage_infotextField.setFill(Color.RED);
            }
        }
    }

    /**
   * Handles the controller logic needed when a user presses the register button. Uses imports from
   * the Core and persistence modules. This function does not return a value.
     */
    @FXML
    private void handleRegistration() throws Exception {
        String username = loginPage_usernameField.getText();
        String password = loginPage_passwordField.getText();

        if (isNotEmpty(username, password)) {
            if (manager.register(username, password)) {
                changeScene();
            } else {
                loginPage_infotextField.setText("A user by that name already exists");
                loginPage_infotextField.setFill(Color.RED);
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
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/Calendar.fxml")));
            Stage stage = (Stage) loginPage_registerButton.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            System.err.println(e);
        }


    }

    /**
     * Handles the controller logic needed to check whether a user has populated the required fields.
   * Mainly used to save resources by preventing unnecessary calls to heavier functions when not
   * needed.
   *
     * @param username The string in the username field.
     * @param password The string in the password field.
     * @return True if fields are populated, false otherwise.
     */
    @FXML
    private boolean isNotEmpty(String username, String password) {
        if (username.length() < 1) {
            loginPage_infotextField.setText("Please enter a username");
            loginPage_infotextField.setFill(Color.RED);
            return false;
        }

        if (password.length() < 1) {
            loginPage_infotextField.setText("Please enter a password");
            loginPage_infotextField.setFill(Color.RED);
            return false;
        }

        return true;
    }

}