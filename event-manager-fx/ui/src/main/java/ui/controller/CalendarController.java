
package ui.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;

import event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import persistence.EventFileManager;
import persistence.UserFileManager;
import user.User;
import user.UserSession;
import event.Event;


/**
 * Controller for {@code /ui/CalendarApp.fxml}. 
 */


public class CalendarController {
    private Month month;
    private int year;

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
      LocalDate date = LocalDate.now();
      setMonth(date);

      Map<String, Event> events = EventFileManager.getEvents();
      addEvents(events.values().stream().toList());
    }

    private void setMonth(LocalDate date) {
      if (date == null) {throw new NullPointerException();}
      this.month = date.getMonth();
      this.year = date.getYear();
      int dateNumber = date.getDayOfMonth();
      date = date.minusDays(dateNumber-1);
      int day = date.getDayOfWeek().get(ChronoField.DAY_OF_WEEK)-1;
      int week = 1;

      monthLabel.setText(this.month.name());
      
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
              //TODO: Remove user from event
              System.out.println(user.isAttendingEvent(event.getTitle()));
              user.removeEventId(event.getTitle());
            } else {
              joinButton.setText("-");
              //TODO: Add user to event
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


