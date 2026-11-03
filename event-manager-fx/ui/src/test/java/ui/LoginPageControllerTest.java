package ui;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.NodeQueryUtils.isVisible;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    /**
     * Starts the stage so the tests can take place.
   *
   * @param stage the JavaFX stage used for rendering
   * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/loginPage.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

  /** Tests that the login page window loads by checking if an element is present. */
    @Test
    void testWindowLoads() {
        // Simple test to check if the scene initialized properly
        verifyThat("#loginPage_usernameField", NodeMatchers.isVisible());
    }

    /**
   * Tests that the login page shows an error message when invalid credentials are used to try to
   * log in.
     */
    @Test
    public void testInvalidUserLogin() {
        clickOn("#loginPage_usernameField").write("invalid");
        clickOn("#loginPage_passwordField").write("invalid");

        clickOn("#loginPage_loginButton");
        verifyThat("#loginPage_infotextField", hasText("An error has occured"));
    }

    /**
   * Tests that the login page shows an error message when a user tries registering with a username
   * that has already been used.
     */
    @Test
    public void testInvalidUserRegistration() {
        clickOn("#loginPage_usernameField").write("ola");
        clickOn("#loginPage_passwordField").write("123");

        clickOn("#loginPage_registerButton");
        verifyThat("#loginPage_infotextField", hasText("A user by that name already exists"));
    }

  /** Tests that the controller correctly switches to the calendar page after a successful login. */
    @Test
    public void testValidLogin() throws InterruptedException {
        clickOn("#loginPage_usernameField").write("ola");
        clickOn("#loginPage_passwordField").write("banan");

        clickOn("#loginPage_loginButton");

        //Verifying that the scene changed by checking that a node from the calendar view is present
        verifyThat("#yearLabel", isVisible());
    }

}