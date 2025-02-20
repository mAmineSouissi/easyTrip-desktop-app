package tn.esprit.easytripdesktopapp.controllers.Travel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.controllers.Promotion.UpdatePromotion;
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.services.ServiceOfferTravel;

import java.io.IOException;
import java.util.List;

public class AfficherOfferTravelController {

    @FXML
    private ListView<OfferTravel> listViewPromotions;

    @FXML
    private Button btnAjouter, btnModifier, btnSupprimer, btnRafraichir;

    private final ServiceOfferTravel serviceOfferTravel = new ServiceOfferTravel();

    @FXML
    public void initialize() {
        rafraichirListe(); // Charger la liste d'offres lors de l'initialisation
    }

    @FXML
    private void openAddtravel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/ajouter_offer_travel.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openUpdateTravel(ActionEvent event) {
        OfferTravel selectedOffer = listViewPromotions.getSelectionModel().getSelectedItem();
        if (selectedOffer == null) {
            showAlert("Erreur", "Veuillez sélectionner une offre à modifier.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Charger la fenêtre UpdateOfferTravel
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/update_offer_travel.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et lui passer l'offre sélectionnée
            UpdateOfferTravelController controller = loader.getController();
            controller.setSelectedOffer(selectedOffer);  // Passer l'offre au contrôleur

            // Ouvrir la fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture de la fenêtre de mise à jour : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void deleteTravel(ActionEvent event) {
        OfferTravel selectedOffer = listViewPromotions.getSelectionModel().getSelectedItem();
        if (selectedOffer != null) {
            // Afficher une alerte de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de Suppression");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette offre ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Appeler la méthode delete avec l'ID de l'offre sélectionnée
                    serviceOfferTravel.delete(selectedOffer);
                    rafraichirListe();
                    showAlert("Succès", "L'offre a été supprimée avec succès.", Alert.AlertType.INFORMATION);
                }
            });
        } else {
            showAlert("Erreur", "Veuillez sélectionner une offre à supprimer.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void refreshTravels(ActionEvent event) {
        rafraichirListe();
    }

    @FXML
    private void rafraichirListe() {
        List<OfferTravel> offers = serviceOfferTravel.getAll();
        ObservableList<OfferTravel> observableOffers = FXCollections.observableArrayList(offers);
        listViewPromotions.setItems(observableOffers);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
