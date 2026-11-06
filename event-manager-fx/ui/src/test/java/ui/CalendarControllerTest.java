
package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.NodeQueryUtils.isVisible;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import persistence.EventFileManager;
import persistence.UserFileManager;
import ui.controller.CalendarController;
import user.UserManager;
import user.UserSession;

/** Tests the calendar controller and its UI interactions. */
public class CalendarControllerTest extends ApplicationTest {

  @TempDir Path tempDir;

  private EventFileManager eventFiles;
  private UserFileManager userFiles;

  /**
   * Sets up the calendar using temporary files.
   *
   * @param stage the primary stage
   * @throws IOException if FXML loading fails
   */
  @Override
  public void start(Stage stage) throws IOException {
    eventFiles = new EventFileManager(tempDir.resolve("eventdata.json").toString());
    userFiles = new UserFileManager(tempDir.resolve("userdata.json").toString());

    UserManager manager = new UserManager(userFiles);
    manager.register("testuser", "testpassword");
    manager.login("testuser", "testpassword");

    FXMLLoader loader =
        new FXMLLoader(CalendarController.class.getResource("/ui/Calendar.fxml"));

    loader.setControllerFactory(
        type -> {
          if (type == CalendarController.class) {
            return new CalendarController(eventFiles, userFiles);
          }
          throw new IllegalArgumentException("Unexpected controller: " + type);
        });

    Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @AfterEach
  void cleanUp() {
    UserSession.logout();
  }

  /** Tests that the calendar displays the current month and year. */
  @Test
  void testWindowLoads() {
    verifyThat("#yearLabel", isVisible());
    verifyThat("#yearLabel", hasText(Integer.toString(LocalDate.now().getYear())));
    verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().name()));
  }

  /** Tests navigation between months. */
  @Test
  void testMonthChange() {
    clickOn("#nextMonth");
    verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().plus(1).name()));

    clickOn("#prevMonth");
    verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().name()));

    clickOn("#prevMonth");
    verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().minus(1).name()));
  }

  /** Tests opening and closing the event creator. */
  @Test
  void showEventCreator() {
    verifyThat("#eventCreator", isVisible().negate());
    verifyThat("#openEventCreator", hasText("Create event"));

    clickOn("#openEventCreator");
    verifyThat("#eventCreator", isVisible());
    verifyThat("#closeEventCreator", isVisible());

    clickOn("#closeEventCreator");
    verifyThat("#eventCreator", isVisible().negate());
  }

  /** Tests event creation, duplicate names and invalid input. */
  @Test
  void testEventCreator() {
    createEvent();
    verifyThat("#eventError", hasText(""));

    createEvent();
    verifyThat("#eventError", hasText(""));

    assertEquals(
        2,
        new EventFileManager(tempDir.resolve("eventdata.json").toString())
            .getEvents().values().stream()
            .filter(event -> event.getTitle().equals("Testing"))
            .count());

    clickOn("#openEventCreator");
    clickOn("#createEvent");

    verifyThat(
        "#eventError",
        hasText("Something went wrong. \nCheck that you have filled all fields properly"));
  }

  /** Tests that logout opens the login page and clears the session. */
  @Test
  public void testValidLogout() throws InterruptedException {
    clickOn("#logout");

    verifyThat("#loginPageUsernameField", isVisible());
    assertThrows(NullPointerException.class, () -> UserSession.getUser());
  }

  /** Tests event details, attendance and deletion. */
  @Test
  public void testEventPane() {
    verifyThat("#eventPane", isVisible().negate());

    createEvent();

    Node label = lookup(hasText("Testing")).query();
    HBox eventPane = (HBox) label.getParent();
    Button button = (Button) eventPane.getChildren().get(1);

    clickOn(button);
    verifyThat("#eventPane", isVisible());

    verifyThat("#toggleEventButton", hasText("Join event"));

    clickOn("#toggleEventButton");
    verifyThat("#toggleEventButton", hasText("Leave event"));

    clickOn("#closeEvent");
    verifyThat("#eventPane", isVisible().negate());

    clickOn(button);
    verifyThat("#toggleEventButton", hasText("Leave event"));

    clickOn("#deleteEvent");
  }

  /** Tests independent attendance and deletion for identically named events. */
  @Test
  void sameNamedEventsHaveIndependentAttendanceAndDeletion() {
    createEvent();
    createEvent();

    List<Button> buttons =
        lookup(hasText("Testing")).queryAll().stream()
            .filter(node -> node.getParent() instanceof HBox)
            .map(node -> (Button) ((HBox) node.getParent()).getChildren().get(1))
            .toList();

    assertEquals(2, buttons.size());

    clickOn(buttons.get(0));
    clickOn("#toggleEventButton");
    verifyThat("#toggleEventButton", hasText("Leave event"));

    clickOn("#closeEvent");

    clickOn(buttons.get(1));
    verifyThat("#toggleEventButton", hasText("Join event"));

    clickOn("#deleteEvent");

    Node remaining = lookup(hasText("Testing")).query();
    clickOn(((HBox) remaining.getParent()).getChildren().get(1));

    verifyThat("#toggleEventButton", hasText("Leave event"));

    assertEquals(
        1,
        new EventFileManager(tempDir.resolve("eventdata.json").toString())
            .getEvents().values().stream()
            .filter(event -> event.getTitle().equals("Testing"))
            .count());
  }

  /** Creates a test event through the UI. */
  private void createEvent() {
    clickOn("#openEventCreator");

    clickOn("#setEventTitle").write("Testing");
    clickOn("#setEventLocation").write("Testing location");

    DatePicker datePicker = lookup("#setEventDate").queryAs(DatePicker.class);
    interact(() -> datePicker.setValue(LocalDate.now().withDayOfMonth(1)));

    press(KeyCode.ENTER).release(KeyCode.ENTER);

    clickOn("#setEventTime").write("10:00");

    clickOn("#createEvent");
    waitForFxEvents();
  }
}
