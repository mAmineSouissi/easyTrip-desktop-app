package tn.esprit.easytripdesktopapp.controllers.Agence;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class afficher_agence implements Initializable {

    @FXML
    private ListView<Agence> listViewAgences;

    private final ServiceAgence serviceAgence = new ServiceAgence();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAgences();  // Charge les agences au démarrage
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
                    setGraphic(null);
                } else {
                    Button updateButton = new Button("Modifier");
                    updateButton.setOnAction(event -> openUpdateAgence(agence));

                    Button deleteButton = new Button("Supprimer");
                    deleteButton.setOnAction(event -> confirmDeleteAgence(agence));

                    VBox buttonsBox = new VBox(5, updateButton, deleteButton);
                    buttonsBox.setAlignment(Pos.CENTER);

                    VBox detailsBox = new VBox(3);
                    detailsBox.getChildren().addAll(
                            new Label("📌 Nom: " + agence.getNom()),
                            new Label("📍 Adresse: " + agence.getAddress()),
                            new Label("📞 Téléphone: " + agence.getPhone()),
                            new Label("📧 Email: " + agence.getEmail()),
                            new Label("🖼️ Image: " + agence.getImage())
                    );

                    HBox hbox = new HBox(10, buttonsBox, detailsBox);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void confirmDeleteAgence(Agence agence) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'agence : " + agence.getNom());
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette agence ? Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteAgence(agence);
        }
    }

    private void deleteAgence(Agence agence) {
        serviceAgence.delete(agence);
        loadAgences();  // Rafraîchir après suppression
    }

    private void openUpdateAgence(Agence agence) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/update_agence.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            update_agence controller = loader.getController();
            controller.setAgence(agence);
            stage.show();

            stage.setOnHiding(event -> loadAgences()); // Rafraîchir après modification

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openAddAgence() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/ajouter_agence.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();

            stage.setOnHiding(event -> loadAgences()); // Rafraîchir après ajout

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
