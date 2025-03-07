package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import java.util.*;

public class AfficherPromotion implements Initializable {

    private final CRUDService<Promotion> promotionService = new ServicePromotion();
    @FXML
    private FlowPane cardContainer;
    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        removeExpiredPromotions();
        loadPromotions();
        cardContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Ajuster la mise en page en fonction de la nouvelle largeur
            adjustLayout(newVal.doubleValue());
        });
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
        card.getStyleClass().add("card"); // Utilisez une classe CSS pour la carte
        card.setPrefWidth(200);
        card.setSpacing(10);

        Label titleLabel = new Label(promotion.getTitle());
        titleLabel.getStyleClass().add("promo-name"); // Utilisez une classe CSS pour le titre

        Label discountLabel = new Label("Réduction : " + promotion.getDiscount_percentage() + "%");
        discountLabel.getStyleClass().add("promo-discount"); // Utilisez une classe CSS pour le pourcentage

        Label dateLabel = new Label("Valide jusqu'au : " + promotion.getValid_until());
        dateLabel.getStyleClass().add("promo-valid-until"); // Utilisez une classe CSS pour la date

        card.setOnMouseClicked(event -> showPromotionDetail(promotion));

        Button btnModifier = new Button("Modifier");
        btnModifier.getStyleClass().add("modify-button"); // Utilisez une classe CSS pour le bouton Modifier
        btnModifier.setOnAction(event -> openUpdatePromotion(promotion));

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.getStyleClass().add("delete-button"); // Utilisez une classe CSS pour le bouton Supprimer
        btnSupprimer.setOnAction(event -> confirmDelete(promotion));

        card.getChildren().addAll(titleLabel, discountLabel, dateLabel, btnModifier, btnSupprimer);
        return card;
    }

    private void openUpdatePromotion(Promotion promotion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/update_promotion.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/ajouter_promotion.fxml"));
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
        detailAlert.setContentText("Titre : " + promotion.getTitle() + "\nDescription : " + promotion.getDescription() + "\nRéduction : " + promotion.getDiscount_percentage() + "%" + "\nValide jusqu'au : " + promotion.getValid_until());
        detailAlert.showAndWait();
    }

    private void adjustLayout(double width) {
        // Ajuster la mise en page des cartes en fonction de la largeur
        if (width < 600) {
            cardContainer.setHgap(5);
            cardContainer.setVgap(5);
        } else {
            cardContainer.setHgap(10);
            cardContainer.setVgap(10);
        }
    }

    public void goBack(ActionEvent actionEvent) {
        Stage stage;
        try {
            ResourceBundle resourcesBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/TablesAdmin.fxml"), resourcesBundle);
            Parent root = loader.load();
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}