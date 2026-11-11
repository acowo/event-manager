package ui;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.NodeQueryUtils.isVisible;

import dto.response.LoginRegisterResponseDto;
import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import ui.auth.ClientSession;
import ui.client.EventRestApiClient;
import ui.client.UserRestApiClient;

/** UI tests for {@link ui.controller.LoginPageController}. */
public class LoginPageControllerTest extends ApplicationTest {
  private MockedConstruction<UserRestApiClient> userClients;
  private MockedConstruction<EventRestApiClient> eventClients;
  private UserRestApiClient loginClient;

  /**
   * Starts the stage so the tests can take place.
   *
   * @param stage the JavaFX stage used for rendering
   * @throws IOException if the FXML file cannot be loaded
   */
  @Override
  public void start(Stage stage) throws IOException {
    userClients = mockConstruction(UserRestApiClient.class);
    eventClients = mockConstruction(EventRestApiClient.class);

    Parent root =
        FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/loginPage.fxml")));
    loginClient = userClients.constructed().get(0);
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Closes the mocked clients. Runs on the JavaFX thread, the same thread that created them.
   **/
  @Override
  public void stop() {
    userClients.close();
    eventClients.close();
    ClientSession.clearAuthToken();
  }

  /**
   * Tests that the login page window loads by checking if an element is present.
   **/
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
  public void testInvalidUserLogin() throws IOException, InterruptedException {
    when(loginClient.login("invalid", "invalid"))
        .thenReturn(new LoginRegisterResponseDto("invalid", false));

    clickOn("#loginPageUsernameField").write("invalid");
    clickOn("#loginPagePasswordField").write("invalid");

    clickOn("#loginPageLoginButton");
    verify(loginClient).login("invalid", "invalid");
    verifyThat("#loginPageInfotextField", hasText("An error has occurred"));
  }

  /**
   * Tests that the login page shows an error message when a user tries registering with a id
   * that has already been used.
   */
  @Test
  public void testInvalidUserRegistration() throws IOException, InterruptedException {
    when(loginClient.register("ola", "123"))
        .thenReturn(new LoginRegisterResponseDto("ola", false));

    clickOn("#loginPageUsernameField").write("ola");
    clickOn("#loginPagePasswordField").write("123");

    clickOn("#loginPageRegisterButton");
    verify(loginClient).register("ola", "123");
    verifyThat("#loginPageInfotextField", hasText("A user by that name already exists"));
  }

  /**
   * Tests that an info text is shown, and the server is not called, when the id is empty.
   **/
  @Test
  void testEmptyUsername_doesNotCallServer() throws IOException, InterruptedException {
    clickOn("#loginPagePasswordField").write("123");
    clickOn("#loginPageLoginButton");

    verifyThat("#loginPageInfotextField", hasText("Please enter a id"));
    verify(loginClient, never()).login(any(), any());
  }

  /** Tests that the controller correctly switches to the calendar page after a successful login. */
  @Test
  public void testValidLogin() throws InterruptedException, IOException {
    when(loginClient.login("ola", "banan"))
        .thenReturn(new LoginRegisterResponseDto("ola", true));

    clickOn("#loginPageUsernameField").write("ola");
    clickOn("#loginPagePasswordField").write("banan");

    clickOn("#loginPageLoginButton");

    // Verifying that the scene changed by checking that a node from the calendar view is present
    verify(loginClient).login("ola", "banan");
    verifyThat("#yearLabel", isVisible());
  }
}
