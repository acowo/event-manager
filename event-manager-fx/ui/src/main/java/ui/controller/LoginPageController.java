package ui.controller;

//FXML imports
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

//User imports
import user.User;
import user.UserSession;

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

    /**
     * Handles the controller logic needed when a user presses the login button.
     * Uses imports from the Core and persistence modules. This function does not return a value.
     */
    @FXML
    private void handleLogin() throws Exception {
        String username = loginPage_usernameField.getText();
        String password = loginPage_passwordField.getText();




    }

    /**
     * Handles the controller logic needed when a user presses the register button.
     * Uses imports from the Core and persistence modules. This function does not return a value.
     */
    @FXML
    private void handleRegistration() throws Exception {
        String username = loginPage_usernameField.getText();
        String password = loginPage_passwordField.getText();


        if (User.userExists(username)) {
            loginPage_infotextField.setText("A user by this name already exists.");
            loginPage_infotextField.setFill(Color.RED);
        } else {
            User.createUser(username, password);
            UserSession.setUser(username,password);
            changeScene();
        }
    }


    /**
     * Handles the controller logic needed when a user successfully creates a session through a valid account.
     * Switches the scene away from the login page to the calendar view, can be changed in the future.
     * This function does not return a value.
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


}
