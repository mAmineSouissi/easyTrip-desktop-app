package tn.esprit.easytripdesktopapp.controllers.Agent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.net.URL;
import java.util.*;

public class AfficherPromotion implements Initializable {

    @FXML
    private FlowPane cardContainer;

    @FXML
    private TextField searchField;

    private final CRUDService<Promotion> promotionService = new ServicePromotion();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        removeExpiredPromotions();
        loadPromotions();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
    }

    private void removeExpiredPromotions() {
        List<Promotion> promotions = promotionService.getAll();
        java.util.Date today = new java.util.Date();

        for (Promotion promotion : promotions) {
            java.util.Date expirationDate = new java.util.Date(promotion.getValid_until().getTime() + (24 * 60 * 60 * 1000));
            if (expirationDate.before(today)) {
                promotionService.delete(promotion);
                System.out.println("Promotion supprimée : " + promotion.getTitle());
            }
        }
    }

    private void loadPromotions() {
        cardContainer.getChildren().clear();

        List<Promotion> promotions = promotionService.getAll();
        promotions.sort(Comparator.comparingDouble(Promotion::getDiscount_percentage).reversed());

        for (Promotion promotion : promotions) {
            VBox card = createPromotionCard(promotion);
            cardContainer.getChildren().add(card);
        }
    }

    private VBox createPromotionCard(Promotion promotion) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 1);");
        card.setPrefWidth(200);
        card.setSpacing(10);

        Label titleLabel = new Label(promotion.getTitle());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label discountLabel = new Label("Réduction : " + promotion.getDiscount_percentage() + "%");
        Label dateLabel = new Label("Valide jusqu'au : " + promotion.getValid_until());

        card.setOnMouseClicked(event -> showPromotionDetail(promotion));

        card.getChildren().addAll(titleLabel, discountLabel, dateLabel);
        return card;
    }

    @FXML
    public void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        cardContainer.getChildren().clear();

        List<Promotion> promotions = promotionService.getAll();

        for (Promotion promotion : promotions) {
            if (promotion.getTitle().toLowerCase().contains(searchText)) {
                VBox card = createPromotionCard(promotion);
                cardContainer.getChildren().add(card);
            }
        }

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
