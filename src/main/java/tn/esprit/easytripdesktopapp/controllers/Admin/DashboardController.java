package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.services.ServiceUser;

import java.io.IOException;
import java.util.List;

public class DashboardController {
    private final ServiceUser serviceUser = new ServiceUser();
    private final ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> surnameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> phoneColumn;
    @FXML private TableColumn<User, String> addressColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, Void> actionsColumn;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupActionsColumn();
        loadUsers();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        surnameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSurname()));
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        phoneColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPhone()));
        addressColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));
        roleColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox box = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("edit-button");
                deleteBtn.getStyleClass().add("delete-button");

                editBtn.setOnAction(e -> {
                    User user = getTableRow().getItem();
                    if (user != null) {
                        editUser(user);
                    }
                });

                deleteBtn.setOnAction(e -> {
                    User user = getTableRow().getItem();
                    if (user != null) {
                        deleteUser(user);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void loadUsers() {
        try {
            System.out.println("Loading users...");
            List<User> users = serviceUser.getAll();
            System.out.println("Retrieved users: " + users.size());
            userList.setAll(users);
            userTable.setItems(userList);
            System.out.println("Users loaded into table");
        } catch (Exception e) {
            System.out.println("Error in loadUsers(): " + e.getMessage());
            e.printStackTrace();
            showError("Error loading users", e.getMessage());
        }
    }

    private void editUser(User user) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        dialog.setHeaderText("Edit user: " + user.getName());

        // Create the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField nameField = new TextField(user.getName());
        TextField surnameField = new TextField(user.getSurname());
        TextField emailField = new TextField(user.getEmail());
        TextField phoneField = new TextField(user.getPhone());
        TextField addressField = new TextField(user.getAddress());
        ComboBox<String> roleField = new ComboBox<>(FXCollections.observableArrayList("ADMIN", "USER"));
        roleField.setValue(user.getRole());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Surname:"), 0, 1);
        grid.add(surnameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);
        grid.add(new Label("Role:"), 0, 5);
        grid.add(roleField, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                user.setName(nameField.getText());
                user.setSurname(surnameField.getText());
                user.setEmail(emailField.getText());
                user.setPhone(phoneField.getText());
                user.setAddress(addressField.getText());
                user.setRole(roleField.getValue());
                return user;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedUser -> {
            try {
                serviceUser.update(updatedUser);
                loadUsers();
                showInfo("Success", "User updated successfully");
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
                    loadUsers();
                    showInfo("Success", "User deleted successfully");
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
    public void handleLogOut(ActionEvent event) {
        final Stage[] stage = new Stage[1];
        final Scene[] scene = new Scene[1];
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("You will be redirected to the login screen.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Perform log out logic here (e.g., close the app, redirect to login screen)
                System.out.println("User logged out.");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Login.fxml"));
                stage[0] = (Stage)((Node)event.getSource()).getScene().getWindow();
                try {
                    scene[0] = new Scene(loader.load());
                    stage[0].setScene(scene[0]);
                    stage[0].setTitle("Login Screen");
                    stage[0].show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
}