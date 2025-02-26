package tn.esprit.easytripdesktopapp.controllers.Client.Travel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServiceOfferTravel;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AfficherOfferTravelController implements Initializable {
    @FXML
    public ComboBox<String> agenceComboBox;
    @FXML
    private FlowPane cardContainer;

    @FXML
    private ComboBox<String> offerComboBox; // Liste déroulante pour filtrer par destination

    @FXML
    private ComboBox<String> promotionComboBox; // Liste déroulante pour filtrer par promotion

    @FXML
    private DatePicker startDatePicker; // Date de départ pour le filtre

    @FXML
    private DatePicker endDatePicker; // Date d'arrivée pour le filtre

    @FXML
    private TextField searchField;

    private final CRUDService<OfferTravel> offerService = new ServiceOfferTravel();
    private List<OfferTravel> allOffers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();

        // Écouteurs pour les filtres
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        offerComboBox.setOnAction(event -> applyFilters());
        promotionComboBox.setOnAction(event -> applyFilters());
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        agenceComboBox.setOnAction(event -> applyFilters()); // Ajouter un écouteur pour agenceComboBox
    }

    private void loadData() {
        allOffers = offerService.getAll();
        loadComboBoxes();
        loadAgencies(); // Charger les agences
        loadOffers(allOffers);
    }

    private void loadComboBoxes() {
        // Charger les destinations
        offerComboBox.getItems().clear();
        offerComboBox.getItems().add("Toutes les destinations"); // Option pour afficher tout
        allOffers.stream()
                .map(OfferTravel::getDestination)
                .distinct()
                .forEach(offerComboBox.getItems()::add);
        offerComboBox.getSelectionModel().selectFirst();

        // Charger les promotions
        promotionComboBox.getItems().clear();
        promotionComboBox.getItems().add("Toutes les promotions"); // Option pour afficher tout
        allOffers.stream()
                .filter(o -> o.getPromotion() != null)
                .map(o -> o.getPromotion().getTitle())
                .distinct()
                .forEach(promotionComboBox.getItems()::add);
        promotionComboBox.getSelectionModel().selectFirst();
    }

    private void loadAgencies() {
        agenceComboBox.getItems().clear();
        agenceComboBox.getItems().add("Toutes les agences"); // Option pour afficher tout
        allOffers.stream()
                .filter(o -> o.getAgence() != null) // Filtrer les offres avec une agence non nulle
                .map(o -> o.getAgence().getNom()) // Récupérer les noms des agences
                .distinct() // Éviter les doublons
                .forEach(agenceComboBox.getItems()::add); // Ajouter à la ComboBox
        agenceComboBox.getSelectionModel().selectFirst(); // Sélectionner "Toutes les agences" par défaut
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
    private void applyFilters() {
        String selectedDestination = offerComboBox.getValue();
        String selectedPromotion = promotionComboBox.getValue();
        String selectedAgency = agenceComboBox.getValue(); // Récupération de l'agence sélectionnée
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String keyword = searchField.getText().toLowerCase().trim();

        // Convertir LocalDate en java.sql.Date (si non null)
        java.sql.Date startSqlDate = (startDate != null) ? java.sql.Date.valueOf(startDate) : null;
        java.sql.Date endSqlDate = (endDate != null) ? java.sql.Date.valueOf(endDate) : null;

        List<OfferTravel> filteredOffers = allOffers.stream()
                .filter(o -> selectedDestination == null || selectedDestination.equals("Toutes les destinations") || o.getDestination().equals(selectedDestination))
                .filter(o -> selectedPromotion == null || selectedPromotion.equals("Toutes les promotions") ||
                        (o.getPromotion() != null && o.getPromotion().getTitle().equals(selectedPromotion)))
                .filter(o -> selectedAgency == null || selectedAgency.equals("Toutes les agences") ||
                        (o.getAgence() != null && o.getAgence().getNom().equals(selectedAgency))) // Filtre par agence
                .filter(o -> startSqlDate == null || (o.getDeparture_date() != null && (o.getDeparture_date().after(startSqlDate) || o.getDeparture_date().equals(startSqlDate))))
                .filter(o -> endSqlDate == null || (o.getArrival_date() != null && (o.getArrival_date().before(endSqlDate) || o.getArrival_date().equals(endSqlDate))))
                .filter(o -> o.getDestination().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        loadOffers(filteredOffers);
    }

    private void showOfferDetails(OfferTravel offer) {
        Alert detailAlert = new Alert(Alert.AlertType.INFORMATION);
        detailAlert.setTitle("Détails de l'Offre de Voyage");
        detailAlert.setHeaderText(null);

        // Créer un VBox pour organiser l'image, les détails et le bouton
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

        // Ajouter un bouton "Réserver"
        Button btnReserver = new Button("Réserver");
        btnReserver.getStyleClass().add("button-reserver"); // Appliquer une classe CSS
        btnReserver.setOnAction(event -> {
            // Logique pour réserver l'offre
            System.out.println("Offre réservée : " + offer.getDestination());
            detailAlert.close(); // Fermer la boîte de dialogue après la réservation
        });

        // Ajouter le bouton au VBox
        vbox.getChildren().add(btnReserver);

        // Définir le VBox comme contenu de l'Alert
        detailAlert.getDialogPane().setContent(vbox);

        // Afficher l'Alert
        detailAlert.showAndWait();
    }
    }


