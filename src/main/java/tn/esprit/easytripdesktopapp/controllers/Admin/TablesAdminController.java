package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.services.ServiceUser;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TablesAdminController implements Initializable {
    private final ServiceUser serviceUser = new ServiceUser();
    private final ObservableList<User> userList = FXCollections.observableArrayList();

    public Label welcomeLabel;

    public Label manageOfferLabel;

    public Label otherOffersLabel;

    public Button logOutButton;

    public ImageView profilePic;
    public Button editProfile;
    public Label manageAgencies;
    public Label manageReservations;
    public Label manageWebinaire;
    public Label manageReclamations;
    public Label manageFeedbacks;
    public Label manageTickets;
    public Label manageHotels;
    public Label managePromotions;
    public Label manageTransports;
    public Label manageCars;

    @FXML
    private ListView<User> userListView;

    private ResourceBundle bundle;

    @FXML
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        setupListView();
        loadUsers();

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
        manageCars.setText(bundle.getString("manage_cars"));
        manageTransports.setText(bundle.getString("manage_transports"));
        managePromotions.setText(bundle.getString("manage_promotions"));
        manageAgencies.setText(bundle.getString("manage_agency"));
        manageReservations.setText(bundle.getString("manage_reservations"));
        manageWebinaire.setText(bundle.getString("manage_webinaire"));
        manageReclamations.setText(bundle.getString("manage_reclamations"));
        manageFeedbacks.setText(bundle.getString("manage-feedbacks"));
        manageTickets.setText(bundle.getString("manage_ticket"));
        manageHotels.setText(bundle.getString("manage_hotel"));
        manageOfferLabel.setText(bundle.getString("travel_offers"));
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

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle(bundle.getString("profile_updated_title"));
                successAlert.setHeaderText(bundle.getString("profile_updated_header"));
                successAlert.showAndWait();
            }
        });
    }

    private void setupListView() {
        userListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create Labels with field names
                    Label idLabel = new Label(bundle.getString("label.id"));
                    Label nameLabel = new Label(bundle.getString("label.name"));
                    Label surnameLabel = new Label(bundle.getString("label.surname"));
                    Label emailLabel = new Label(bundle.getString("label.email"));
                    Label phoneLabel = new Label(bundle.getString("label.phone"));
                    Label addressLabel = new Label(bundle.getString("label.address"));
                    Label roleLabel = new Label(bundle.getString("label.role"));

                    // Create Labels for values
                    Label idValue = new Label(String.valueOf(user.getId()));
                    Label nameValue = new Label(user.getName());
                    Label surnameValue = new Label(user.getSurname());
                    Label emailValue = new Label(user.getEmail());
                    Label phoneValue = new Label(user.getPhone());
                    Label addressValue = new Label(user.getAddress());
                    Label roleValue = new Label(user.getRole());

                    // Buttons for Edit and Delete
                    Button editBtn = new Button(bundle.getString("button.edit"));
                    Button deleteBtn = new Button(bundle.getString("button.delete"));

                    editBtn.getStyleClass().add("edit-button");
                    deleteBtn.getStyleClass().add("delete-button");

                    // Handle Button Actions
                    editBtn.setOnAction(e -> editUser(user));
                    deleteBtn.setOnAction(e -> deleteUser(user));

                    // GridPane to Structure Labels Like a Table
                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(5);

                    grid.addRow(0, idLabel, idValue);
                    grid.addRow(1, nameLabel, nameValue);
                    grid.addRow(2, surnameLabel, surnameValue);
                    grid.addRow(3, emailLabel, emailValue);
                    grid.addRow(4, phoneLabel, phoneValue);
                    grid.addRow(5, addressLabel, addressValue);
                    grid.addRow(6, roleLabel, roleValue);

                    // HBox for Buttons
                    HBox buttonBox = new HBox(10, editBtn, deleteBtn);
                    VBox itemBox = new VBox(grid, buttonBox);
                    itemBox.setSpacing(5);

                    setGraphic(itemBox);
                }
            }
        });
    }

    private void loadUsers() {
        try {
            List<User> users = serviceUser.getAll();
            userList.setAll(users);
            userListView.setItems(userList);
        } catch (Exception e) {
            showError("Error loading users", e.getMessage());
        }
    }

    private void editUser(User user) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        dialog.setHeaderText("Edit details for: " + user.getName());

        // Text fields for editing
        TextField nameField = new TextField(user.getName());
        TextField surnameField = new TextField(user.getSurname());
        TextField emailField = new TextField(user.getEmail());
        TextField phoneField = new TextField(user.getPhone());
        TextField addressField = new TextField(user.getAddress());
        TextField roleField = new TextField(user.getRole());

        // GridPane for Input Fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label(bundle.getString("label.name")), nameField);
        grid.addRow(1, new Label(bundle.getString("label.surname")), surnameField);
        grid.addRow(2, new Label(bundle.getString("label.email")), emailField);
        grid.addRow(3, new Label(bundle.getString("label.phone")), phoneField);
        grid.addRow(4, new Label(bundle.getString("label.address")), addressField);
        grid.addRow(5, new Label(bundle.getString("label.role")), roleField);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty() || addressField.getText().isEmpty() || roleField.getText().isEmpty()) {
                    showError("Validation Error", "All fields must be filled.");
                    return null;
                }

                if (!isValidEmail(emailField.getText())) {
                    showError("Validation Error", "Invalid email format.");
                    return null;
                }

                if (!isValidPhone(phoneField.getText())) {
                    showError("Validation Error", "Invalid phone number format.");
                    return null;
                }
                user.setName(nameField.getText());
                user.setSurname(surnameField.getText());
                user.setEmail(emailField.getText());
                user.setPhone(phoneField.getText());
                user.setAddress(addressField.getText());
                user.setRole(roleField.getText());
                return user;
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        result.ifPresent(updatedUser -> {
            try {
                serviceUser.update(updatedUser);
                loadUsers();
                showInfo("Success", "User updated successfully.");
            } catch (Exception e) {
                showError("Update Error", e.getMessage());
            }
        });
    }

    private void deleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Delete User");
        alert.setContentText("Are you sure you want to delete " + user.getName() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    serviceUser.delete(user);
                    userList.remove(user);
                    showInfo("Success", "User deleted successfully.");
                } catch (Exception e) {
                    showError("Delete Error", e.getMessage());
                }
            }
        });
    }


    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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

    public void navigateToAdminAgence(MouseEvent mouseEvent) {
        Stage stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/afficher_agence.fxml"));
            Parent root = loader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offer Travel");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void navigateToAdminTravel(MouseEvent mouseEvent) {
        Stage stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/afficher_offer_travel.fxml"));
            Parent root = loader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offer Travel");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateToPromotionAdmin(MouseEvent mouseEvent) {
        Stage stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/afficher_promotion.fxml"));
            Parent root = loader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offer Travel");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateToReclamationAdmin(MouseEvent mouseEvent) {
        Stage stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/ReclamationAdmin.fxml"));
            Parent root = loader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offer Travel");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void navigateToWebinaireAdmin(MouseEvent mouseEvent) {
        Stage stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/AdminWebinaire.fxml"));
            Parent root = loader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Webinaire");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void navigateToAdminFeedbacks(MouseEvent mouseEvent) {
        Stage stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/FeedbackAdmin.fxml"));
            Parent root = loader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offer Travel");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void showCharts(MouseEvent event) {
        try {
            // Load the Charts.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/Charts.fxml"));
            Parent root = loader.load();

            // Create a new stage for the charts
            Stage chartsStage = new Stage();
            chartsStage.setTitle("Charts");
            chartsStage.setScene(new Scene(root));

            // Show the charts stage
            chartsStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed, e.g., show an alert to the user
        }
    }


    public void navigateToTransport(MouseEvent mouseEvent) {
            Stage stage;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/AdminReservation.fxml"));
                Parent root = loader.load();
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Offer Travel");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    public void navigateToCars(MouseEvent mouseEvent) {
        Stage stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/Admin.fxml"));
            Parent root = loader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Offer Travel");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}