package ui.controller;

import event.Event;
import event.EventManager;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import persistence.EventFileManager;
import persistence.UserFileManager;
import user.User;
import user.UserManager;
import user.UserSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller for {@code /ui/CalendarApp.fxml}.
 */

public class CalendarController {
  private final EventManager eventManager;
  private final UserManager userManager;
  private Month month;
  private int year;
  private LocalDate today;
  private Map<String, Event> events;
  private String selectedEventId;
  private User user;
  @FXML
  private GridPane calendar;
  @FXML
  private Label monthLabel, yearLabel, time, eventTitle, eventDate, eventTime, eventLocation, eventError;
  @FXML
  private ScrollPane eventCreator, eventPane;
  @FXML
  private TextField setEventTitle, setEventLocation, setEventTime;
  @FXML
  private DatePicker setEventDate;
  @FXML
  private ToggleButton toggleEventButton;
  @FXML
  private Button deleteEventButton, closeEventButton;
  @FXML
  private HBox eventButtons;
  @FXML
  private Pane resourcePane;

  public CalendarController() {
    this(new EventFileManager(), new UserFileManager());
  }

  public CalendarController(EventFileManager eventManager, UserFileManager userManager) {
    this.eventManager = new EventManager(Objects.requireNonNull(eventManager));
    this.userManager = new UserManager(Objects.requireNonNull(userManager));
  }

  /**
   * Default method automatically called when opening the Calendar.fxml file.
   *
   * <p>Sets the view to show the current month and shows all events for the month</p>
   */

  @FXML
  public void initialize() {
    this.today = LocalDate.now();
    user = UserSession.getUser();
    this.events = this.eventManager.getEvents();
    this.user = UserSession.getUser();


    setMonth(this.today);

    closeEventCreator();
    hideEvent();
  }

  /**
   * Changes {@link #month month} to one month before. <br> Updates the display to fit the new month with
   * {@link #setMonth setMonth()}
   *
   * @see #nextMonth nextMonth
   */

  @FXML
  public void prevMonth() {
    if (this.month.equals(Month.JANUARY)) {
      this.year -= 1;
    }
    this.month = this.month.minus(1);
    setMonth(this.month, this.year);
  }

  /**
   * Changes {@link #month month} to one month after. <br>
   * Updates the display to fit the new month with {@link #setMonth setMonth()}
   *
   * @see #prevMonth prevMonth
   */

  @FXML
  public void nextMonth() {
    this.month = this.month.plus(1);
    if (this.month.equals(Month.JANUARY)) {
      this.year += 1;
    }
    setMonth(this.month, this.year);
  }

  @FXML
  public void openEventCreator() {
    eventCreator.disableProperty().set(false);
    eventCreator.setVisible(true);
    eventCreator.opacityProperty().set(1);
  }

  @FXML
  public void closeEventCreator() {
    eventCreator.disableProperty().set(true);
    eventCreator.opacityProperty().set(0);
    eventCreator.setVisible(false);
    eventError.setText("");
    setEventTime.setText("");
    setEventTitle.setText("");
    setEventLocation.setText("");
  }

  @FXML
  public void createEvent() {

    try {
      LocalDate date = setEventDate.getValue();
      LocalTime time =
          LocalTime.of(Integer.parseInt(setEventTime.getText(0, 2)), Integer.parseInt(setEventTime.getText(3, 5)));
      Event event = new Event(setEventTitle.getText(), setEventLocation.getText(), LocalDateTime.of(date, time));
      if (this.eventManager.addEvent(event)) {
        this.events.put(event.getEventId(), event);
        setMonth(LocalDate.of(this.year, this.month, 1));
        this.user.addEventId(event.getEventId(), true);
        userManager.editUser(user.getUsername(), user);
        closeEventCreator();
      } else {
        eventError.setText("Something went wrong. \nCheck that you have filled all fields properly");
      }
    } catch (Exception e) {
      eventError.setText("Something went wrong. \nCheck that you have filled all fields properly");
    }
  }

  @FXML
  public void deleteEvent() {
    if (selectedEventId == null) {
      return;
    }
    userManager.removeEvent(selectedEventId);
    eventManager.removeEvent(selectedEventId);
    events.remove(selectedEventId);

    if (user.isAttendingEvent(selectedEventId)) {
      user.removeEventId(selectedEventId);
    }
    setMonth(LocalDate.of(this.year, this.month, 1));
    hideEvent();
  }


