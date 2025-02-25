package tn.esprit.easytripdesktopapp.controllers.Admin.Promotion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
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
        removeExpiredPromotions();
        loadPromotions();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
    }


    private void removeExpiredPromotions() {
        List<Promotion> promotions = promotionService.getAll();
        Date today = new Date();

        for (Promotion promotion : promotions) {

            Date expirationDate = new Date(promotion.getValid_until().getTime() + (24 * 60 * 60 * 1000));

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

        Button btnModifier = new Button("Modifier");
        btnModifier.getStyleClass().add("modify-button");
        btnModifier.setOnAction(event -> openUpdatePromotion(promotion));

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.getStyleClass().add("delete-button");
        btnSupprimer.setOnAction(event -> confirmDelete(promotion));

        card.getChildren().addAll(titleLabel, discountLabel, dateLabel, btnModifier, btnSupprimer);
        return card;
    }

    private void openUpdatePromotion(Promotion promotion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/Promotion/update_promotion.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modifier Promotion");
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(loader.load(), 400, 400);
            stage.setScene(scene);

            UpdatePromotion controller = loader.getController();
            controller.setPromotion(promotion);

            stage.showAndWait();
            loadPromotions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openAddPromotion(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/Promotion/ajouter_promotion.fxml"));
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
        loadPromotions();
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
