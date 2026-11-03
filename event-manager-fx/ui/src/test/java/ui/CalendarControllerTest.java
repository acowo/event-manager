package ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.NodeQueryUtils.isVisible;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.nio.file.Path;
import java.util.List;

import event.Event;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.service.query.EmptyNodeQueryException;

import ui.controller.CalendarController;
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
    private EventFileManager eventFiles;
    private UserFileManager userFiles;
    private List<Event> savedEvents(String title) {
        return new EventFileManager(tempDir.resolve("eventdata.json").toString())
                .getEvents().values().stream()
                .filter(e -> e.getTitle().equals(title))
                .toList();
    }

    @TempDir
    Path tempDir;

    /**    (non-Javadoc)
     * Setup before each test. Clears the event and user files for the test event and sets user session with test user
     * @see org.testfx.framework.junit5.ApplicationTest#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage stage) throws IOException {
        eventFiles = new EventFileManager(
            tempDir.resolve("eventdata.json").toString()
        );

        userFiles = new UserFileManager(
            tempDir.resolve("userdata.json").toString()
        );

        UserManager manager = new UserManager(userFiles);
        manager.register("testuser", "testpassword");
        manager.login("testuser", "testpassword");

        eventFiles.addEvent(new Event("Not Admin", "Home", LocalDateTime.now()));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Calendar.fxml"));
        loader.setControllerFactory(type -> {
            if (type == CalendarController.class) {
                return new CalendarController(eventFiles, userFiles);
            }
            throw new IllegalArgumentException("Unexpected controller: " + type);
        });

        Parent root = loader.load();   // instance load, not the static FXMLLoader.load
        stage.setScene(new Scene(root));
        stage.show();

    }

    @AfterEach
    void cleanUp() {
        UserSession.logout();
    }

    /**
     * Tests that the calendar opens properly with the month and year of today
     */
    @Test 
    void testWindowLoads() {
        verifyThat("#yearLabel", isVisible());
        verifyThat("#yearLabel", hasText(Integer.toString(LocalDate.now().getYear())));
        verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().name()));
    }

    /**
     * Tests that the months change properly when using the arrows to traverse months
     */
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
     * Tests that the create event button opens the eventcreator window and that the window closes upon pressing the cancel button
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
     * Tests that the eventcreator manages to create an event, but allows duplicate names and rejects incomplete fields.
     */
    @Test 
    void testEventCreator() {
        createEvent();
        verifyThat("#eventError", hasText(""));
        
        createEvent();
        verifyThat("#eventError", hasText(""));
        assertEquals(2, savedEvents("Testing").size());
        
        clickOn("#openEventCreator");
        clickOn("#createEvent");
        verifyThat("#eventError", hasText("Something went wrong. \nCheck that you have filled all fields properly"));
    }

    /**
     * tests that the logout button opens the loginPage and that the user in session is removed
     *
     */
    @Test
    public void testValidLogout() {
        clickOn("#logout");

        //Verifying that the scene changed by checking that a node from the login view is present
        verifyThat("#loginPage_usernameField", isVisible());
        assertThrows(NullPointerException.class, UserSession::getUser);
    }


    /**
     * Tests that the event pane for an event shows properly
     */
    @Test
    public void testEventPane() {
        verifyThat("#eventPane", isVisible().negate()); //Checks that the pane is invisible initially

        createEvent(); //Creates a test event and localizes the button that shows the event pane
        Node label = lookup(hasText("Testing")).query();
        HBox eventPane = (HBox) label.getParent();
        Button button = (Button) eventPane.getChildren().get(1);
        clickOn(button);

        verifyThat("#eventPane", isVisible());

        clickOn("#closeEventButton");
        verifyThat("#eventPane", isVisible().negate());

        clickOn(button);
        //verifyThat("#toggleEventButton", hasText("Leave event"));
        clickOn("#deleteEventButton");

    }

    /**
     * Tests that the events have independent attendance and deletion
     */
    @Test
    void sameNamedEventsHaveIndependentDeletion() {
        createEvent();
        createEvent();
        List<Button> buttons = lookup(hasText("Testing")).queryAll().stream()
                .filter(node -> node.getParent() instanceof HBox)
                .map(node -> (Button) ((HBox) node.getParent()).getChildren().get(1))
                .toList();
        assertEquals(2, buttons.size());
        clickOn(buttons.get(0));
        verifyThat("#deleteEventButton", isVisible());
        clickOn("#closeEventButton");

        clickOn(buttons.get(1));
        verifyThat("#deleteEventButton", isVisible());
        clickOn("#closeEventButton");

        assertEquals(2, savedEvents("Testing").size());

        clickOn(buttons.get(0));
        clickOn("#deleteEventButton");

        assertEquals(1, savedEvents("Testing").size());

    }

    @Test
    void canJoinAndLeaveNonAdministrativeEvents() {
        Event nonAdmin = eventFiles.getEvents().values().stream().findFirst().orElseThrow(IllegalStateException::new);

        Node label = lookup(hasText("Not Admin")).query();
        HBox eventPane = (HBox) label.getParent();
        Button button = (Button) eventPane.getChildren().get(1);
        clickOn(button);

        verifyThat("#eventPane", isVisible());
        verifyThat("#toggleEventButton", isVisible());
        verifyThat("#toggleEventButton", hasText("Join event"));
        assertFalse(UserSession.getUser().isAttendingEvent(nonAdmin.getEventId()));

        clickOn("#toggleEventButton");
        verifyThat("#toggleEventButton", hasText("Leave event"));
        assertTrue(UserSession.getUser().isAttendingEvent(nonAdmin.getEventId()));

        clickOn("#closeEventButton");
        clickOn(button);

        verifyThat("#toggleEventButton", hasText("Leave event"));
        clickOn("#toggleEventButton");
        verifyThat("#toggleEventButton", hasText("Join event"));


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
