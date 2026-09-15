module ui {
  requires javafx.controls;
  requires javafx.fxml;
  requires core;

  opens ui.controller to javafx.graphics, javafx.fxml;
  exports ui;
}
