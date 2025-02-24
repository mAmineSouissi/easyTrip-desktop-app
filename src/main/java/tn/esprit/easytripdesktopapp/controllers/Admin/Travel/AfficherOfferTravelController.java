package tn.esprit.easytripdesktopapp.controllers.Admin.Travel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.controllers.Agent.Travel.UpdateOfferTravelController;
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.services.ServiceOfferTravel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherOfferTravelController implements Initializable {

    @FXML
    private FlowPane cardContainer; // ✅ Remplace ListView

    private final ServiceOfferTravel serviceOfferTravel = new ServiceOfferTravel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadOffers(); // ✅ Charge les offres au démarrage
    }

    // ✅ Charge les offres sous forme de cartes
    private void loadOffers() {
        cardContainer.getChildren().clear(); // Effacer les anciennes cartes

        List<OfferTravel> offers = serviceOfferTravel.getAll();
        for (OfferTravel offer : offers) {
            VBox card = createOfferCard(offer);
            cardContainer.getChildren().add(card);
        }
    }

    // ✅ Crée une carte pour une offre
    private VBox createOfferCard(OfferTravel offer) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-spacing: 10; "
                + "-fx-border-color: #ddd; -fx-border-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 1, 1);");
        card.setPrefWidth(250);

        // ✅ Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(250);
        imageView.setFitHeight(150);
        if (offer.getImage() != null && !offer.getImage().isEmpty()) {
            imageView.setImage(new Image(offer.getImage()));
        } else {
            imageView.setImage(new Image("file:src/default.jpg")); // Image par défaut
        }

        // ✅ Détails
        Text destinationText = new Text("Destination: " + offer.getDestination());
        Text priceText = new Text("Prix: " + offer.getPrice() + " DT");

        // ✅ Bouton Modifier
        Button btnModifier = new Button("Modifier");
        btnModifier.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        btnModifier.setOnAction(event -> openUpdateTravel(offer));

        // ✅ Bouton Supprimer
        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        btnSupprimer.setOnAction(event -> deleteTravel(offer));

        card.getChildren().addAll(imageView, destinationText, priceText, btnModifier, btnSupprimer);
        return card;
    }

    // ✅ Ouvre la fenêtre de modification
    private void openUpdateTravel(OfferTravel offer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/Travel/update_offer_travel.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            UpdateOfferTravelController controller = loader.getController();
            controller.setSelectedOffer(offer);

            stage.showAndWait(); // Attend la fermeture de la fenêtre
            loadOffers(); // ✅ Recharge après modification
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture de la fenêtre de mise à jour.", Alert.AlertType.ERROR);
        }
    }

    // ✅ Supprime une offre avec confirmation
    private void deleteTravel(OfferTravel offer) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de Suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette offre ?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                serviceOfferTravel.delete(offer);
                loadOffers(); // ✅ Recharge après suppression
                showAlert("Succès", "L'offre a été supprimée avec succès.", Alert.AlertType.INFORMATION);
            }
        });
    }

    // ✅ Affiche une alerte
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
