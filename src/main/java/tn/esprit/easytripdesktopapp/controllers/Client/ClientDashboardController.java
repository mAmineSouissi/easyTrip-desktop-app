package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.utils.UserSession;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import tn.esprit.easytripdesktopapp.services.ServiceUser;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientDashboardController implements Initializable {
    public ImageView profilePic;
    public Button logOutButton;
    public Label manageOfferLabel;
    public Label otherOffersLabel;
    public Label coming_soon;
    public Label manageOfferHotel;
    public Label hotelOffersLabel;
    public Label ticketsOffersLabel;
    @FXML
    private Label welcomeLabel;

    @FXML
    private Button editProfile;

    @FXML
    private ImageView profilePicture;

    @FXML
    private TextField profilePhotoField;

    private ResourceBundle bundle;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        bundle = resources;

        UserSession session = UserSession.getInstance();
        String imageUrl = getUserProfileImageUrl();

        if (session != null) {
            User user = session.getUser();
            welcomeLabel.setText(bundle.getString("welcome_label") + " " + user.getName() + " " + user.getSurname());

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
            welcomeLabel.setText(bundle.getString("welcome_guest"));
        }
        coming_soon.setText(bundle.getString("coming_soon"));
        manageOfferLabel.setText(bundle.getString("travel_offers"));
        hotelOffersLabel.setText(bundle.getString("offer_hotel"));
        ticketsOffersLabel.setText(bundle.getString("offer_tickets"));
        editProfile.setText(bundle.getString("profile_button"));
        logOutButton.setText(bundle.getString("logout_button"));
    }

    private String getUserProfileImageUrl() {
        UserSession session = UserSession.getInstance();
        if (session != null) {
            User user = session.getUser();
            return user.getProfilePhoto();
        } else {
            return "http://localhost/img/profile/defaultPic.jpg";
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
        grid.addRow(0, new Label(bundle.getString("name_label")), nameField);
        grid.addRow(1, new Label(bundle.getString("surname_label")), surnameField);
        grid.addRow(2, new Label(bundle.getString("email_label")), emailField);
        grid.addRow(3, new Label(bundle.getString("phone_label")), phoneField);
        grid.addRow(4, new Label(bundle.getString("address_label")), addressField);

        Button chooseImageButton = new Button(bundle.getString("choose_profile_picture"));
        chooseImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

            if (selectedFile != null) {
                String fileName = selectedFile.getName();
                String baseUrl = "http://localhost/img/profile/";
                String newImageUrl = baseUrl + fileName;

                profilePhotoField.setText(newImageUrl);
                profilePreview.setImage(new Image(newImageUrl, true));
            }
        });

        grid.addRow(5, new Label(bundle.getString("profile_photo_label")), chooseImageButton);
        grid.addRow(6, profilePreview);

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("edit_profile_title"));
        alert.setHeaderText(bundle.getString("update_profile_header"));
        alert.getDialogPane().setContent(grid);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty() || addressField.getText().isEmpty()) {
                    showError(bundle.getString("validation_error_title"), bundle.getString("validation_error_empty_fields"));
                    return;
                }

                if (!isValidEmail(emailField.getText())) {
                    showError(bundle.getString("validation_error_title"), bundle.getString("validation_error_invalid_email"));
                    return;
                }

                if (!isValidPhone(phoneField.getText())) {
                    showError(bundle.getString("validation_error_title"), bundle.getString("validation_error_invalid_phone"));
                    return;
                }

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
                welcomeLabel.setText(bundle.getString("welcome_label") + " " + updatedUser.getName());
                profilePic.setImage(new Image(updatedUser.getProfilePhoto(), true));

                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle(bundle.getString("profile_updated_title"));
                successAlert.setHeaderText(bundle.getString("profile_updated_header"));
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
            // Extract the filename only
            String fileName = selectedFile.getName();

            // Keep the base URL and update only the filename
            String baseUrl = "http://localhost/img/profile/";
            String newImageUrl = baseUrl + fileName;

            // Update the text field and image preview
            profilePhotoField.setText(newImageUrl);
            Image image = new Image(newImageUrl, true);
            profilePicture.setImage(image);
        }
    }

    @FXML
    public void handelLogout(ActionEvent actionEvent) {
        Alert logoutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        logoutAlert.setTitle(bundle.getString("logout_confirmation_title"));
        logoutAlert.setHeaderText(bundle.getString("logout_confirmation_header"));
        logoutAlert.setContentText(bundle.getString("logout_confirmation_content"));

        logoutAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                UserSession session = UserSession.getInstance();
                session.clearSession();
                System.out.println("User logged out successfully.");
                try {
                    ResourceBundle loginBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Login.fxml"), loginBundle);
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle(bundle.getString("login_screen_title"));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Logout cancelled.");
            }
        });
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

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

    public void navigateToHotelAffichage(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/AffichageHotelClient.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offre hotels");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateToReservation(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Reservation/addreservation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateToTicktsAffichage(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/AffichageTicketClient.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offer Tickets");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
