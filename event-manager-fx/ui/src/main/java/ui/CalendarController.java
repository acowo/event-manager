package ui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


/**
 * Controller for {@code /ui/App.fxml}. Wires the calculator buttons declared
 * in the FXML to the {@link } operand stack from the core module.
 */
public class CalendarController {
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
      Month month = date.getMonth();
      int dateNumber = date.getDayOfMonth();
      date = date.minusDays(dateNumber-1);
      int day = date.getDayOfWeek().get(ChronoField.DAY_OF_WEEK)-1;
      int week = 1;

      monthLabel.setText(month.name());
      
      for (int i = 0; i<month.length(date.isLeapYear());i++){
        AnchorPane pane = new AnchorPane();
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


    private void changeScene(Event event, String filepath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(filepath));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.show();
    }

}
