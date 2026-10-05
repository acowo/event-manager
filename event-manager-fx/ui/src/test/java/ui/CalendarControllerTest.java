package ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.NodeQueryUtils.isVisible;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import event.EventManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import persistence.EventFileManager;
import persistence.UserFileManager;
import user.UserManager;

public class CalendarControllerTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws IOException {
        UserManager manager = new UserManager(new UserFileManager());
        manager.login("ola", "banan"); //Sets user to testuser so the application properly opens
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/Calendar.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
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

    @Test 
    void testMonthChange() {
        clickOn("#nextMonth");
        verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().plus(1).name()));

        clickOn("#prevMonth");
        verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().name()));

        clickOn("#prevMonth");
        verifyThat("#monthLabel", hasText(LocalDate.now().getMonth().minus(1).name()));
    }

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

    @Test 
    void createEvent() {
        EventFileManager manager = new EventFileManager();
        if (manager.eventExists("Testing event")) {
            manager.removeEvent("Testing event");
        }

        clickOn("#openEventCreator");

        clickOn("#setEventTitle").write("Testing event");
        clickOn("#setEventLocation").write("Testing location");
        clickOn("#setEventDate").write("10/23/2026");
        // Source - https://stackoverflow.com/a/59481991
        // Posted by user12150819
        // Retrieved 2026-10-05, License - CC BY-SA 4.0
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        clickOn("#setEventTime").write("10:00");

        clickOn("#createEvent");
        verifyThat("#eventError", hasText(""));
        assertDoesNotThrow(() -> manager.getEvent("Testing event"));


        clickOn("#openEventCreator"); //Checks that duplucate events fail
        clickOn("#createEvent");
        verifyThat("#eventError", hasText("Something went wrong. \nCheck that you have filled all fields properly"));
    }


    

}
