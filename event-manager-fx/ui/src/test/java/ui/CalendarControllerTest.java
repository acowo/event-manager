package ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.NodeQueryUtils.isVisible;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.service.query.EmptyNodeQueryException;

import event.EventManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import persistence.EventFileManager;
import persistence.UserFileManager;
import user.UserManager;
import user.UserSession;

public class CalendarControllerTest extends ApplicationTest {

  /**
   * Setup before each test. Clears the event and user files for the test event and
   * sets user session with test user.
   *
   * @param stage the primary stage
   * @throws IOException if loading the FXML fails
     */
    @Override
    public void start(Stage stage) throws IOException {
        EventFileManager files = new EventFileManager();
        files.getEvents().values().stream()
                .filter(event -> event.getTitle().equals("Testing"))
                .forEach(event -> {
                    files.removeEvent(event.getEventId());
                    new UserFileManager().removeEvent(event.getEventId());
                });
        UserFileManager users = new UserFileManager();
        users.removeEvent("Testing");

        UserManager manager = new UserManager(new UserFileManager());
        manager.login("ola", "banan"); //Sets user to testuser so the application properly opens
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/Calendar.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @AfterEach
    void cleanUp() {
        EventFileManager files = new EventFileManager();
        files.getEvents().values().stream()
                .filter(event -> event.getTitle().equals("Testing"))
                .forEach(event -> {
                    files.removeEvent(event.getEventId());
                    new UserFileManager().removeEvent(event.getEventId());
                });
    }

  /** Tests that the calendar opens properly with the month and year of today. */
    @Test 
    void testWindowLoads() {
        verifyThat("#yearLabel", isVisible());
        verifyThat("#yearLabel", hasText(Integer.toString(LocalDate.now().getYear())));
        verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().name()));
    }

  /** Tests that the months change properly when using the arrows to traverse months. */
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
        verifyThat("#eventCreator", isVisible().negate()); //Checks that the eventcreator is invisible to begin with

        verifyThat("#openEventCreator", hasText("Create event"));
        clickOn("#openEventCreator"); //Checks that the eventcreator is visible after pressing create event button
        verifyThat("#eventCreator", isVisible());
        verifyThat("#closeEventCreator", isVisible());
        
        clickOn("#closeEventCreator");
        verifyThat("#eventCreator", isVisible().negate());
    }
 
    /**
   * Tests that the eventcreator manages to create an event, but allows duplicate names and rejects
   * incomplete fields.
     */
    @Test 
    void testEventCreator() {
        createEvent();
        verifyThat("#eventError", hasText(""));
        
        createEvent();
        verifyThat("#eventError", hasText(""));
        assertEquals(2, new EventFileManager().getEvents().values().stream()
                .filter(event -> event.getTitle().equals("Testing")).count());
        
        clickOn("#openEventCreator");
        clickOn("#createEvent");
        verifyThat("#eventError", hasText("Something went wrong. \nCheck that you have filled all fields properly"));
    }

    /**
   * Tests that the logout button opens the loginPage and that the user in session is removed.
   *
   * @throws InterruptedException if interrupted
     */
    @Test
    public void testValidLogout() throws InterruptedException {
        clickOn("#logout");

        //Verifying that the scene changed by checking that a node from the login view is present
        verifyThat("#loginPage_usernameField", isVisible());
        assertThrows(NullPointerException.class, () -> UserSession.getUser());
    }

  /** Tests that the event pane for an event shows properly. */
    @Test
    public void testEventPane() {
        verifyThat("#eventPane", isVisible().negate()); //Checks that the pane is invisible initially
        
        createEvent(); //Creates a test event and localizes the button that shows the event pane
        Node label = lookup(hasText("Testing")).query();
        HBox eventPane = (HBox) label.getParent();
        Button button = (Button) eventPane.getChildren().get(1);
        clickOn(button);
        
        verifyThat("#eventPane", isVisible());
        
        verifyThat("#toggleEventButton", hasText("Join event")); //Checks that the user isnt signed up for an event that was just created, checks that the status changes after the button is pressed
        clickOn("#toggleEventButton");
        verifyThat("#toggleEventButton", hasText("Leave event"));
        
        clickOn("#closeEvent");
        verifyThat("#eventPane", isVisible().negate());
        
        clickOn(button);
        verifyThat("#toggleEventButton", hasText("Leave event"));
        clickOn("#deleteEvent");

    }

  /** Tests that the events have independent attendance and deletion. */
    @Test
    void sameNamedEventsHaveIndependentAttendanceAndDeletion() {
        createEvent();
        createEvent();
        List<Button> buttons = lookup(hasText("Testing")).queryAll().stream()
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
        clickOn((Button) ((HBox) remaining.getParent()).getChildren().get(1));
        verifyThat("#toggleEventButton", hasText("Leave event"));
        assertEquals(1, new EventFileManager().getEvents().values().stream()
                .filter(event -> event.getTitle().equals("Testing")).count());
    }

    /**
     * Help function to create test event
     */
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
