package tn.esprit.easytripdesktopapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.AccountType;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.services.ServiceUser;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {

    @FXML
    private TextArea addressField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField profilePhotoField;

    @FXML
    private ComboBox<AccountType> roleComboBox;

    @FXML
    private ImageView image;

    @FXML
    private Button signUpButton;

    @FXML
    private Button backArrow;

    @FXML
    private Button backButton;

    @FXML
    private TextField surnameField;

    @FXML
    private Button chooseImageButton;

    private String imageUrl;

    @FXML
    void onClick(ActionEvent event) {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String role = String.valueOf(roleComboBox.getValue());

        if (name.isEmpty() || surname.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || role.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format!");
            return;
        }

        if (!isValidPhone(phone)) {
            showAlert("Error", "Invalid phone number format!");
            return;
        }

        User newUser = new User(name, surname, password, email, phone, address, imageUrl, role);
        ServiceUser serviceUser = new ServiceUser();
        serviceUser.add(newUser);

        if (serviceUser.getAll() != null) {
            showAlert("Success", "User created successfully!");
        } else {
            showAlert("Error", "User creation failed!");
        }

        resetFields();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void resetFields() {
        nameField.clear();
        surnameField.clear();
        passwordField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void reset(ActionEvent event) {
        resetFields();
    }

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList(AccountType.Agent, AccountType.Client));
        roleComboBox.getSelectionModel().select(AccountType.Client);
        imageUrl = "file:///home/cardinal/Documents/GitHub/easyTrip-desktop-app/src/main/resources/tn/esprit/easytripdesktopapp/assets/defaultPic.jpg";
        Image img = new Image(imageUrl);
        image.setImage(img);

    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void chooseImage() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));


        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {

            imageUrl = selectedFile.toURI().toString();


            Image img = new Image(imageUrl);
            image.setImage(img);
        }
    }

    // Email Validation
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^\\d{8}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

}