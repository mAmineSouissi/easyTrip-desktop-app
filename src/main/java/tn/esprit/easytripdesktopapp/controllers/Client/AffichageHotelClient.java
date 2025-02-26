package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AffichageHotelClient {

    @FXML
    private FlowPane cardContainer;  // FlowPane pour contenir les cartes des hôtels horizontalement

    @FXML
    private TextField searchField;  // Champ de recherche par nom ou ville

    @FXML
    private ComboBox<Integer> ratingFilter;  // Filtre par note minimale

    @FXML
    private ComboBox<Float> priceFilter;  // Filtre par prix maximum

    @FXML
    private Button backButton;  // Bouton pour retourner à l'accueil

    private final CRUDService<Hotel> hotelService = new ServiceHotel();  // Service pour gérer les hôtels
    private List<Hotel> hotels;  // Liste des hôtels pour garder une référence

    UserSession session=UserSession.getInstance();

    @FXML
    public void initialize() {
        // Initialiser les filtres
        ratingFilter.getItems().addAll(1, 2, 3, 4, 5);
        priceFilter.getItems().addAll(50f, 100f, 150f, 200f, 250f, 300f);

        loadHotels();  // Charger les hôtels à l'initialisation
    }

    // Méthode pour charger les hôtels dans la CardView
    @FXML
    private void loadHotels() {
        cardContainer.getChildren().clear();
        hotels = hotelService.getAll();

        for (Hotel hotel : hotels) {
            // Créer une carte pour chaque hôtel
            VBox card = new VBox(10);
            card.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 10, 0, 0);");

            // Ajouter l'image de l'hôtel
            ImageView imageView = new ImageView(new Image(hotel.getImage()));
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);

            // Ajouter les informations de l'hôtel à la carte
            Label nameLabel = new Label("Nom: " + hotel.getName());
            nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label addressLabel = new Label("Adresse: " + hotel.getAdresse());
            Label cityLabel = new Label("Ville: " + hotel.getCity());

            // Afficher la note sous forme d'étoiles
            HBox ratingBox = new HBox(5); // Conteneur pour les étoiles
            int rating = hotel.getRating();
            for (int i = 1; i <= 5; i++) {
                Label starLabel = new Label(i <= rating ? "★" : "☆"); // Étoile pleine ou vide
                starLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: " + (i <= rating ? "#FFD700" : "#C0C0C0") + ";");
                ratingBox.getChildren().add(starLabel);
            }

            Label priceLabel = new Label("Prix: " + hotel.getPrice() + " €");
            priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");

            // Afficher la promotion si elle existe
            Label promotionLabel = new Label("Promotion: " + (hotel.getPromotion() != null ? hotel.getPromotion().getDiscount_percentage() + "% de réduction" : "Aucune promotion"));
            promotionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #FF5722;");

            // Bouton pour voir les détails
            Button detailsButton = new Button("Voir les détails");
            detailsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
            detailsButton.setOnAction(e -> showHotelDetails(hotel));

            // Ajouter les éléments à la carte
            card.getChildren().addAll(imageView, nameLabel, addressLabel, cityLabel, ratingBox, priceLabel, promotionLabel, detailsButton);

            // Ajouter la carte au conteneur
            cardContainer.getChildren().add(card);
        }
    }

    // Appliquer les filtres de recherche
    @FXML
    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        Integer minRating = ratingFilter.getValue();
        Float maxPrice = priceFilter.getValue();

        List<Hotel> filteredHotels = hotels.stream()
                .filter(hotel -> hotel.getName().toLowerCase().contains(searchText) || hotel.getCity().toLowerCase().contains(searchText))
                .filter(hotel -> minRating == null || hotel.getRating() >= minRating)
                .filter(hotel -> maxPrice == null || hotel.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        cardContainer.getChildren().clear();
        for (Hotel hotel : filteredHotels) {
            // Créer une carte pour chaque hôtel filtré
            VBox card = new VBox(10);
            card.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 10, 0, 0);");

            // Ajouter l'image de l'hôtel
            ImageView imageView = new ImageView(new Image(hotel.getImage()));
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);

            // Ajouter les informations de l'hôtel à la carte
            Label nameLabel = new Label("Nom: " + hotel.getName());
            nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label addressLabel = new Label("Adresse: " + hotel.getAdresse());
            Label cityLabel = new Label("Ville: " + hotel.getCity());

            // Afficher la note sous forme d'étoiles
            HBox ratingBox = new HBox(5); // Conteneur pour les étoiles
            int rating = hotel.getRating();
            for (int i = 1; i <= 5; i++) {
                Label starLabel = new Label(i <= rating ? "★" : "☆"); // Étoile pleine ou vide
                starLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: " + (i <= rating ? "#FFD700" : "#C0C0C0") + ";");
                ratingBox.getChildren().add(starLabel);
            }

            Label priceLabel = new Label("Prix: " + hotel.getPrice() + " €");
            priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");

            // Afficher la promotion si elle existe
            Label promotionLabel = new Label("Promotion: " + (hotel.getPromotion() != null ? hotel.getPromotion().getDiscount_percentage() + "% de réduction" : "Aucune promotion"));
            promotionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #FF5722;");

            // Bouton pour voir les détails
            Button detailsButton = new Button("Voir les détails");
            detailsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
            detailsButton.setOnAction(e -> showHotelDetails(hotel));

            // Ajouter les éléments à la carte
            card.getChildren().addAll(imageView, nameLabel, addressLabel, cityLabel, ratingBox, priceLabel, promotionLabel, detailsButton);

            // Ajouter la carte au conteneur
            cardContainer.getChildren().add(card);
        }
    }

    // Réinitialiser les filtres
    @FXML
    private void resetFilters() {
        searchField.clear();
        ratingFilter.getSelectionModel().clearSelection();
        priceFilter.getSelectionModel().clearSelection();
        loadHotels();  // Recharger tous les hôtels
    }

    // Afficher les détails de l'hôtel
    private void showHotelDetails(Hotel hotel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/DetailHotel.fxml"));
            Parent root = loader.load();

            DetailHotel detailController = loader.getController();
            detailController.setHotel(hotel);

            Stage stage = new Stage();
            stage.setTitle("Détails de l'hôtel");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des détails de l'hôtel.");
        }
    }

    @FXML
    private void goToAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/AccueilClient.fxml"));
            Parent root = loader.load();

            // Fermer la fenêtre actuelle
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface d'accueil.");
        }
    }

    // Méthode pour afficher un message d'alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}