package tn.esprit.easytripdesktopapp.controllers.Promotion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherPromotion implements Initializable {

    @FXML
    private ListView<Promotion> listViewPromotions;

    private final ServicePromotion servicePromotion = new ServicePromotion();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPromotions();
        setupListViewCellFactory(); // Personnaliser l'affichage
    }

    @FXML
    public void loadPromotions() {
        listViewPromotions.getItems().clear();
        List<Promotion> promotions = servicePromotion.getAll();
        listViewPromotions.getItems().addAll(promotions);
    }

    @FXML
    public void openAddPromotion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/ajouter_promotion.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    public void deletePromotion(ActionEvent event) {
        Promotion selectedPromotion = listViewPromotions.getSelectionModel().getSelectedItem();
        if (selectedPromotion == null) {
            showAlert("Erreur", "Veuillez sélectionner une promotion à supprimer !", Alert.AlertType.ERROR);
            return;
        }

        // Affichage d'une boîte de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer cette promotion ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            servicePromotion.delete(selectedPromotion); // 👈 Passer l'objet directement
            showAlert("Succès", "Promotion supprimée avec succès !", Alert.AlertType.INFORMATION);
            loadPromotions(); // Actualiser la liste
        }
    }


    @FXML
    public void refreshPromotions(ActionEvent event) {
        loadPromotions();
    }

    private void openScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupListViewCellFactory() {
        listViewPromotions.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Promotion promotion, boolean empty) {
                super.updateItem(promotion, empty);
                if (empty || promotion == null) {
                    setText(null);
                } else {
                    setText("🎁 Titre: " + promotion.getTitle() + "\n" +
                            "📖 Description: " + promotion.getDescription() + "\n" +
                            "💲 Réduction: " + promotion.getDiscount_percentage() + "%\n" +
                            "📅 Valable jusqu'à: " + promotion.getValid_until());
                }
            }
        });
    }

    @FXML
    public void openUpdatePromotion(ActionEvent actionEvent) {
        // Récupérer la promotion sélectionnée dans la liste
        Promotion selectedPromotion = listViewPromotions.getSelectionModel().getSelectedItem();

        if (selectedPromotion == null) {
            showAlert("Erreur", "Veuillez sélectionner une promotion à modifier.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Charger la fenêtre UpdatePromotion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/update_promotion.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et lui passer la promotion sélectionnée
            UpdatePromotion controller = loader.getController();
            controller.setPromotion(selectedPromotion);  // Passer la promotion au contrôleur

            // Ouvrir la fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
