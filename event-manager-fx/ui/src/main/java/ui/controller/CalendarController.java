
package ui.controller;

import dto.EventDto;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.auth.ClientSession;
import ui.client.EventRestApiClient;
import ui.client.UserRestApiClient;

/** Controller for {@code /ui/CalendarApp.fxml}. */
public class CalendarController {
  private Month month;
  private int year;
  private LocalDate today;
  private Map<String, EventDto> events;
  private Map<String, Boolean> userAttendingEvents;
  private String selectedEventId;
  private String userAuth;
  private UserRestApiClient userRestApiClient;

  @FXML private GridPane calendar;

  @FXML private Label monthLabel;
  @FXML private Label yearLabel;
  @FXML private Label time;
  @FXML private Label eventTitle;
  @FXML private Label eventDate;
  @FXML private Label eventTime;
  @FXML private Label eventLocation;
  @FXML private Label eventError;
  @FXML private Label eventErrorLabel;

  @FXML private ScrollPane eventCreator;
  @FXML private ScrollPane eventPane;
  @FXML private TextField setEventTitle;
  @FXML private TextField setEventLocation;
  @FXML private TextField setEventTime;

  @FXML private DatePicker setEventDate;

  @FXML private ToggleButton toggleEventButton;

  @FXML private Button deleteEventButton;
  @FXML private Button closeEventButton;

  @FXML private HBox eventButtons;

  /**
   * Default constructor, creates a default eventFileManager and userFileManager.
   */
  public CalendarController() {
    this.userRestApiClient = new UserRestApiClient();
  }

