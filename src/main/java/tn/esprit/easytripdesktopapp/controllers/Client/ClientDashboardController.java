package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.utils.UserSession;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import tn.esprit.easytripdesktopapp.services.ServiceUser;

import java.io.File;
import java.io.IOException;

public class ClientDashboardController {
    public ImageView profilePic;
    @FXML
    private Label welcomeLabel;

    @FXML
    private Button editProfile;

    @FXML
    private ImageView profilePicture;

    @FXML
    private TextField profilePhotoField;

    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();
        String imageUrl = getUserProfileImageUrl();

        if (session != null) {
            User user = session.getUser();
            welcomeLabel.setText("Welcome, " + user.getName());

            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    Image profileImage = new Image(imageUrl, true);
                    profilePic.setImage(profileImage);
                } catch (Exception e) {
                    System.out.println("Error loading image: " + e.getMessage());
                }
            } else {
                System.out.println("No image URL found.");
            }
        } else {
            welcomeLabel.setText("Welcome, Guest");
        }
    }

    private String getUserProfileImageUrl() {
        UserSession session = UserSession.getInstance();
        if (session != null) {
            User user = session.getUser();
            return user.getProfilePhoto();
        } else {
            return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqafzhnwwYzuOTjTlaYMeQ7hxQLy_Wq8dnQg&s";
        }
    }

    @FXML
    public void handelEditProfile(ActionEvent actionEvent) {
        // Initialize text fields
        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        TextField profilePhotoField = new TextField();

        // Create an ImageView for previewing the selected image
        ImageView profilePreview = new ImageView();
        profilePreview.setFitWidth(100);
        profilePreview.setFitHeight(100);
        profilePreview.setPreserveRatio(true);

        UserSession session = UserSession.getInstance();
        if (session != null) {
            User currentUser = session.getUser();
            nameField.setText(currentUser.getName());
            surnameField.setText(currentUser.getSurname());
            emailField.setText(currentUser.getEmail());
            phoneField.setText(currentUser.getPhone());
            addressField.setText(currentUser.getAddress());
            profilePhotoField.setText(currentUser.getProfilePhoto());
            System.out.println("Before setUser: " + currentUser.getPassword());

            // Load the existing profile image
            if (currentUser.getProfilePhoto() != null && !currentUser.getProfilePhoto().isEmpty()) {
                profilePreview.setImage(new Image(currentUser.getProfilePhoto(), true));
            }
        }

        GridPane grid = new GridPane();
        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Surname:"), surnameField);
        grid.addRow(2, new Label("Email:"), emailField);
        grid.addRow(3, new Label("Phone:"), phoneField);
        grid.addRow(4, new Label("Address:"), addressField);

        Button chooseImageButton = new Button("Choose Profile Picture");
        chooseImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

            if (selectedFile != null) {
                String imagePath = "file://" + selectedFile.getAbsolutePath();
                profilePhotoField.setText(imagePath);
                profilePreview.setImage(new Image(imagePath));
            }
        });

        grid.addRow(5, new Label("Profile Photo:"), chooseImageButton);
        grid.addRow(6, profilePreview); // Add the preview image below the button

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Edit Profile");
        alert.setHeaderText("Update your profile details");
        alert.getDialogPane().setContent(grid);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                User updatedUser = new User();
                updatedUser.setId(session.getUser().getId());
                updatedUser.setName(nameField.getText());
                updatedUser.setSurname(surnameField.getText());
                updatedUser.setEmail(emailField.getText());
                updatedUser.setPhone(phoneField.getText());
                updatedUser.setAddress(addressField.getText());
                updatedUser.setProfilePhoto(profilePhotoField.getText());
                updatedUser.setPassword(session.getUser().getPassword());
                updatedUser.setRole(session.getUser().getRole());

                System.out.println("Updating user password: " + updatedUser.getPassword());

                // Save updated user to the database
                ServiceUser serviceUser = new ServiceUser();
                serviceUser.update(updatedUser);

                // Update session user
                session.setUser(updatedUser);

                // Update UI after saving changes
                welcomeLabel.setText("Welcome, " + updatedUser.getName());
                profilePic.setImage(new Image(updatedUser.getProfilePhoto(), true));

                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Profile Updated");
                successAlert.setHeaderText("Your profile has been updated successfully.");
                successAlert.showAndWait();
            }
        });
    }

    @FXML
    private void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            // Update the text field with the file path
            profilePhotoField.setText(selectedFile.getAbsolutePath());

            // Update the image preview
            String imagePath = "file://" + selectedFile.getAbsolutePath();
            Image image = new Image(imagePath);
            profilePicture.setImage(image);
        }
    }

    @FXML
    public void handelLogout(ActionEvent actionEvent) {
        // Clear the user session
        UserSession session = UserSession.getInstance();
        session.clearSession();

        // Confirmation logout Alert Logic
        Alert logoutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        logoutAlert.setTitle("Logout Confirmation");
        logoutAlert.setHeaderText("Are you sure you want to log out?");
        logoutAlert.setContentText("You will be redirected to the login screen.");

        logoutAlert.showAndWait().ifPresent(response -> {
            // If the user confirms , navigate to login screen
            if (response == ButtonType.OK) {
                System.out.println("User logged out successfully.");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Login.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Login Screen");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Logout cancelled.");
            }
        });
    }

}
