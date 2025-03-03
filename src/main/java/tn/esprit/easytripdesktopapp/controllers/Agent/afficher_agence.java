package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.controllers.Admin.update_agence;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class afficher_agence implements Initializable {

    @FXML
    public FlowPane cardContainer; // Conteneur pour les cartes d'agences

    private ServiceAgence agenceService = new ServiceAgence(); // Service pour gérer les agences
    private List<Agence> allAgences; // Liste de toutes les agences
    private UserSession session = UserSession.getInstance(); // Session utilisateur
    private ResourceBundle bundle; // Pour l'internationalisation

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
        loadData(); // Charger les données des agences
    }

    private void loadData() {
        allAgences = agenceService.getByAgentId(session.getUser().getId()); // Récupérer les agences de l'agent
        loadAgences(allAgences); // Charger les cartes d'agences
    }

    private void loadAgences(List<Agence> agences) {
        cardContainer.getChildren().clear(); // Vider le conteneur
        for (Agence agence : agences) {
            VBox card = createAgenceCard(agence); // Créer une carte pour chaque agence
            cardContainer.getChildren().add(card); // Ajouter la carte au conteneur
        }
    }

    private VBox createAgenceCard(Agence agence) {
        VBox card = new VBox();
        card.getStyleClass().add("card");

        // Image de l'agence
        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);
        imageView.getStyleClass().add("card-image");

        if (agence.getImage() != null && !agence.getImage().isEmpty()) {
            try {
                Image img = new Image(agence.getImage());
                imageView.setImage(img);
            } catch (Exception e) {
                imageView.setImage(new Image("file:src/main/resources/images/default_agence.png"));
            }
        } else {
            imageView.setImage(new Image("file:src/main/resources/images/default_agence.png"));
        }

        // Nom de l'agence
        Text nomAgence = new Text(agence.getNom());
        nomAgence.getStyleClass().add("card-title");

        // Boutons Modifier et Supprimer
        Button btnModifier = new Button("Modifier");
        btnModifier.getStyleClass().add("button-modifier");
        btnModifier.setOnAction(event -> openUpdateAgence(agence));

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.getStyleClass().add("button-supprimer");
        btnSupprimer.setOnAction(event -> confirmDelete(agence));

        HBox buttonBox = new HBox(10, btnModifier, btnSupprimer);
        buttonBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(imageView, nomAgence, buttonBox);
        return card;
    }

    private void openUpdateAgence(Agence agence) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/update_agence.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            update_agence controller = loader.getController();
            controller.setAgence(agence);

            // Recharger les données après la modification
            stage.setOnHiding(event -> loadData());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmDelete(Agence agence) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setContentText("Voulez-vous vraiment supprimer l'agence " + agence.getNom() + " ?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteAgence(agence);
            }
        });
    }

    private void deleteAgence(Agence agence) {
        agenceService.delete(agence);
        loadData(); // Recharger les données après suppression
    }

    @FXML
    public void openAddAgence(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/ajouter_agence.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Recharger les données après ajout
            stage.setOnHiding(event -> loadData());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/tn/esprit/easytripdesktopapp/FXML/Agent/Dashboard.fxml"),
                    bundle);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(bundle.getString(session.getUser().getRole().toLowerCase() + "_dashboard"));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}