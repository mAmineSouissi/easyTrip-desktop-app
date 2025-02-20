package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.services.ServiceUser;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TablesAdminController {
    private final ServiceUser serviceUser = new ServiceUser();
    private final ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML private ListView<User> userListView;

    @FXML
    public void initialize() {
        setupListView();
        loadUsers();
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
                    Label idLabel = new Label("ID:");
                    Label nameLabel = new Label("Name:");
                    Label surnameLabel = new Label("Surname:");
                    Label emailLabel = new Label("Email:");
                    Label phoneLabel = new Label("Phone:");
                    Label addressLabel = new Label("Address:");
                    Label roleLabel = new Label("Role:");

                    // Create Labels for values
                    Label idValue = new Label(String.valueOf(user.getId()));
                    Label nameValue = new Label(user.getName());
                    Label surnameValue = new Label(user.getSurname());
                    Label emailValue = new Label(user.getEmail());
                    Label phoneValue = new Label(user.getPhone());
                    Label addressValue = new Label(user.getAddress());
                    Label roleValue = new Label(user.getRole());

                    // Buttons for Edit and Delete
                    Button editBtn = new Button("Edit");
                    Button deleteBtn = new Button("Delete");

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
        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Surname:"), surnameField);
        grid.addRow(2, new Label("Email:"), emailField);
        grid.addRow(3, new Label("Phone:"), phoneField);
        grid.addRow(4, new Label("Address:"), addressField);
        grid.addRow(5, new Label("Role:"), roleField);

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
    public void handleLogOut(ActionEvent actionEvent) {
        Alert logoutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        logoutAlert.setTitle("Logout Confirmation");
        logoutAlert.setHeaderText("Are you sure you want to log out?");
        logoutAlert.setContentText("You will be redirected to the login screen.");

        logoutAlert.showAndWait().ifPresent(response -> {
            // If the user confirms , navigate to login screen
            if (response == ButtonType.OK) {
                UserSession session = UserSession.getInstance();
                session.clearSession();
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
