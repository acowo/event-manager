
package ui.controller;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import event.Event;
import event.EventManager;
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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import persistence.EventFileManager;
import persistence.UserFileManager;
import user.User;
import user.UserManager;
import user.UserSession;
import event.Event;


/**
 * Controller for {@code /ui/CalendarApp.fxml}. 
 */


public class CalendarController {
    private Month month;
    private int year;

    private LocalDate today;
    private Map<String,Event> events;
    private EventManager manager;
    private User user;
    private UserFileManager userManager;

    @FXML
    private GridPane calendar;

    @FXML
    private Label monthLabel, yearLabel, time, eventTitle, eventDate, eventTime, eventLocation, eventError;

    @FXML 
    private Slider hourSlider;

    @FXML 
    private ScrollPane eventCreator, eventPane;

    @FXML 
    private TextField setEventTitle, setEventLocation, setEventTime;

    @FXML 
    private DatePicker setEventDate;

    @FXML 
    private ToggleButton toggleEventButton;

    /**
     * Default method automatically called when opening the {@link Calendar.FXML} file
     * 
     * <p>Sets the view to show the current month and shows all events for the month</p>
     */

    @FXML
    public void initialize() {

      this.today = LocalDate.now();
      this.manager = new EventManager(new EventFileManager());
      events = manager.getEvents();
      user = UserSession.getUser();
      userManager = new UserFileManager();

      setMonth(this.today);

      closeEventCreator();
      hideEvent();
    }

    /**
     * <p>Changes {@link #month month} to one month before <br>
     * Updates the display to fit the new month with {@link #setMonth setMonth()} </p>
     * @see #nextMonth nextMonth
     */
    
    @FXML 
    public void prevMonth() {
      if (this.month.equals(Month.JANUARY)) {this.year -= 1;}
      this.month = this.month.minus(1);
      setMonth(this.month, this.year);
    }
    /**
     * <p>Changes {@link #month month} to one month after <br>
     * Updates the display to fit the new month with {@link #setMonth setMonth()} </p>
     * @see #prevMonth prevMonth
     */
    
    @FXML 
    public void nextMonth() {
      this.month = this.month.plus(1);
      if (this.month.equals(Month.JANUARY)) {this.year += 1;}
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
        LocalTime time = LocalTime.of(Integer.parseInt(setEventTime.getText(0,2)), Integer.parseInt(setEventTime.getText(3,5)));
        Event event = new Event(setEventTitle.getText(), setEventLocation.getText(), LocalDateTime.of(date, time));
        this.manager.addEvent(event);
        this.events.put(event.getTitle(), event);
        setMonth(LocalDate.of(this.year,this.month,1));
        closeEventCreator();
        
      } catch (Exception e) {
        eventError.setText("Something went wrong. \nCheck that you have filled all fields properly");
      }
    }

    @FXML 
    public void deleteEvent() {
      events.remove(eventTitle.getText());
      manager.removeEvent(eventTitle.getText());
      userManager.removeEvent(eventTitle.getText());
      if (user.isAttendingEvent(eventTitle.getText())) {user.removeEventId(eventTitle.getText());}
      setMonth(LocalDate.of(this.year,this.month,1));
      hideEvent();
    }


    public void showEvent(Event event) {
      eventPane.setOpacity(1);
      eventPane.setDisable(false);
      eventPane.setVisible(true);

      eventTitle.setText(event.getTitle());
      eventLocation.setText(event.getPlace());
      eventDate.setText(event.getTime().toLocalDate().toString());
      eventTime.setText(event.getTime().toLocalTime().toString());

      if (user.isAttendingEvent(event.getTitle())) {
        toggleEventButton.setText("Leave event");
        toggleEventButton.setSelected(true);
      } else {
        toggleEventButton.setText("Join event");
        toggleEventButton.setSelected(false);
      }
    }

    @FXML 
    public void toggleEvent() {
      Event event = events.get(eventTitle.getText());
      
      if (toggleEventButton.getText().equals("Leave event")) {
              toggleEventButton.setText("Join event");
              user.removeEventId(event.getTitle());
            } else {
              toggleEventButton.setText("Leave event");
              user.addEventId(event.getTitle());
            }
            userManager.editUser(user.getUsername(), user);
    }
    
    @FXML 
    public void hideEvent() {
      eventPane.setOpacity(0);
      eventPane.setDisable(true);
      eventPane.setVisible(false);
    }


    /**
     * Ends the current user session and changes back to the login page
     * @param event the FXML event
     * @throws IOException
     */
    @FXML 
    public void handleLogout(javafx.event.Event event) throws IOException{
      UserSession.logout();
      changeScene(event, "/ui/loginPage.fxml");

    }

    /**
     * Displays a month in the UI based on a specific date
     * @param date the date of the month to be shown
     * @throws IllegalArgumentException if date is null
     */

    private void setMonth(LocalDate date) {
      if (date == null) {throw new IllegalArgumentException();}
      calendar.getChildren().subList(1, calendar.getChildren().size()).clear();



      this.month = date.getMonth();
      this.year = date.getYear();
      int dateNumber = date.getDayOfMonth();
      date = date.minusDays(dateNumber-1);
      int day = date.getDayOfWeek().get(ChronoField.DAY_OF_WEEK)-1;
      int week = 0;

      monthLabel.setText(this.month.name());
      yearLabel.setText(Integer.toString(this.year));
      
      for (int i = 0; i<month.length(date.isLeapYear());i++){
        VBox pane = new VBox();
        pane.spacingProperty().set(5);
        Label dateLabel = new Label(Integer.toString(i+1));
        dateLabel.setPadding(new Insets(3));
        pane.getChildren().add(dateLabel);
        calendar.add(pane,day,week);
        day = day+1;
        if (day%7 == 0) {
          day = 0;
          week += 1;
        }
      }
      addEvents();
    }


    /**
     * Displays a month in the UI based on a specific month in a year
     * @param month the month to be shown
     * @param year the year of the month to be shown
     * @throws IllegalArgumentException if month or year before 1970
     */

    private void setMonth(Month month, int year) {
      if (month == null || year < 1970) {throw new IllegalArgumentException();}
      setMonth(LocalDate.of(year, month, 1));
    }

    /**
     * Displays all events of the current month showing
     * 
     * @param events list of all events
     * @throws IllegalStateException if this.month is null
     */

    private void addEvents() {
      if (this.month == null) {throw new IllegalStateException();}
      if (this.events != null) {
        List<Event> eventsInMonth = this.events.values().stream().filter(e -> e.getTime().getMonth()==this.month && e.getTime().getYear()==this.year).toList();
        for (Event event : eventsInMonth) {
          int dayOfMonth = event.getTime().getDayOfMonth();
          VBox pane = (VBox) calendar.getChildren().get(dayOfMonth);
          HBox eventPane = new HBox();

          //TODO: Fix the styling of new events
          eventPane.setStyle("-fx-background-color: #cc76fb; -fx-background-radius:5px;");
          eventPane.getChildren().add(new Label(event.getTitle()));

          Button button = new Button("...");
          button.setOnAction(e -> {showEvent(event);});
          
          eventPane.getChildren().add(button);
          HBox.setMargin(button, new Insets(0, 0, 0, 2));

          pane.getChildren().add(eventPane);
        }
      }
    }



    /**
     * Changes the FXML to a different view
     * @param event the FXML event that caused the function to run
     * @param filepath the filepath of the new view
     * @throws IOException
     */
    private void changeScene(javafx.event.Event event, String filepath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(filepath));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}


