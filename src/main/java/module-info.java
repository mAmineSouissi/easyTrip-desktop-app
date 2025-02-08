module tn.esprit.easytripdesktopapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens tn.esprit.easytripdesktopapp to javafx.fxml;
    exports tn.esprit.easytripdesktopapp;
}