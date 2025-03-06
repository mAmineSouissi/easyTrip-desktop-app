package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import tn.esprit.models.Res_Transport;
import tn.esprit.services.ResTransportService;
import tn.esprit.test.MyListener1;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class AdminResCard {
    @FXML
    private Label DateDebLabel;

    @FXML
    private Label DateFinLabel;

    @FXML
    private Label StatuesLabel;


    private Res_Transport res_transport;
    private ResTransportService rt = new ResTransportService();

    public void setResAdminData(Res_Transport res_transport) {
        this.res_transport = res_transport;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String startDateFormatted = dateFormatter.format(res_transport.getStartDate());
        String endDateFormatted = dateFormatter.format(res_transport.getEndDate());
        DateDebLabel.setText(startDateFormatted);
        DateFinLabel.setText(endDateFormatted);
        StatuesLabel.setText(res_transport.getStatus().toString());
    }
    @FXML
    void DeleteRes(ActionEvent event) {
        if (res_transport != null) {
            // Pass the ID of the selected car to delete
            rt.DeleteResTransport(res_transport.getId()); // Use the car's ID for deletion

            // Remove the car from the ListView after deletion


            System.out.println("Reservation deleted: " + res_transport);
        } else {
            System.out.println("No car selected!");
        }
    }

    @FXML
    void GoToUpdateResAdmin(ActionEvent event) {
        if (res_transport == null) {  // Check if a car is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a car to update.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminReservation.fxml"));
            Parent root = loader.load();

            // Get controller instance
            AdminReservation controller = loader.getController();
            controller.setReservationData(res_transport); // Pass selected car data

            // Change scene
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Error loading UpdateCar.fxml: " + e.getMessage());
        }
    }
}
