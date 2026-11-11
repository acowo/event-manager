package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.NodeQueryUtils.isVisible;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import dto.EventDto;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.testfx.framework.junit5.ApplicationTest;
import ui.client.EventRestApiClient;
import ui.client.UserRestApiClient;
import ui.controller.CalendarController;

/**
 * Test class for CalendarController, verifying UI behavior and user interactions.
 * Mockito static/construction mocks are thread-local, and the controller runs on the JavaFX
 * thread.
 */
public class CalendarControllerTest extends ApplicationTest {

  private final Map<String, EventDto> serverEvents = new ConcurrentHashMap<>();
  private final Map<String, Boolean> serverAttendance = new ConcurrentHashMap<>();

  private boolean failCreate;
  private boolean failOptIn;

  private MockedConstruction<EventRestApiClient> eventClient;
  private MockedConstruction<UserRestApiClient> userClient;

  /**
   * Setup before each test. Mocks the REST clients, seeds the fake server with two events owned by
   * someone else, and loads the calendar view.
   *
   * @param stage the primary stage
   * @throws IOException if loading the FXML fails
   */
  @Override
  public void start(Stage stage) throws IOException {
    serverEvents.clear();
    serverAttendance.clear();
    failCreate = false;
    failOptIn = false;

    serverEvents.put("non-admin-1",
        new EventDto("non-admin-1", "Non-admin", "Somewhere", LocalDateTime.now()));
    serverEvents.put("non-admin-2",
        new EventDto("non-admin-2", "Non-admin", "Somewhere", LocalDateTime.now()));

    eventClient = mockConstruction(
        EventRestApiClient.class, (mock, context) -> {
          when(mock.getAllEvents()).thenAnswer(inv -> new ArrayList<>(serverEvents.values()));
          when(mock.createEvent(any(), any(), any(), any())).thenAnswer(inv -> {
            if (failCreate) {
              return false;
            }
            String id = inv.getArgument(0);
            serverEvents.put(id, new EventDto(
                id, inv.getArgument(1), inv.getArgument(2), inv.getArgument(3)));
            serverAttendance.put(id, true);
            return true;
          });
            doAnswer(inv -> {
              serverEvents.remove((String) inv.getArgument(0));
              serverAttendance.remove((String) inv.getArgument(0));
              return inv.getMethod().getReturnType() == boolean.class ? true : null;
            }).when(mock).deleteEventById(any());
      });

    userClient = mockConstruction(UserRestApiClient.class, (mock, context) -> {
      when(mock.getUserEvents()).thenAnswer(inv -> new HashMap<>(serverAttendance));
      when(mock.optIn(any())).thenAnswer(inv -> {
        if (failOptIn) {
          return false;
        }
        serverAttendance.put(inv.getArgument(0), false);
        return true;
      });
      when(mock.optOut(any())).thenAnswer(inv -> {
        serverAttendance.remove((String) inv.getArgument(0));
        return true;
      });
    });

    FXMLLoader loader = new FXMLLoader(
        CalendarController.class.getResource("/ui/Calendar.fxml")
    );
    Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Closes the thread-local mocks on the FX thread, where they were opened.
   * */
  @AfterEach
  void cleanUp() {
    interact(() -> {
      userClient.close();
      eventClient.close();
    });
  }

  /**
   * Tests that the calendar opens properly with the month and year of today.
   * */
  @Test
  void testWindowLoads() {
    verifyThat("#yearLabel", isVisible());
    verifyThat("#yearLabel", hasText(Integer.toString(LocalDate.now().getYear())));
    verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().name()));
  }

  /**
   * Tests that events fetched from the server are shown in the calendar on load.
   * */
  @Test
  void testServerEventsAreShown() {
    assertEquals(2, findEventButtons("Non-admin").size());
  }

  /**
   * Tests that the months change properly when using the arrows to traverse months.
   * */
  @Test
  void testMonthChange() {
    clickOn("#nextMonth");
    verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().plus(1).name()));

    clickOn("#prevMonth");
    verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().name()));

    clickOn("#prevMonth");
    verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().minus(1).name()));
  }

  /**
   * Tests that the create event button opens the eventcreator window and that the window closes
   * upon pressing the cancel button.
   */
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

  /**
   * Tests that the eventcreator manages to create an event (sent to the server), allows duplicate
   * names and rejects incomplete fields.
   */
  @Test
  void testEventCreator() {
    createEvent();
    verifyThat("#eventError", hasText(""));

    createEvent();
    verifyThat("#eventError", hasText(""));
    assertEquals(2, countServerEvents("Testing"));

    clickOn("#openEventCreator");
    clickOn("#createEvent");
    verifyThat(
        "#eventError",
        hasText("Something went wrong. \nCheck that you have filled all fields properly"));
    assertEquals(2, countServerEvents("Testing"));
  }

  /**
   * Tests that an error is shown, and no event is displayed, when the server rejects creation.
   * */
  @Test
  void testEventCreatorServerFailure() {
    failCreate = true;
    createEvent();

    verifyThat("#eventCreator", isVisible());
    verifyThat(
        "#eventError",
        hasText("Something went wrong. \nCheck that you have filled all fields properly"));
    assertEquals(0, countServerEvents("Testing"));
    assertTrue(findEventButtons("Testing").isEmpty());
  }

  /**
   * Tests that the logout button opens the loginPage.
   */
  @Test
  public void testValidLogout() {
    clickOn("#logout");

    verifyThat("#loginPageUsernameField", isVisible());
  }

  /**
   * Tests that the event pane for an event the user does not own shows properly, and that
   * joining/leaving is sent to the server.
   */
  @Test
  public void testEventPane() {
    verifyThat("#eventPane", isVisible().negate());

    Button button = findEventButtons("Non-admin").get(0);
    clickOn(button);
    verifyThat("#eventPane", isVisible());

    verifyThat("#toggleEventButton", hasText("Join event"));
    clickOn("#toggleEventButton");
    verifyThat("#toggleEventButton", hasText("Leave event"));
    assertEquals(1, serverAttendance.size());
    assertFalse(serverAttendance.values().iterator().next());

    clickOn("#closeEventButton");
    verifyThat("#eventPane", isVisible().negate());

    clickOn(button);
    verifyThat("#toggleEventButton", hasText("Leave event"));
    clickOn("#toggleEventButton");
    verifyThat("#toggleEventButton", hasText("Join event"));
    assertTrue(serverAttendance.isEmpty());
  }

  /**
   * Tests that the UI stays on "Join event" when the server rejects the request.
   * */
  @Test
  void testJoinEventServerFailure() {
    failOptIn = true;
    clickOn(findEventButtons("Non-admin").get(0));

    clickOn("#toggleEventButton");
    verifyThat("#eventErrorLabel", hasText("Something went wrong. \nPlease try again later."));
    verifyThat("#toggleEventButton", hasText("Join event"));
    assertTrue(serverAttendance.isEmpty());
  }

  /**
   * Tests that the creator of an event gets a delete button, and that deleting hits the server.
   * */
  @Test
  void testCreatorCanDeleteEvent() {
    createEvent();
    assertEquals(1, countServerEvents("Testing"));

    clickOn(findEventButtons("Testing").get(0));
    verifyThat("#deleteEventButton", isVisible());
    clickOn("#deleteEventButton");
    clickDialogButton("OK");
    waitForFxEvents();

    assertEquals(0, countServerEvents("Testing"));
    assertTrue(findEventButtons("Testing").isEmpty());
    verifyThat("#eventPane", isVisible().negate());
  }

  /** Tests that same-named events have independent attendance and deletion. */
  @Test
  void sameNamedEventsHaveIndependentAttendanceAndDeletion() {
    List<Button> buttons = findEventButtons("Non-admin");
    assertEquals(2, buttons.size());

    // Join the first event only
    clickOn(buttons.get(0));
    clickOn("#toggleEventButton");
    verifyThat("#toggleEventButton", hasText("Leave event"));
    clickOn("#closeEventButton");

    // The other one is still joinable
    clickOn(buttons.get(1));
    verifyThat("#toggleEventButton", hasText("Join event"));
    clickOn("#closeEventButton");
    assertEquals(1, serverAttendance.size());

    // Deleting an event we created only removes that event
    createEvent();
    createEvent();
    assertEquals(2, countServerEvents("Testing"));
    clickOn(findEventButtons("Testing").get(0));
    clickOn("#deleteEventButton");
    clickDialogButton("OK");
    waitForFxEvents();

    assertEquals(1, countServerEvents("Testing"));
    assertEquals(1, findEventButtons("Testing").size());
    assertEquals(2, findEventButtons("Non-admin").size());
  }

  /** Help function to create test event through the UI. */
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

  @Test
  void testDeleteUser() throws Exception {
    UserRestApiClient calendarUserClient = userClient.constructed().get(0);

    clickOn("#deleteUser");
    clickDialogButton("Cancel");
    verify(calendarUserClient, never()).deleteUser();
    verifyThat("#yearLabel", isVisible());

    clickOn("#deleteUser");
    clickDialogButton("OK");
    waitForFxEvents();

    verify(calendarUserClient).deleteUser();
    verifyThat("#loginPageUsernameField", isVisible());
  }

  /**
   * Finds the "..." buttons of all events in the calendar with the given title.
   * */
  private List<Button> findEventButtons(String title) {
    return lookup(hasText(title)).queryAll().stream()
        .filter(node -> node.getParent() instanceof HBox)
        .map(node -> (HBox) node.getParent())
        .filter(box -> box.getChildren().size() == 2 && box.getChildren().get(1) instanceof Button)
        .map(box -> (Button) box.getChildren().get(1))
        .toList();
  }

  /**
   * Clicks on a button in a dialog pane based on the text.
   *
   * @param text the text on the button.
   */
  private void clickDialogButton(String text) {
    waitForFxEvents();
    clickOn(lookup(".dialog-pane .button").match(hasText(text)).queryAs(Button.class));
    waitForFxEvents();
  }

  /**
   * Counts the events with the given title on the fake server.
   * */
  private long countServerEvents(String title) {
    return serverEvents.values().stream().filter(e -> e.title().equals(title)).count();
  }
}
