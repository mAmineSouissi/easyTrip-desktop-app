package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.services.CarsService;
import tn.esprit.easytripdesktopapp.models.cars;
import tn.esprit.easytripdesktopapp.services.CarsService;

import java.io.File;
import java.io.IOException;

public class UpdateCar {

    @FXML
    private TextField model_textfield;

    @FXML
    private TextField price_textfield;

    @FXML
    private TextField location_textfield;

    @FXML
    private TextField seats_textfield;
    @FXML
    private TextField image_textfield;
    @FXML
    private ComboBox<cars.availability> statue_combobox;

    private CarsService carsService = new CarsService();
    private int carId; // Store the ID of the car to update

    @FXML
    public void initialize() {
        statue_combobox.getItems().addAll(cars.availability.values());

    }

    // Method to pre-fill data when editing a car
    public void setCarData(cars car) {
        this.carId = car.getId();
        model_textfield.setText(car.getModel());
        seats_textfield.setText(String.valueOf(car.getSeats()));
        location_textfield.setText(car.getLocation());
        price_textfield.setText(String.valueOf(car.getPrice()));
        image_textfield.setText(car.getImage());
        statue_combobox.setValue(car.getStatus());
    }

    @FXML
    void edit(ActionEvent event) {
        try {
            String model = model_textfield.getText();
            String seatsText = seats_textfield.getText();
            String location = location_textfield.getText();
            String priceText = price_textfield.getText();
            String image = image_textfield.getText();
            cars.availability status = statue_combobox.getValue();  // Get the selected status


            if (model.isEmpty() || seatsText.isEmpty() || location.isEmpty() || priceText.isEmpty() || image.isEmpty() || status == null) {
                showAlert("Error", "All fields are required, including status!");
                return;
            }

            // Validate seats: must be a number and not exceed 9
            int seats;
            try {
                seats = Integer.parseInt(seatsText);
                if (seats <= 0 || seats > 9) {
                    showAlert("Error", "Seats must be a number between 1 and 9.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Seats must be a valid number.");
                return;
            }

            // Validate price: must be a valid number (integer or decimal)
            float price;
            try {
                price = Float.parseFloat(priceText);
                if (price <= 0) {
                    showAlert("Error", "Price must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Price must be a valid number.");
                return;
            }


            cars updatedCar = new cars(carId, model, seats, location, price, image, status); // Include status in the constructor
            carsService.updateCars(updatedCar);

            showAlert("Success", "Car updated successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter numbers for seats and price.");
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin.fxml"));
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
    }

    @FXML
    void upload_img(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload your profile picture");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String fileName = selectedFile.getName().toLowerCase();
            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                image_textfield.setText(selectedFile.getPath());
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

    public void goBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/Admin.fxml"));
            Parent root = loader.load();
            // Change scene
            ((javafx.scene.Node) actionEvent.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Error loading UpdateCar.fxml: " + e.getMessage());
        }
    }
}
