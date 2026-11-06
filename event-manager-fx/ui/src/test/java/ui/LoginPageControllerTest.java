
package ui;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.NodeQueryUtils.isVisible;

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

/** UI tests for {@link LoginPageController}. */
public class LoginPageControllerTest extends ApplicationTest {

  @TempDir
  static Path tempDir;

  /**
   * Starts the login page using a temporary user file.
   *
   * @param stage the JavaFX stage used for rendering
   * @throws IOException if the FXML file cannot be loaded
   */
  @Override
  public void start(Stage stage) throws IOException {
    Path userFile =
        tempDir.resolve("userdata-" + java.util.UUID.randomUUID() + ".json");

    UserFileManager repository = new UserFileManager(userFile.toString());
    UserManager manager = new UserManager(repository);

    manager.register("ola", "banan");

    FXMLLoader loader =
        new FXMLLoader(
            Objects.requireNonNull(getClass().getResource("/ui/loginPage.fxml")));

    Parent root = loader.load();

    LoginPageController controller = loader.getController();
    controller.setUserManager(manager);
    controller.setCalendarView("/ui/TestCalendar.fxml");

    stage.setScene(new Scene(root));
    stage.show();
  }

  /** Tests that the login page loads correctly. */
  @Test
  void testWindowLoads() {
    verifyThat("#loginPageUsernameField", NodeMatchers.isVisible());
  }

  /** Tests that invalid login credentials display an error message. */
  @Test
  public void testInvalidUserLogin() {
    clickOn("#loginPageUsernameField").write("invalid");
    clickOn("#loginPagePasswordField").write("invalid");

    clickOn("#loginPageLoginButton");

    verifyThat("#loginPageInfotextField", hasText("An error has occured"));
  }

  /** Tests that registering an existing username displays an error message. */
  @Test
  public void testInvalidUserRegistration() {
    clickOn("#loginPageUsernameField").write("ola");
    clickOn("#loginPagePasswordField").write("123");

    clickOn("#loginPageRegisterButton");

    verifyThat(
        "#loginPageInfotextField",
        hasText("A user by that name already exists"));
  }

  /** Tests that a successful login switches to the calendar view. */
  @Test
  public void testValidLogin() {
    clickOn("#loginPageUsernameField").write("ola");
    clickOn("#loginPagePasswordField").write("banan");

    clickOn("#loginPageLoginButton");

    verifyThat("#yearLabel", isVisible());
  }

  /** Clears the user session after each test. */
  @AfterEach
  void cleanUp() {
    UserSession.logout();
  }
}
