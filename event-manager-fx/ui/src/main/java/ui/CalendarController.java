package ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
 * Controller for {@code /ui/CalendarApp.fxml}. 
 */
public class CalendarController {
    private Month month;
    private int year;

    @FXML
    private GridPane calendar;

    @FXML
    private Label monthLabel;

    @FXML
    public void initialize() {
      System.out.println("day");
      LocalDate date = LocalDate.now();
      setMonth(date);
    }

    private void setMonth(LocalDate date) {
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

    private void addEvents(List<Event> events) {
      if (events != null) {
        List<Event> eventsInMonth = events.stream().filter(e -> e.getTime().getMonth()==this.month && e.getTime().getYear()==this.year).toList();
        for (Event event : eventsInMonth) {
          int dayOfMonth = event.getTime().getDayOfMonth();
          VBox pane = (VBox) calendar.getChildren().get(dayOfMonth+7);
          HBox eventPane = new HBox();

          eventPane.setStyle("-fx-background-color: #ffd078");
          eventPane.getChildren().add(new Label(event.getTitle()));

          ToggleButton joinButton = new ToggleButton("+");
          joinButton.setOnAction(e -> {
            if (joinButton.getText().equals("-")) {
              joinButton.setText("+");
              //TODO: Remove user from event
            } else {
              joinButton.setText("-");
              //TODO: Add user to event
            }
          });

          eventPane.getChildren().add(joinButton);
          HBox.setMargin(joinButton, new Insets(0, 0, 0, 10));

          pane.getChildren().add(eventPane);
        }
      }
    }


}
