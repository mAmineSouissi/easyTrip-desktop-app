module tn.esprit.easytripdesktopapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens tn.esprit.easytripdesktopapp to javafx.fxml;
    opens tn.esprit.easytripdesktopapp.controllers to javafx.fxml;
    opens tn.esprit.easytripdesktopapp.models to javafx.fxml;
    opens tn.esprit.easytripdesktopapp.services to javafx.fxml;

    exports tn.esprit.easytripdesktopapp;
    exports tn.esprit.easytripdesktopapp.controllers;
    exports tn.esprit.easytripdesktopapp.models;
    exports tn.esprit.easytripdesktopapp.services;
}