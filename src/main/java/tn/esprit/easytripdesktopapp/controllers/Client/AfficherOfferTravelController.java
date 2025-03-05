package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.services.ServiceOfferTravel;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AfficherOfferTravelController implements Initializable {

    private final CRUDService<OfferTravel> offerService = new ServiceOfferTravel();
    UserSession session = UserSession.getInstance();
    @FXML
    private FlowPane cardContainer;
    @FXML
    private ComboBox<String> offerComboBox; // Liste déroulante pour filtrer par destination
    @FXML
    private TextField searchField;
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

        List<OfferTravel> filteredOffers = allOffers.stream().filter(o -> o.getDestination().equals(selectedDestination)).toList();

        loadOffers(filteredOffers);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        List<OfferTravel> filteredOffers = allOffers.stream().filter(o -> o.getDestination().toLowerCase().contains(keyword)).toList();
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
        btnReserver.getStyleClass().add("button-reserver");
        btnReserver.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Reservation/addreservation.fxml"));
                Parent root = loader.load();
                Addreservation detailController = loader.getController();
                detailController.setOffer(offer);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Reservation Screen");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button btnFeedback = new Button("Soumettre un Feedback");
        btnFeedback.getStyleClass().add("button-reserver");
        btnFeedback.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/FeedbackUser.fxml"));
                Parent root = loader.load();

                FeedbackUser detailController = loader.getController();
                detailController.setOfferTravel(offer);

                Stage stage = new Stage();
                stage.setTitle("Détails de l'hôtel");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors du chargement des détails de l'hôtel.");
            }

        });

        // Ajouter le bouton au VBox
        vbox.getChildren().add(btnReserver);
        vbox.getChildren().add(btnFeedback);

        // Définir le VBox comme contenu de l'Alert
        detailAlert.getDialogPane().setContent(vbox);

        // Afficher l'Alert
        detailAlert.showAndWait();
    }

    private void makeReservation(OfferTravel offer) {
        // Logique pour la réservation de l'offre
        System.out.println("Réservation effectuée pour l'offre : " + offer.getId());
        // Ajoutez votre logique de réservation ici (par exemple, ouvrir une nouvelle fenêtre ou appeler un service)
    }


    public void goBack(ActionEvent actionEvent) {
        Stage stage;
        try {
            ResourceBundle resourcesBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/Dashboard.fxml"), resourcesBundle);
            Parent root = loader.load();
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface d'accueil.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}