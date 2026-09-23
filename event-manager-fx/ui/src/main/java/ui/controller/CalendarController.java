
package ui.controller;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import persistence.EventFileManager;
import persistence.UserFileManager;
import user.User;
import user.UserSession;


/**
 * Controller for {@code /ui/CalendarApp.fxml}. 
 */


public class CalendarController {
    private Month month;
    private int year;
    private LocalDate today;

    @FXML
    private GridPane calendar;

    @FXML
    private Label monthLabel, yearLabel;

    /**
     * Default method automatically called when opening the {@link Calendar.FXML} file
     * 
     * <p>Sets the view to show the current month and shows all events for the month</p>
     */

    @FXML
    public void initialize() {
      this.today = LocalDate.now();
      setMonth(this.today);

      EventFileManager manager = new EventFileManager();
      Map<String, Event> events = manager.getEvents();
      addEvents(events.values().stream().toList());
    }

    @FXML 
    public void prevMonth() {
      if (this.month.equals(Month.JANUARY)) {this.year -= 1;}
      this.month = this.month.minus(1);
      setMonth(this.month, this.year);
    }
    
    @FXML 
    public void nextMonth() {
      this.month = this.month.plus(1);
      if (this.month.equals(Month.JANUARY)) {this.year += 1;}
      setMonth(this.month, this.year);
    }

    private void setMonth(LocalDate date) {
      if (date == null) {throw new NullPointerException();}
      calendar.getChildren().subList(8, calendar.getChildren().size()).clear();



      this.month = date.getMonth();
      this.year = date.getYear();
      int dateNumber = date.getDayOfMonth();
      date = date.minusDays(dateNumber-1);
      int day = date.getDayOfWeek().get(ChronoField.DAY_OF_WEEK)-1;
      int week = 1;

      monthLabel.setText(this.month.name());
      yearLabel.setText(Integer.toString(this.year));
      
      for (int i = 0; i<month.length(date.isLeapYear());i++){
        VBox pane = new VBox();
        Label dateLabel = new Label(Integer.toString(i+1));
        dateLabel.paddingProperty();
        pane.getChildren().add(dateLabel);
        calendar.add(pane,day,week);
        day = day+1;
        if (day%7 == 0) {
          day = 0;
          week += 1;
        }
      }
    }

    private void setMonth(Month month, int year) {
      setMonth(LocalDate.of(year, month, 1));
    }

    /**
     * Displays all events of the current month showing
     * 
     * @param events list of all events
     * @throws IllegalStateException if this.month is null
     */

    private void addEvents(List<Event> events) {
      if (this.month == null) {throw new IllegalStateException();}
      if (events != null) {
        List<Event> eventsInMonth = events.stream().filter(e -> e.getTime().getMonth()==this.month && e.getTime().getYear()==this.year).toList();
        for (Event event : eventsInMonth) {
          int dayOfMonth = event.getTime().getDayOfMonth();
          VBox pane = (VBox) calendar.getChildren().get(dayOfMonth+7);
          HBox eventPane = new HBox();

          eventPane.setStyle("-fx-background-color: #ffd078");
          eventPane.getChildren().add(new Label(event.getTitle()));

          ToggleButton joinButton = new ToggleButton("+");
          User user = UserSession.getUser();
          UserFileManager manager = new UserFileManager();
          joinButton.setOnAction(e -> {
            if (joinButton.getText().equals("-")) {
              joinButton.setText("+");
              System.out.println(user.isAttendingEvent(event.getTitle()));
              user.removeEventId(event.getTitle());
            } else {
              joinButton.setText("-");
              user.addEventId(event.getTitle());
            }
            manager.editUser(user.getUsername(), user);
          });
          
          if (user.isAttendingEvent(event.getTitle())) {
            joinButton.setText("-");
            joinButton.setSelected(true);
          }

          eventPane.getChildren().add(joinButton);
          HBox.setMargin(joinButton, new Insets(0, 0, 0, 10));

          pane.getChildren().add(eventPane);
        }
      }
    }


}


