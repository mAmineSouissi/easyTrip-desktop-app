package tn.esprit.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import tn.esprit.models.Res_Transport;
import tn.esprit.services.ResTransportService;
import tn.esprit.utils.MyDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class UpdateResTransport {
    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<Res_Transport.Status> statusComboBox;

    private int ResId;
    private ResTransportService resTransportService = new ResTransportService();
    private int carId;
    private int userId;



    // Method to set the selected reservation data
    public void setResTransportData(Res_Transport resTransport) {

        this.carId = resTransport.getCarId();
        this.ResId = resTransport.getId();
        this.userId = resTransport.getUserId();


        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();




        // Populate the form with the selected reservation data
        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);

        statusComboBox.setValue(resTransport.getStatus());

    }

    // Method to handle the update button click
    @FXML
    private void handleUpdateReservation(ActionEvent event) {
        // Get the updated values from the form
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Res_Transport.Status status = statusComboBox.getValue();

        // Validate the input
        if (startDate == null || endDate == null || status == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }
        if (startDate.isAfter(endDate)) {
            showAlert("Error", "The start date must be before the end date.");
            return;
        }

        // Update the selected reservation
        // Convert LocalDate to java.sql.Date
        java.util.Date sqlStartDate = Date.valueOf(startDate);
        java.util.Date sqlEndDate = Date.valueOf(endDate);

        Res_Transport resTransport = new Res_Transport(ResId,userId,carId,sqlStartDate,sqlEndDate,status);

        System.out.println(resTransport);

        // Call the service to update the reservation
        resTransportService.UpdateResTransport(resTransport);
        showAlert("Success", "Reservation added successfully!");



        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowResTransport1.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Get the UpdateResTransport controlle
        // Show the UpdateResTransport page
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();



        // Show success message

    }

    // Helper method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
