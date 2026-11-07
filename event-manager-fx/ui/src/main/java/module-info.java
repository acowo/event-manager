module eventmanager.ui {
  requires javafx.controls;
  requires javafx.fxml;

  requires eventmanager.core;
  requires eventmanager.persistence;
  requires api.contract;

  requires java.net.http;
  requires com.google.gson;


    opens ui.controller to javafx.graphics, javafx.fxml;
  exports ui;
}