  /**
   * Default method automatically called when opening the Calendar.fxml file.
   *
   * <p>Sets the view to show the current month and shows all events for the month.
   */
  @FXML
  public void initialize() {

    this.today = LocalDate.now();
    try {
      this.events = new HashMap<>(eventListToMap(EventRestApiClient.getAllEvents()));
      this.userAttendingEvents = new HashMap<>(userRestApiClient.getUserEvents());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
    this.userAuth = ClientSession.getAuthToken();

    setMonth(this.today);

    closeEventCreator();
    hideEvent();
  }

  /**
   * Changes {@link #month month} to one month before. <br>
   * Updates the display to fit the new month with {@link #setMonth setMonth()}
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

  /**
   * Opens the event creator pane.
   */
  @FXML
  public void openEventCreator() {
    eventCreator.disableProperty().set(false);
    eventCreator.setVisible(true);
  }

  /**
   * Closes the event creator pane and clears its input fields.
   */
  @FXML
  public void closeEventCreator() {
    eventCreator.disableProperty().set(true);
    eventCreator.setVisible(false);
    eventError.setText("");
    setEventTime.setText("");
    setEventTitle.setText("");
    setEventLocation.setText("");
  }

  /**
   * Creates an event based on the user input and adds it to the manager.
   * Shows error message if the event cannot be created.
   */
  @FXML
  public void createEvent() {

    try {
      LocalDate date = setEventDate.getValue();
      LocalTime time =
          LocalTime.of(
              Integer.parseInt(setEventTime.getText(0, 2)),
              Integer.parseInt(setEventTime.getText(3, 5)));
      String id = UUID.randomUUID().toString();
      if (EventRestApiClient.createEvent(
          id, setEventTitle.getText(), setEventLocation.getText(), LocalDateTime.of(date, time))) {
        this.events.put(id, new EventDto(
            id, setEventTitle.getText(), setEventLocation.getText(), LocalDateTime.of(date, time)));
        setMonth(LocalDate.of(this.year, this.month, 1));
        userAttendingEvents.put(id, true);
        closeEventCreator();
      } else {
        eventError.setText("Something went wrong. \n"
            + "Check that you have filled all fields properly");
      }
    } catch (Exception e) {
      eventError.setText("Something went wrong. "
          + "\nCheck that you have filled all fields properly");
    }
  }

  /** Deletes the currently selected event from the repository and calendar view. */
  @FXML
  public void deleteEvent() {
    if (selectedEventId == null) {
      return;
    }
    // TODO: Update the local registry of events
    events.remove(selectedEventId);
    try {
      EventRestApiClient.deleteEventById(selectedEventId);
      setMonth(LocalDate.of(this.year, this.month, 1));
      hideEvent();
    } catch (IOException | InterruptedException e) {
      eventErrorLabel.setText("Something went wrong. \n"
          + "Please try again later.");
    }

  }

  /**
   * Shows an event and its info in the event pane.
   * Shows the togglebutton if user is not admin of the event.
   * Shows a delete button if user is admin of the event.
   *
   * @param eventDto the {@link EventDto} of the event to be shown
   */
  public void showEvent(EventDto eventDto) {
    selectedEventId = eventDto.eventId();

    eventPane.setDisable(false);
    eventPane.setVisible(true);

    eventTitle.setText(eventDto.title());
    eventLocation.setText(eventDto.place());
    eventDate.setText(eventDto.time().toLocalDate().toString());
    eventTime.setText(eventDto.time().toLocalTime().toString());

    eventButtons.getChildren().clear();
    eventButtons.getChildren().add(closeEventButton);
    if (userAttendingEvents.containsKey(eventDto.eventId())) {
      if (userAttendingEvents.get(eventDto.eventId())) {
        eventButtons.getChildren().add(0, deleteEventButton);
      } else {
        eventButtons.getChildren().add(0, toggleEventButton);
        toggleEventButton.setText("Leave event");
        toggleEventButton.setSelected(true);
      }
    } else {
      eventButtons.getChildren().add(0, toggleEventButton);
      toggleEventButton.setText("Join event");
      toggleEventButton.setSelected(false);
    }
  }

  /**
   * Opts user in or out from event and changes the button view.
   */
  @FXML
  public void toggleEvent() {
    if (selectedEventId == null) {
      return;
    }
    EventDto eventDto = events.get(selectedEventId);

    try {
      if (toggleEventButton.getText().equals("Leave event")) {
        if (userRestApiClient.optOut(selectedEventId)) {
          userAttendingEvents.remove(selectedEventId);
          toggleEventButton.setText("Join event");
        } else {
          throw new IllegalStateException();
        }
      } else {
        if (userRestApiClient.optIn(selectedEventId)) {
          userAttendingEvents.put(selectedEventId, false);
          toggleEventButton.setText("Leave event");
        } else {
          throw new IllegalStateException();
        }
      }
    } catch (IOException | InterruptedException | IllegalStateException e) {
      eventErrorLabel.setText("Something went wrong. \n"
          + "Please try again later.");
      toggleEventButton.setSelected(!toggleEventButton.isSelected());
      e.printStackTrace();
    }
  }

  /**
   * Hides the event pane.
   */
  @FXML
  public void hideEvent() {
    selectedEventId = null;
    eventPane.setDisable(true);
    eventPane.setVisible(false);
    eventErrorLabel.setText("");
  }

  /**
   * Ends the current user session and changes back to the login page.
   *
   * @param event the FXML event
   * @throws IOException if an error occurs while loading the file
   */
  @FXML
  public void handleLogout(javafx.event.Event event) throws IOException {
    ClientSession.clearAuthToken();
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
   * @param year  the year of the month to be shown
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
      List<EventDto> eventsInMonth = this.events.values().stream()
          .filter(e -> e.time().getMonth() == this.month
              && e.time().getYear() == this.year).toList();
      for (EventDto eventDto : eventsInMonth) {
        HBox eventPane = new HBox();

        // TODO: Fix the styling of new events
        eventPane.setStyle("-fx-background-color: #cc76fb; -fx-background-radius:5px;");
        eventPane.getChildren().add(new Label(eventDto.title()));

        Button button = new Button("...");
        button.setOnAction(e -> {
          showEvent(eventDto);
        });

        eventPane.getChildren().add(button);
        HBox.setMargin(button, new Insets(0, 0, 0, 2));

        int dayOfMonth = eventDto.time().getDayOfMonth();
        VBox pane = (VBox) calendar.getChildren().get(dayOfMonth);
        pane.getChildren().add(eventPane);
      }
    }
  }

  /**
   * Changes the sFXML to a different view.
   *
   * @param event    the FXML event that caused the function to run
   * @param filepath the filepath of the new view
   * @throws IOException if an error occurs during loading of the file
   */
  private void changeScene(javafx.event.Event event, String filepath) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource(filepath));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  private Map<String, EventDto> eventListToMap(List<EventDto> events) {
    Map<String, EventDto> map = new HashMap<>();
    events.forEach(e -> map.put(e.eventId(), e));
    return map;
  }
}
