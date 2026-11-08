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

/** UI tests for {@link ui.controller.LoginPageController}. */
public class LoginPageControllerTest extends ApplicationTest {
  /**
   * Starts the stage so the tests can take place.
   *
   * @param stage the JavaFX stage used for rendering
   * @throws IOException if the FXML file cannot be loaded
   */
  @Override
  public void start(Stage stage) throws IOException {
    Parent root =
        FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/loginPage.fxml")));
    stage.setScene(new Scene(root));
    stage.show();
  }

  /** Tests that the login page window loads by checking if an element is present. */
  @Test
  void testWindowLoads() {
    // Simple test to check if the scene initialized properly
    verifyThat("#loginPageUsernameField", NodeMatchers.isVisible());
  }

  /**
   * Tests that the login page shows an error message when invalid credentials are used to try to
   * log in.
   */
  @Test
  public void testInvalidUserLogin() {
    clickOn("#loginPageUsernameField").write("invalid");
    clickOn("#loginPagePasswordField").write("invalid");

    clickOn("#loginPageLoginButton");
    verifyThat("#loginPageInfotextField", hasText("An error has occured"));
  }

  /**
   * Tests that the login page shows an error message when a user tries registering with a id
   * that has already been used.
   */
  @Test
  public void testInvalidUserRegistration() {
    clickOn("#loginPageUsernameField").write("ola");
    clickOn("#loginPagePasswordField").write("123");

    clickOn("#loginPageRegisterButton");
    verifyThat("#loginPageInfotextField", hasText("A user by that name already exists"));
  }

  /** Tests that the controller correctly switches to the calendar page after a successful login. */
  @Test
  public void testValidLogin() throws InterruptedException {
    clickOn("#loginPageUsernameField").write("ola");
    clickOn("#loginPagePasswordField").write("banan");

    clickOn("#loginPageLoginButton");

    // Verifying that the scene changed by checking that a node from the calendar view is present
    verifyThat("#yearLabel", isVisible());
  }
}
