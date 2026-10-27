
package ui;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import persistence.UserFileManager;
import ui.controller.LoginPageController;
import user.UserManager;
import user.UserSession;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.NodeQueryUtils.isVisible;


public class LoginPageControllerTest extends ApplicationTest {
    /**
     * Starts the stage so the tests can take place.
     * @param stage
     * @throws IOException
     */

    @TempDir 
    static Path tempDir;

    @Override 
    public void start(Stage stage) throws IOException {
        Path userFile = tempDir.resolve(
            "userdata-" + java.util.UUID.randomUUID() + ".json"
        );

        UserFileManager repository = new UserFileManager(
            userFile.toString()
        );

        UserManager manager = new UserManager(repository);

        manager.register("ola", "banan");

        FXMLLoader loader = new FXMLLoader(
            Objects.requireNonNull(getClass().getResource("/ui/loginPage.fxml"))

        );

        Parent root = loader.load();

        LoginPageController controller = loader.getController();
        controller.setUserManager(manager);
        controller.setCalendarView("/ui/TestCalendar.fxml");

        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Tests that the login page window loads by checking if an element is present.
     */
    @Test
    void testWindowLoads() {
        // Simple test to check if the scene initialized properly
        verifyThat("#loginPage_usernameField", NodeMatchers.isVisible());
    }

    /**
     * Tests that the login page shows an error message when invalid credentials are used to try to log in.
     */
    @Test
    public void testInvalidUserLogin() {
        clickOn("#loginPage_usernameField");
        write("ola");

        clickOn("#loginPage_passwordField");
        write("123");

        verifyThat("#loginPage_usernameField", hasText("ola"));
        verifyThat("#loginPage_passwordField", hasText("123"));

        clickOn("#loginPage_registerButton");

        verifyThat(
            "#loginPage_infotextField",
            hasText("A user by that name already exists")
        );
    }

    /**
     * Tests that the login page shows an error message when a user tries registering with a username that has already been used.
     */
    @Test
    public void testInvalidUserRegistration() {
        clickOn("#loginPage_usernameField").write("ola");
        clickOn("#loginPage_passwordField").write("123");

        clickOn("#loginPage_registerButton");
        verifyThat("#loginPage_infotextField", hasText("A user by that name already exists"));
    }

    /**
     * Tests that the controller correctly switches to the calendar page after a successful login.
     */
    @Test
    public void testValidLogin() throws InterruptedException {
        clickOn("#loginPage_usernameField").write("ola");
        clickOn("#loginPage_passwordField").write("banan");

        clickOn("#loginPage_loginButton");

        //Verifying that the scene changed by checking that a node from the calendar view is present
        verifyThat("#yearLabel", isVisible());
    }

    @AfterEach
    void cleanUp() {
        UserSession.logout();
    }

}