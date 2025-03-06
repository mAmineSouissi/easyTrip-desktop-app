package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.models.Category; // Import de l'énumération Category
import tn.esprit.easytripdesktopapp.services.ServiceOfferTravel;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AfficherOfferTravelController implements Initializable {

    @FXML
    private FlowPane cardContainer;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> cbCategoryFilter; // Filtre par catégorie
    @FXML
    private DatePicker dpDepartureFilter; // Filtre par date de départ

    private final CRUDService<OfferTravel> offerService = new ServiceOfferTravel();
    private List<OfferTravel> allOffers;
    private UserSession session = UserSession.getInstance();
    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
        loadData();

        // Écouteur pour la recherche dynamique
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        // Charger les catégories dans le ComboBox
        cbCategoryFilter.getItems().add("Toutes les catégories"); // Option par défaut
        for (Category category : Category.values()) {
            cbCategoryFilter.getItems().add(category.toString()); // Ajouter chaque catégorie de l'énumération
        }
        cbCategoryFilter.getSelectionModel().selectFirst();

        // Écouteur pour le filtre de date de départ
        dpDepartureFilter.valueProperty().addListener((observable, oldValue, newValue) -> filterOffers());
    }

    private void loadData() {
        allOffers = offerService.getAll();
        loadOffers(allOffers);
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

        // Image de l'offre
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

        // Informations de l'offre
        Text destinationText = new Text("Destination : " + offer.getDestination());
        destinationText.getStyleClass().add("card-title");
        destinationText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Text priceText = new Text("Prix : " + offer.getPrice() + " DT");
        priceText.setStyle("-fx-font-size: 14px;");

        // Boutons
        Button btnModifier = new Button("Modifier");
        btnModifier.getStyleClass().add("button-modifier");
        btnModifier.setOnAction(event -> openUpdateOffer(offer));

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.getStyleClass().add("button-supprimer");
        btnSupprimer.setOnAction(event -> confirmDelete(offer));

        HBox buttonBox = new HBox(10, btnModifier, btnSupprimer);
        buttonBox.setAlignment(Pos.CENTER);

        // Ajouter les éléments à la carte
        card.getChildren().addAll(imageView, destinationText, priceText, buttonBox);

        // Ajouter un événement pour afficher les détails lors du clic sur la carte
        card.setOnMouseClicked(event -> showOfferDetails(offer));

        return card;
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        List<OfferTravel> filteredOffers = allOffers.stream()
                .filter(offer -> offer.getDestination().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        loadOffers(filteredOffers);
    }

    @FXML
    private void filterOffers() {
        // Récupérer les valeurs des filtres
        String keyword = searchField.getText().toLowerCase().trim();
        String selectedCategory = cbCategoryFilter.getValue();
        LocalDate selectedDate = dpDepartureFilter.getValue();

        // Convertir la date sélectionnée (LocalDate) en java.sql.Date
        java.sql.Date selectedSqlDate = (selectedDate != null) ? java.sql.Date.valueOf(selectedDate) : null;

        // Filtrer les offres
        List<OfferTravel> filteredOffers = allOffers.stream()
                .filter(offer -> offer.getDestination().toLowerCase().contains(keyword)) // Filtre par destination
                .filter(offer -> selectedCategory == null || selectedCategory.equals("Toutes les catégories") || offer.getCategory().toString().equals(selectedCategory)) // Filtre par catégorie
                .filter(offer -> selectedSqlDate == null || offer.getDeparture_date().after(selectedSqlDate) || offer.getDeparture_date().equals(selectedSqlDate)) // Filtre par date de départ (>=)
                .collect(Collectors.toList());

        // Charger les offres filtrées
        loadOffers(filteredOffers);
    }

    private void openUpdateOffer(OfferTravel offer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/update_offer_travel.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            UpdateOfferTravelController controller = loader.getController();
            controller.setSelectedOffer(offer);

            stage.setOnHiding(event -> loadData()); // Rafraîchir après modification

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmDelete(OfferTravel offer) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setContentText("Voulez-vous vraiment supprimer l'offre pour " + offer.getDestination() + " ?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteOffer(offer);
            }
        });
    }

    private void deleteOffer(OfferTravel offer) {
        offerService.delete(offer);
        loadData(); // Rafraîchir la liste après suppression
    }

    @FXML
    public void openAddOffer(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/ajouter_offer_travel.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            stage.setOnHiding(event -> loadData()); // Rafraîchir après ajout

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Dashboard.fxml"), bundle);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(bundle.getString(session.getUser().getRole().toLowerCase() + "_dashboard"));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showOfferDetails(OfferTravel offer) {
        // Créer une boîte de dialogue personnalisée
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Détails de l'offre");
        dialog.setHeaderText("Détails de l'offre pour " + offer.getDestination());

        // Style de la boîte de dialogue
        dialog.getDialogPane().setStyle(
                "-fx-background-color: #ffffff; " + // Fond blanc
                        "-fx-border-color: #3498db; " + // Bordure bleue
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 3);" // Ombre légère
        );

        // Créer le contenu de la boîte de dialogue
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20;");

        // Image de l'offre
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        String imagePath = offer.getImage();
        File file = (imagePath != null) ? new File(imagePath) : null;

        if (file != null && file.exists()) {
            imageView.setImage(new Image(file.toURI().toString()));
        } else {
            imageView.setImage(new Image("file:src/main/resources/images/default_offer.png"));
        }

        // Informations de l'offre
        Label departureLabel = new Label("Départ : " + offer.getDeparture());
        departureLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        Label destinationLabel = new Label("Destination : " + offer.getDestination());
        destinationLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        Label hotelLabel = new Label("Hôtel : " + offer.getHotelName());
        hotelLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        Label flightLabel = new Label("Vol : " + offer.getFlightName());
        flightLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        Label priceLabel = new Label("Prix : " + offer.getPrice() + " DT");
        priceLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #27ae60;");

        Label departureDateLabel = new Label("Date de départ : " + offer.getDeparture_date());
        departureDateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        Label arrivalDateLabel = new Label("Date d'arrivée : " + offer.getArrival_date());
        arrivalDateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        Label descriptionLabel = new Label("Description : " + offer.getDiscription());
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        Label agenceLabel = new Label("Agence : " + offer.getAgence().getNom());
        agenceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        Label promotionLabel = new Label("Promotion : " + (offer.getPromotion() != null ? offer.getPromotion().getTitle() : "Aucune"));
        promotionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        Label categoryLabel = new Label("Catégorie : " + offer.getCategory());
        categoryLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        // Ajouter les éléments au contenu
        content.getChildren().addAll(
                imageView,
                departureLabel,
                destinationLabel,
                hotelLabel,
                flightLabel,
                priceLabel,
                departureDateLabel,
                arrivalDateLabel,
                descriptionLabel,
                agenceLabel,
                promotionLabel,
                categoryLabel
        );

        // Ajouter le contenu à la boîte de dialogue
        dialog.getDialogPane().setContent(content);

        // Ajouter un bouton "Fermer"
        ButtonType closeButton = new ButtonType("Fermer", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Styler le bouton "Fermer"
        Button closeBtn = (Button) dialog.getDialogPane().lookupButton(closeButton);
        closeBtn.setStyle(
                "-fx-background-color: #3498db; " + // Fond bleu
                        "-fx-text-fill: white; " + // Texte blanc
                        "-fx-background-radius: 20; " + // Coins arrondis
                        "-fx-border-radius: 20; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 3);" // Ombre légère
        );

        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle("-fx-background-color: #2980b9;")); // Bleu plus foncé au survol
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle("-fx-background-color: #3498db;")); // Revenir au bleu initial

        // Afficher la boîte de dialogue
        dialog.showAndWait();
    }
}