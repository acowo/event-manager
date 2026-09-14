package ui.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;


public class loginPageController {
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

    //Handles registration by checking return value to see if it succeeded or not.
    //Firstly goes through usersystem, then that goes through filehandling.
    @FXML
    private void handleLogin() throws Exception {

    }

    //Handles registration by checking return value to see if it succeeded or not.
    //Firstly goes through usersystem, then that goes through filehandling.
    @FXML
    private void handleRegistration() throws Exception {

    }

    @FXML
    private void changeScene() throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/movieproject/register.fxml")); //Change this path when other scene has been made
            Stage stage = (Stage) loginPage_registerButton.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            System.err.println(e);
        }


    }


}
