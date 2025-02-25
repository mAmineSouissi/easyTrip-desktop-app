package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.services.ServiceOfferTravel;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherOfferTravelController implements Initializable {

    @FXML
    private FlowPane cardContainer;

    @FXML
    private ComboBox<String> offerComboBox; // Liste déroulante pour filtrer par destination

    @FXML
    private TextField searchField;

    private final CRUDService<OfferTravel> offerService = new ServiceOfferTravel();
    private List<OfferTravel> allOffers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        offerComboBox.setOnAction(event -> filterOffers());
    }

    private void loadData() {
        allOffers = offerService.getAll();
        loadComboBox();
        loadOffers(allOffers);
    }

    private void loadComboBox() {
        offerComboBox.getItems().clear();
        offerComboBox.getItems().add("Toutes les destinations"); // Option pour afficher tout
        for (OfferTravel offer : allOffers) {
            offerComboBox.getItems().add(offer.getDestination());
        }
        offerComboBox.getSelectionModel().selectFirst();
    }

    private void loadOffers(List<OfferTravel> offers) {
        cardContainer.getChildren().clear();
        for (OfferTravel offer : offers) {
            VBox card = createOfferCard(offer);
            cardContainer.getChildren().add(card);
        }
    }

    private VBox createOfferCard(OfferTravel offer) {
        VBox card = new VBox();
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 1);");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        String imagePath = offer.getImage();
        File file = (imagePath != null) ? new File(imagePath) : null;

        if (file != null && file.exists()) {
            imageView.setImage(new Image(file.toURI().toString()));
        } else {
            imageView.setImage(new Image("file:src/main/resources/images/default_offer.png"));
        }

        Text destinationText = new Text("Destination : " + offer.getDestination());
        destinationText.getStyleClass().add("card-title");
        destinationText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Text priceText = new Text("Prix : " + offer.getPrice() + " €");
        priceText.setStyle("-fx-font-size: 14px;");

        card.getChildren().addAll(imageView, destinationText, priceText);
        card.setAlignment(Pos.CENTER);

        // Ajout de l'événement pour afficher les détails lors du clic sur la carte
        card.setOnMouseClicked(event -> showOfferDetails(offer));

        return card;
    }


    @FXML
    private void filterOffers() {
        String selectedDestination = offerComboBox.getValue();

        if (selectedDestination == null || selectedDestination.equals("Toutes les destinations")) {
            loadOffers(allOffers);
            return;
        }

        List<OfferTravel> filteredOffers = allOffers.stream()
                .filter(o -> o.getDestination().equals(selectedDestination))
                .toList();

        loadOffers(filteredOffers);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        List<OfferTravel> filteredOffers = allOffers.stream()
                .filter(o -> o.getDestination().toLowerCase().contains(keyword))
                .toList();
        loadOffers(filteredOffers);
    }

    private void showOfferDetails(OfferTravel offer) {
        Alert detailAlert = new Alert(Alert.AlertType.INFORMATION);
        detailAlert.setTitle("Détails de l'Offre de Voyage");
        detailAlert.setHeaderText(null);
        detailAlert.setContentText(
                "🏝 Destination : " + offer.getDestination() +
                        "\n🚉 Départ : " + offer.getDeparture() +
                        "\n💰 Prix : " + offer.getPrice() + " €" +
                        "\n🏨 Hôtel : " + offer.getHotelName() +
                        "\n✈️ Vol : " + offer.getFlightName() +
                        "\n📅 Départ : " + offer.getDeparture() + offer.getDeparture_date() + " - Arrivée : " + offer.getArrival_date() +
                        "\n📖 Description : " + offer.getDiscription() +
                        "\n🏢 Agence : " + (offer.getAgence() != null ? offer.getAgence().getNom() : "Non spécifiée") +
                        "\n🎁 Promotion : " + (offer.getPromotion() != null ? offer.getPromotion().getTitle() : "Aucune") +
                        "\n📂 Catégorie : " + offer.getCategory()
        );

        detailAlert.showAndWait();
    }





}