  public void showEvent(Event event) {
    selectedEventId = event.getEventId();

    eventPane.setDisable(false);
    eventPane.setVisible(true);

    eventTitle.setText(event.getTitle());
    eventLocation.setText(event.getPlace());
    eventDate.setText(event.getTime().toLocalDate().toString());
    eventTime.setText(event.getTime().toLocalTime().toString());

    eventButtons.getChildren().clear();
    eventButtons.getChildren().add(closeEventButton);

    if (user.isAdministratingEvent(event.getEventId())) {
      eventButtons.getChildren().add(0, deleteEventButton);
    } else {
      eventButtons.getChildren().add(0, toggleEventButton);


      if (user.isAttendingEvent(event.getEventId())) {
        toggleEventButton.setText("Leave event");
        toggleEventButton.setSelected(true);
      } else {
        toggleEventButton.setText("Join event");
        toggleEventButton.setSelected(false);
      }
    }
  }

  @FXML
  public void toggleEvent() {
    if (selectedEventId == null) {
      return;
    }
    Event event = events.get(selectedEventId);

    if (toggleEventButton.getText().equals("Leave event")) {
      toggleEventButton.setText("Join event");
      user.removeEventId(event.getEventId());
    } else {
      toggleEventButton.setText("Leave event");
      user.addEventId(event.getEventId(), false);

    }
    userManager.editUser(user.getUsername(), user);
  }


  @FXML
  public void hideEvent() {
    selectedEventId = null;

    eventPane.setDisable(true);
    eventPane.setVisible(false);
  }


  /**
   * Ends the current user session and changes back to the login page.
   *
   * @param event the FXML event
   * @throws IOException if FXML cannot be found

   */
  @FXML
  public void handleLogout(javafx.event.Event event) throws IOException {
    UserSession.logout();
    changeScene(event, "/ui/loginPage.fxml");

  }

  /**
   * Displays a month in the UI based on a specific date.
   *
   * @param date the date of the month to be shown
   * @throws IllegalArgumentException if date is null
   */
  private void setMonth(LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException();
    }
    calendar.getChildren().subList(1, calendar.getChildren().size()).clear();
    this.month = date.getMonth();
    this.year = date.getYear();
    int dateNumber = date.getDayOfMonth();
    date = date.minusDays(dateNumber - 1);
    int day = date.getDayOfWeek().get(ChronoField.DAY_OF_WEEK) - 1;
    int week = 0;

    monthLabel.setText(this.month.name());
    yearLabel.setText(Integer.toString(this.year));

    for (int i = 0; i < month.length(date.isLeapYear()); i++) {
      VBox pane = new VBox();
      pane.spacingProperty().set(5);
      Label dateLabel = new Label(Integer.toString(i + 1));
      dateLabel.setPadding(new Insets(3));
      pane.getChildren().add(dateLabel);
      calendar.add(pane, day, week);
      day = day + 1;
      if (day % 7 == 0) {
        day = 0;
        week += 1;
      }
    }
    addEvents();
  }

  /**
   * Displays a month in the UI based on a specific month in a year.
   *
   * @param month the month to be shown
   * @param year the year of the month to be shown
   * @throws IllegalArgumentException if month or year before 1970
   */
  private void setMonth(Month month, int year) {
    if (month == null || year < 1970) {
      throw new IllegalArgumentException();
    }
    setMonth(LocalDate.of(year, month, 1));
  }

  /**
   * Displays all events of the current month showing.
   *
   * @throws IllegalStateException if this.month is null
   */
  private void addEvents() {
    if (this.month == null) {
      throw new IllegalStateException();
    }
    if (this.events != null) {
      List<Event> eventsInMonth = this.events.values().stream()
          .filter(e -> e.getTime().getMonth() == this.month && e.getTime().getYear() == this.year).toList();
      for (Event event : eventsInMonth) {
        int dayOfMonth = event.getTime().getDayOfMonth();
        VBox pane = (VBox) calendar.getChildren().get(dayOfMonth);
        HBox eventPane = new HBox();

        //TODO: Fix the styling of new events
        eventPane.setStyle("-fx-background-color: #cc76fb; -fx-background-radius:5px;");
        eventPane.getChildren().add(new Label(event.getTitle()));

        Button button = new Button("...");
        button.setOnAction(
            e -> {
              showEvent(event);
            });

        eventPane.getChildren().add(button);
        HBox.setMargin(button, new Insets(0, 0, 0, 2));

        int dayOfMonth = event.getTime().getDayOfMonth();
        VBox pane = (VBox) calendar.getChildren().get(dayOfMonth);
        pane.getChildren().add(eventPane);
      }
    }
  }

  /**
   * Changes the FXML to a different view.
   *
   * @param event the FXML event that caused the function to run
   * @param filepath the filepath of the new view
   * @throws IOException if FXML cannot be found
   */
  private void changeScene(javafx.event.Event event, String filepath) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource(filepath));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
