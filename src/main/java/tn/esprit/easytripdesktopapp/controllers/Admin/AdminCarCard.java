package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.esprit.models.cars;
import tn.esprit.services.CarsService;
import tn.esprit.test.MainFx;
import tn.esprit.test.MyListener;
import tn.esprit.models.cars;


import java.io.File;
import java.io.IOException;

public class AdminCarCard {

    @FXML
    private Label LocationLabel;

    @FXML
    private Label ModelLabel;

    @FXML
    private Label PriceLabel;

    @FXML
    private Label SeatsLabel;

    @FXML
    private ImageView img;

    private cars car;
    private CarsService cs = new CarsService();

    public void setCarAdminData(cars car) {
        this.car = car;
        LocationLabel.setText(car.getLocation());
        ModelLabel.setText(car.getModel());
        PriceLabel.setText(MainFx.CURRENCY + car.getPrice());
        SeatsLabel.setText(Integer.toString(car.getSeats()));

        // Handle image loading
        if (car.getImage() == null || car.getImage().isEmpty()) {
            System.out.println("Image non trouvée pour la voiture : " + car.getModel());
        } else {
            try {
                File file = new File(car.getImage());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    img.setImage(image);
                } else {
                    System.out.println("Fichier image introuvable : " + car.getImage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erreur lors du chargement de l'image : " + car.getImage());
            }
        }
    }
    @FXML
    void Delete(ActionEvent event) {
        if (car != null) {
            // Pass the ID of the selected car to delete
            cs.deleteCars(car.getId()); // Use the car's ID for deletion

            // Remove the car from the ListView after deletion


            System.out.println("Car deleted: " + car);
        } else {
            System.out.println("No car selected!");
        }
    }

    @FXML
    void GoToUpdate(ActionEvent event) {
        if (car == null) {  // Check if a car is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a car to update.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCar.fxml"));
            Parent root = loader.load();

            // Get controller instance
            UpdateCar controller = loader.getController();
            controller.setCarData(car); // Pass selected car data

            // Change scene
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Error loading UpdateCar.fxml: " + e.getMessage());
        }
    }

}
