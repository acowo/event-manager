module eventmanager.ui {
  requires javafx.controls;
  requires javafx.fxml;

  requires api.contract;

  requires java.net.http;
  requires com.google.gson;
  requires com.fasterxml.jackson.databind;
  requires com.fasterxml.jackson.datatype.jsr310;


  opens ui.controller to javafx.graphics, javafx.fxml;
  exports ui;
}
