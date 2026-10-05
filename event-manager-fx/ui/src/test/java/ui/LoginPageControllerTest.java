package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import javafx.scene.Parent;


import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.NodeQueryUtils.isVisible;

public class LoginPageControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/loginPage.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testWindowLoads() {
        // Simple test to check if the scene initialized properly
        verifyThat("#loginPage_usernameField", NodeMatchers.isVisible());
    }

    @Test
    public void testInvalidUserLogin() {
        clickOn("#loginPage_usernameField").write("invalid");
        clickOn("#loginPage_passwordField").write("invalid");

        clickOn("#loginPage_loginButton");
        verifyThat("#loginPage_infotextField", hasText("An error has occured"));
    }

    @Test
    public void testInvalidUserRegistration() {
        clickOn("#loginPage_usernameField").write("ola");
        clickOn("#loginPage_passwordField").write("123");

        clickOn("#loginPage_registerButton");
        verifyThat("#loginPage_infotextField", hasText("A user by that name already exists"));
    }

    @Test
    public void testValidLogin() throws InterruptedException {
        clickOn("#loginPage_usernameField").write("ola");
        clickOn("#loginPage_passwordField").write("banan");

        clickOn("#loginPage_loginButton");

        //Verifying that the scene changed by checking that a node from the calendar view is present
        verifyThat("#yearLabel", isVisible());
    }

}