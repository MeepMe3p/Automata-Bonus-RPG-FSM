//module com.example.activity {
//    requires javafx.controls;
//    requires javafx.fxml;
//
//    requires com.almasb.fxgl.all;
//
//    opens com.example.activity to javafx.fxml;
//    exports com.example.activity;
//}
open module activity{
        requires com.almasb.fxgl.all;
        requires java.desktop;
        requires java.sql;
        requires javafx.media;
        requires com.fasterxml.jackson.databind;



        }