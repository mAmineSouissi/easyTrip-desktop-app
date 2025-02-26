package tn.esprit.easytripdesktopapp.controllers.Admin.Travel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
import java.util.stream.Collectors;

public class AfficherOfferTravelController implements Initializable {

    @FXML
    private FlowPane cardContainer;

    @FXML
    private ComboBox<String> offerComboBox; // Liste déroulante pour filtrer par destination

    @FXML
    private ComboBox<String> agencyComboBox; // Nouvelle liste déroulante pour filtrer par agence

    @FXML
    private TextField searchField;

    private final CRUDService<OfferTravel> offerService = new ServiceOfferTravel();
    private List<OfferTravel> allOffers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        offerComboBox.setOnAction(event -> filterOffers());
        agencyComboBox.setOnAction(event -> filterByAgency()); // Ajout de l'événement pour filtrer par agence
    }

    private void loadData() {
        allOffers = offerService.getAll();
        loadComboBoxes();
        loadOffers(allOffers);
    }

    private void loadComboBoxes() {
        // Charger les destinations
        offerComboBox.getItems().clear();
        offerComboBox.getItems().add("Toutes les destinations"); // Option pour afficher tout
        for (OfferTravel offer : allOffers) {
            offerComboBox.getItems().add(offer.getDestination());
        }
        offerComboBox.getSelectionModel().selectFirst();

        // Charger les agences
        agencyComboBox.getItems().clear();
        agencyComboBox.getItems().add("Toutes les agences"); // Option pour afficher tout
        List<String> agencies = allOffers.stream()
                .filter(o -> o.getAgence() != null)
                .map(o -> o.getAgence().getNom())
                .distinct()
                .collect(Collectors.toList());
        agencyComboBox.getItems().addAll(agencies);
        agencyComboBox.getSelectionModel().selectFirst();
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

        Text priceText = new Text("Prix : " + offer.getPrice() + " DT");
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
                .collect(Collectors.toList());

        loadOffers(filteredOffers);
    }

    @FXML
    private void filterByAgency() {
        String selectedAgency = agencyComboBox.getValue();

        if (selectedAgency == null || selectedAgency.equals("Toutes les agences")) {
            loadOffers(allOffers);
            return;
        }

        List<OfferTravel> filteredOffers = allOffers.stream()
                .filter(o -> o.getAgence() != null && o.getAgence().getNom().equals(selectedAgency))
                .collect(Collectors.toList());

        loadOffers(filteredOffers);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        List<OfferTravel> filteredOffers = allOffers.stream()
                .filter(o -> o.getDestination().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        loadOffers(filteredOffers);
    }

    private void showOfferDetails(OfferTravel offer) {
        Alert detailAlert = new Alert(Alert.AlertType.INFORMATION);
        detailAlert.setTitle("Détails de l'Offre de Voyage");
        detailAlert.setHeaderText(null);

        // Créer un VBox pour organiser l'image et les détails
        VBox vbox = new VBox(10); // Espacement de 10 entre les éléments
        vbox.setAlignment(Pos.CENTER); // Centrer le contenu

        // Ajouter l'image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200); // Largeur de l'image
        imageView.setFitHeight(200); // Hauteur de l'image
        imageView.setPreserveRatio(true); // Conserver le ratio de l'image

        String imagePath = offer.getImage();
        File file = (imagePath != null) ? new File(imagePath) : null;

        if (file != null && file.exists()) {
            imageView.setImage(new Image(file.toURI().toString()));
        } else {
            // Image par défaut si l'image de l'offre n'est pas disponible
            imageView.setImage(new Image("file:src/main/resources/images/default_offer.png"));
        }

        // Ajouter l'image au VBox
        vbox.getChildren().add(imageView);

        // Ajouter les autres détails sous forme de Labels
        vbox.getChildren().add(new Label("🏝 Destination : " + offer.getDestination()));
        vbox.getChildren().add(new Label("🚉 Départ : " + offer.getDeparture()));
        vbox.getChildren().add(new Label("💰 Prix : " + offer.getPrice() + " DT"));
        vbox.getChildren().add(new Label("🏨 Hôtel : " + offer.getHotelName()));
        vbox.getChildren().add(new Label("✈️ Vol : " + offer.getFlightName()));
        vbox.getChildren().add(new Label("📅 Départ : " + offer.getDeparture_date() + " - Arrivée : " + offer.getArrival_date()));
        vbox.getChildren().add(new Label("📖 Description : " + offer.getDiscription()));
        vbox.getChildren().add(new Label("🏢 Agence : " + (offer.getAgence() != null ? offer.getAgence().getNom() : "Non spécifiée")));
        vbox.getChildren().add(new Label("🎁 Promotion : " + (offer.getPromotion() != null ? offer.getPromotion().getTitle() : "Aucune")));
        vbox.getChildren().add(new Label("📂 Catégorie : " + offer.getCategory()));

        // Définir le VBox comme contenu de l'Alert
        detailAlert.getDialogPane().setContent(vbox);

        // Afficher l'Alert
        detailAlert.showAndWait();
    }
}