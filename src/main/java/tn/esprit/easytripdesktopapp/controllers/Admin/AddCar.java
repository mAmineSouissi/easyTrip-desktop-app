package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import tn.esprit.easytripdesktopapp.models.cars;
import tn.esprit.easytripdesktopapp.services.CarsService;

import java.io.File;
import java.io.IOException;


public class AddCar {
    @FXML
    private TextField carLocation;

    @FXML
    private TextField model;

    @FXML
    private TextField price;

    @FXML
    private TextField seats;

    @FXML
    private TextField image;


    private CarsService cs = new CarsService();

    @FXML
    void save(ActionEvent event) {
        // Get the image URL from the TextField
        String modelText = model.getText();
        String seatsText = seats.getText();
        String carLocationText = carLocation.getText();
        String priceText = price.getText();
        String img = image.getText();// Renamed imageURL to image
        // Validate that all fields are filled
        if (modelText.isEmpty() || seatsText.isEmpty() || carLocationText.isEmpty() || priceText.isEmpty() || img.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        // Validate seats: must be a number and not exceed 9
        int seatsValue;
        try {
            seatsValue = Integer.parseInt(seatsText);
            if (seatsValue <= 0 || seatsValue > 9) {
                showAlert("Error", "Seats must be a number between 1 and 9.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Seats must be a valid number.");
            return;
        }

        // Validate price: must be a valid number (integer or decimal)
        float priceValue;
        try {
            priceValue = Float.parseFloat(priceText);
            if (priceValue <= 0) {
                showAlert("Error", "Price must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Price must be a valid number.");
            return;
        }
        // Check if img is null or empty and set a default image if necessary
        if (img == null || img.isEmpty()) {
            img = "default_image_url";  // Provide a fallback URL or leave it null
        }

        // Create a new car object using the data from the TextFields
        cars newCar = new cars(
                model.getText(),  // Model name from the TextField
                Integer.parseInt(seats.getText()),  // Seats from the TextField
                carLocation.getText(),  // Location from the TextField
                Float.parseFloat(price.getText()),  // Price from the TextField
                img
        );
        newCar.setStatus(cars.availability.AVAILABLE);// Image URL from the TextField

        // Print out the car data for debugging
        System.out.println("Adding car: " + newCar);

        // Add the car to the database
        cs.addCars(newCar);
        showAlert("Success", "Car added successfully!");
    }
    @FXML
    void show(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/Admin.fxml"));
            model.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void upload_img(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload your profile picture");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String fileName = selectedFile.getName().toLowerCase();
            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                image.setText(selectedFile.getPath());
            } else {
                System.out.println("Invalid file format. Please select a PNG or JPG file.");
            }
        } else {
            System.out.println("No file selected");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
