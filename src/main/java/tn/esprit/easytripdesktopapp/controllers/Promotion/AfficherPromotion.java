package tn.esprit.easytripdesktopapp.controllers.Promotion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherPromotion implements Initializable {

    @FXML
    private FlowPane cardContainer;

    @FXML
    private TextField searchField;

    private final CRUDService<Promotion> promotionService = new ServicePromotion();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPromotions();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
// Charge les promotions au démarrage
    }



    private void loadPromotions() {
        cardContainer.getChildren().clear(); // Nettoyer avant de recharger

        List<Promotion> promotions = promotionService.getAll(); // Récupérer les promotions de la BD

        // Trier les promotions par pourcentage de réduction, de plus grand au plus petit
        promotions.sort(Comparator.comparingDouble(Promotion::getDiscount_percentage).reversed());

        for (Promotion promotion : promotions) {
            VBox card = createPromotionCard(promotion);
            cardContainer.getChildren().add(card); // Ajouter chaque carte au FlowPane
        }
    }

    private VBox createPromotionCard(Promotion promotion) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 1);");
        card.setPrefWidth(200); // Définir une largeur fixe pour les cartes
        card.setSpacing(10);

        // Titre de la promotion
        Label titleLabel = new Label(promotion.getTitle());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Pourcentage de réduction
        Label discountLabel = new Label("Réduction : " + promotion.getDiscount_percentage() + "%");

        // Date de validité
        Label dateLabel = new Label("Valide jusqu'au : " + promotion.getValid_until());

        // Événement de clic sur la carte pour afficher les détails
        card.setOnMouseClicked(event -> showPromotionDetail(promotion));

        // Bouton pour modifier
        Button btnModifier = new Button("Modifier");
        btnModifier.setOnAction(event -> openUpdatePromotion(promotion));

        // Bouton pour supprimer
        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setOnAction(event -> confirmDelete(promotion));

        // Ajouter les éléments à la carte
        card.getChildren().addAll(titleLabel, discountLabel, dateLabel, btnModifier, btnSupprimer);
        return card;
    }


    private void openUpdatePromotion(Promotion promotion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/update_promotion.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modifier Promotion");
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(loader.load(), 400, 400);
            stage.setScene(scene);

            UpdatePromotion controller = loader.getController();
            controller.setPromotion(promotion); // Passer les données de la promotion à mettre à jour

            stage.showAndWait();
            loadPromotions(); // Recharger les promotions après mise à jour
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openAddPromotion(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/ajouter_promotion.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();
            stage.setOnHiding(event -> loadPromotions());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmDelete(Promotion promotion) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer la promotion " + promotion.getTitle() + " ?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deletePromotion(promotion);
            }
        });
    }

    private void deletePromotion(Promotion promotion) {
        promotionService.delete(promotion);
        loadPromotions(); // Rafraîchir la liste après suppression
    }

    @FXML
    public void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        cardContainer.getChildren().clear(); // Nettoyer avant de recharger

        List<Promotion> promotions = promotionService.getAll(); // Récupérer les promotions de la BD

        for (Promotion promotion : promotions) {
            if (promotion.getTitle().toLowerCase().contains(searchText)) {
                VBox card = createPromotionCard(promotion);
                cardContainer.getChildren().add(card);
            }
        }

        // Si la barre de recherche est vide, rechargez toutes les promotions
        if (searchText.isEmpty()) {
            loadPromotions();
        }
    }

    private void showPromotionDetail(Promotion promotion) {
        Alert detailAlert = new Alert(Alert.AlertType.INFORMATION);
        detailAlert.setTitle("Détails de la Promotion");
        detailAlert.setHeaderText(null);
        detailAlert.setContentText("Titre : " + promotion.getTitle() +
                "\nDescription : " + promotion.getDescription() +
                "\nRéduction : " + promotion.getDiscount_percentage() + "%" +
                "\nValide jusqu'au : " + promotion.getValid_until());
        detailAlert.showAndWait();
    }
}
