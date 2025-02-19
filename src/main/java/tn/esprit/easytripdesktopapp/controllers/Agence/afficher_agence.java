package tn.esprit.easytripdesktopapp.controllers.Agence;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class afficher_agence implements Initializable {

    @FXML
    private ListView<Agence> listViewAgences;

    private final ServiceAgence serviceAgence = new ServiceAgence();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAgences();
    }

    @FXML
    public void loadAgences() {
        System.out.println("Chargement des agences...");

        listViewAgences.getItems().clear();

        List<Agence> agences = serviceAgence.getAll();

        listViewAgences.getItems().addAll(agences);

        listViewAgences.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Agence agence, boolean empty) {
                super.updateItem(agence, empty);
                if (empty || agence == null) {
                    setText(null);
                } else {

                    HBox hbox = new HBox();


                    String details = "📌 Nom: " + agence.getNom() + "\n" +
                            "📍 Adresse: " + agence.getAddress() + "\n" +
                            "📞 Téléphone: " + agence.getPhone() + "\n" +
                            "📧 Email: " + agence.getEmail() + "\n" +
                            "🖼️ Image: " + agence.getImage();
                    setText(details);

                    Button deleteButton = new Button("Supprimer");
                    deleteButton.setOnAction(event -> {
                        deleteAgence(agence);
                    });

                    hbox.getChildren().addAll(deleteButton);
                    setGraphic(hbox);
                }
            }
        });
    }

    @FXML
    public void refreshAgences(ActionEvent event) {
        loadAgences();
    }

    private void deleteAgence(Agence agence) {

        serviceAgence.delete(agence);
        loadAgences();
    }

    @FXML
    public void openUpdateAgence(ActionEvent event) {
        Agence selectedAgence = listViewAgences.getSelectionModel().getSelectedItem();
        if (selectedAgence != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/update_agence.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));

                update_agence controller = loader.getController();
                controller.setAgence(selectedAgence); // Passer l'agence sélectionnée au contrôleur de mise à jour

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une agence à modifier.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void openAddAgence(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/ajouter_agence.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
