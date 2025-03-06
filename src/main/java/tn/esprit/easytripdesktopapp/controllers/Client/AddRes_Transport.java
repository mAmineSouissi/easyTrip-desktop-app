package tn.esprit.easytripdesktopapp.controllers.Client;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import tn.esprit.easytripdesktopapp.models.Res_Transport;
import tn.esprit.easytripdesktopapp.services.ResTransportService;

public class AddRes_Transport {

    @FXML
    private DatePicker dateDebPicker; // DatePicker for start date

    @FXML
    private DatePicker dateFinPicker; // DatePicker for end date

    private int carId; // Store the car ID passed from ShowCar

    private ResTransportService resTransportService = new ResTransportService(); // Service to handle reservation logic

    // Method to set the car ID
    public void setCarId(int carId) {
        this.carId = carId;
    }
    @FXML
    void addRes(ActionEvent event) {
// Get the selected dates from the DatePickers
        LocalDate startDate = dateDebPicker.getValue();
        LocalDate endDate = dateFinPicker.getValue();

        // Validate the selected dates
        if (startDate == null || endDate == null) {
            showAlert("Error", "Please select both start and end dates.");
            return;
        }
        if (startDate.isAfter(endDate)) {
            showAlert("Error", "The start date must be before the end date.");
            return;
        }
        // Convert LocalDate to java.sql.Date
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        // Example user ID (replace with the logged-in user's ID)
        int userId = 1; // Replace with the actual user ID

        // Add the reservation using the service
      resTransportService.addResTransport(userId, carId, sqlStartDate, sqlEndDate, Res_Transport.Status.IN_PROGRESS);
        showAlert("Success", "Reservation added successfully!");

    }


    // Helper method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void initialize() {

    }

}
