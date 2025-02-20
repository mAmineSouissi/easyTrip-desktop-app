module tn.esprit.easytripdesktopapp {
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.controls;

    opens tn.esprit.easytripdesktopapp to javafx.fxml;
    opens tn.esprit.easytripdesktopapp.controllers to javafx.fxml;
    opens tn.esprit.easytripdesktopapp.models to javafx.fxml;
    opens tn.esprit.easytripdesktopapp.services to javafx.fxml;
    opens tn.esprit.easytripdesktopapp.controllers.Admin to javafx.fxml;
    opens tn.esprit.easytripdesktopapp.controllers.Agent to javafx.fxml;
    opens tn.esprit.easytripdesktopapp.controllers.Client to javafx.fxml;


    exports tn.esprit.easytripdesktopapp;
    exports tn.esprit.easytripdesktopapp.controllers;
    exports tn.esprit.easytripdesktopapp.models;
    exports tn.esprit.easytripdesktopapp.services;
    exports tn.esprit.easytripdesktopapp.utils;
    opens tn.esprit.easytripdesktopapp.utils to javafx.fxml;
}