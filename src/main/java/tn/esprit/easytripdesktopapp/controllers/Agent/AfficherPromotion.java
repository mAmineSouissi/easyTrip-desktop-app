package tn.esprit.easytripdesktopapp.controllers.Agent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.net.URL;
import java.util.*;

public class AfficherPromotion implements Initializable {

    @FXML
    private FlowPane cardContainer;

    @FXML
    private TextField searchField; // Doit correspondre à fx:id="searchField" dans le FXML


    private final CRUDService<Promotion> promotionService = new ServicePromotion();

    UserSession session = UserSession.getInstance();

    ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
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
        card.getStyleClass().add("card"); // Applique la classe CSS "card"
        card.setPrefWidth(280); // Largeur préférée
        card.setSpacing(10); // Espacement entre les éléments

        Label titleLabel = new Label(promotion.getTitle());
        titleLabel.getStyleClass().add("card-title"); // Applique la classe CSS "card-title"

        Label discountLabel = new Label("Réduction : " + promotion.getDiscount_percentage() + "%");
        discountLabel.getStyleClass().add("card-discount"); // Applique la classe CSS "card-discount"

        Label dateLabel = new Label("Valide jusqu'au : " + promotion.getValid_until());
        dateLabel.getStyleClass().add("card-date"); // Applique la classe CSS "card-date"

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

    public void goBack(ActionEvent actionEvent) {
        Stage stage;
        Scene scene;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(getClass().getResource(
                    "/tn/esprit/easytripdesktopapp/FXML/Agent/Dashboard.fxml"),
                    bundle);
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(bundle.getString(session.getUser().getRole().toLowerCase() + "_dashboard"));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
