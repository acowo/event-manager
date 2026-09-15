module ui {
  requires javafx.controls;
  requires javafx.fxml;

  opens ui.controller to javafx.graphics, javafx.fxml;
  exports ui;
}
