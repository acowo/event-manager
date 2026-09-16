module eventmanager.ui {
  requires javafx.controls;
  requires javafx.fxml;

  requires eventmanager.core;
  requires eventmanager.persistence;


  opens ui.controller to javafx.graphics, javafx.fxml;
  exports ui;
}
