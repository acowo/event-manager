package ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX application entry point.
 *
 * <p>Loads {@code /ui/App.fxml} (from {@code ui/src/main/resources/ui/App.fxml})
 * and shows it in the primary stage. This is the class referenced by the
 * {@code javafx-maven-plugin} configuration in {@code ui/pom.xml}
 * ({@code calc.ui/ui.App}), so running {@code mvn javafx:run} from the {@code ui}
 * module (or the parent, if aggregated) launches this class.</p>
 */
public class CalendarApp extends Application {

  @Override
  public void start(final Stage stage) throws IOException {
    final Parent root = FXMLLoader.load(getClass().getResource("/ui/Calendar.fxml"));
    stage.setTitle("Event Manager Calendar");
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Standard main method, useful for launching without the javafx-maven-plugin
   * (e.g. directly from an IDE run configuration).
   *
   * @param args command-line arguments
   */
  public static void main(final String[] args) {
    launch(args);
  }
}
