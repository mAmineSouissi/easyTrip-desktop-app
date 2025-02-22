package tn.esprit.easytripdesktopapp.controllers.Agence;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class afficher_agence implements Initializable {

    @FXML
    private FlowPane cardContainer;

    private final CRUDService<Agence> agenceService = new ServiceAgence();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAgences();  // Charge les agences au démarrage
    }

    private void loadAgences() {
        cardContainer.getChildren().clear(); // Nettoyer avant de recharger

        List<Agence> agences = agenceService.getAll(); // Récupérer les agences de la BD

        for (Agence agence : agences) {
            VBox card = createAgenceCard(agence);
            cardContainer.getChildren().add(card);
        }
    }

    private VBox createAgenceCard(Agence agence) {
        VBox card = new VBox();
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 1);");

        // Image de l'agence
        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        // Chargement de l'image
        if (agence.getImage() != null && !agence.getImage().isEmpty()) {
            try {
                Image img = new Image(agence.getImage());
                imageView.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur chargement image : " + e.getMessage());
                imageView.setImage(new Image("file:src/main/resources/images/default_agence.png"));
            }
        } else {
            imageView.setImage(new Image("file:src/main/resources/images/default_agence.png"));
        }

        // Nom de l'agence
        Text nomAgence = new Text(agence.getNom());
        nomAgence.getStyleClass().add("card-title");
        nomAgence.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Boutons avec icônes
        Button btnModifier = new Button("Modifier");
        btnModifier.getStyleClass().add("btn-modifier");
        btnModifier.setOnAction(event -> openUpdateAgence(agence));

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.getStyleClass().add("btn-supprimer");
        btnSupprimer.setOnAction(event -> confirmDelete(agence));

        HBox buttonBox = new HBox(10, btnModifier, btnSupprimer);
        buttonBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(imageView, nomAgence, buttonBox);

        // Ajouter un événement de clic sur la carte
        card.setOnMouseClicked(event -> showAgenceDetails(agence));

        return card;
    }

    private void showAgenceDetails(Agence agence) {
        Stage detailsStage = new Stage();
        VBox detailsBox = new VBox(10);
        detailsBox.setAlignment(Pos.CENTER);
        detailsBox.setPadding(new Insets(20));

        // Ajouter les détails de l'agence
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        // Chargement de l'image
        if (agence.getImage() != null && !agence.getImage().isEmpty()) {
            try {
                Image img = new Image(agence.getImage());
                imageView.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur chargement image : " + e.getMessage());
                imageView.setImage(new Image("file:src/main/resources/images/default_agence.png"));
            }
        } else {
            imageView.setImage(new Image("file:src/main/resources/images/default_agence.png"));
        }

        Text nomLabel = new Text("Nom : " + agence.getNom());
        Text adresseLabel = new Text("Adresse : " + agence.getAddress());
        Text telephoneLabel = new Text("Téléphone : " + agence.getPhone());
        Text emailLabel = new Text("Email : " + agence.getEmail());

        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(event -> detailsStage.close());

        detailsBox.getChildren().addAll(imageView, nomLabel, adresseLabel, telephoneLabel, emailLabel, closeButton);

        Scene detailsScene = new Scene(detailsBox, 300, 400);
        detailsStage.setScene(detailsScene);
        detailsStage.setTitle("Détails de l'agence");
        detailsStage.show();
    }



    private void openUpdateAgence(Agence agence) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/update_agence.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            update_agence controller = loader.getController();
            controller.setAgence(agence); // Passer l'agence sélectionnée au contrôleur de mise à jour

            // Ajouter un événement pour recharger les agences lorsque la fenêtre est fermée
            stage.setOnHiding(event -> loadAgences());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void confirmDelete(Agence agence) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer l'agence " + agence.getNom() + " ?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                deleteAgence(agence);
            }
        });
    }

    private void deleteAgence(Agence agence) {
        agenceService.delete(agence);
        loadAgences(); // Rafraîchir la liste après suppression
    }

    @FXML
    public void openAddAgence(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/ajouter_agence.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();
            stage.setOnHiding(event -> loadAgences());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
