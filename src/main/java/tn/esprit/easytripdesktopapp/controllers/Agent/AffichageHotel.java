package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AffichageHotel {

    @FXML private FlowPane cardContainer;
    @FXML private TextField searchField;
    @FXML private ComboBox<Integer> ratingFilter;
    @FXML private ComboBox<Float> priceFilter;
    @FXML private Button backButton;

    private final ServiceHotel hotelService = new ServiceHotel();
    private List<Hotel> hotels;
    private ResourceBundle bundle;
    private UserSession session = UserSession.getInstance();

    @FXML
    public void initialize() {
        bundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());

        // Initialisation des filtres
        ratingFilter.getItems().addAll(1, 2, 3, 4, 5);
        priceFilter.getItems().addAll(50f, 100f, 150f, 200f, 250f, 300f);

        // Chargement initial des hôtels
        loadHotels();

        // Écouteurs pour les filtres
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        ratingFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        priceFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void loadHotels() {
        try {
            hotels = hotelService.getAll();
            applyFilters();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des hôtels: " + e.getMessage());
        }
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        Integer minRating = ratingFilter.getValue();
        Float maxPrice = priceFilter.getValue();

        List<Hotel> filteredHotels = hotels.stream()
                .filter(hotel -> hotel.getName().toLowerCase().contains(searchText) ||
                        hotel.getCity().toLowerCase().contains(searchText))
                .filter(hotel -> minRating == null || hotel.getRating() >= minRating)
                .filter(hotel -> maxPrice == null || hotel.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        displayHotels(filteredHotels);
    }

    private void displayHotels(List<Hotel> hotelsToDisplay) {
        cardContainer.getChildren().clear();

        if (hotelsToDisplay.isEmpty()) {
            Label noResultsLabel = new Label("Aucun hôtel trouvé");
            noResultsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
            cardContainer.getChildren().add(noResultsLabel);
            return;
        }

        for (Hotel hotel : hotelsToDisplay) {
            VBox card = createHotelCard(hotel);
            cardContainer.getChildren().add(card);
        }
    }

    private VBox createHotelCard(Hotel hotel) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-radius: 10; " +
                "-fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 5, 0, 0);");

        // Image de l'hôtel avec gestion des erreurs
        ImageView imageView = new ImageView();
        try {
            if (hotel.getImage() != null && !hotel.getImage().isEmpty()) {
                if (hotel.getImage().startsWith("http")) {
                    imageView.setImage(new Image(hotel.getImage()));
                } else {
                    InputStream imageStream = getClass().getResourceAsStream(hotel.getImage());
                    if (imageStream != null) {
                        imageView.setImage(new Image(imageStream));
                    } else {
                        throw new Exception("Image non trouvée");
                    }
                }
            } else {
                InputStream defaultImage = getClass().getResourceAsStream("/tn/esprit/easytripdesktopapp/assets/default-hotel.png");
                imageView.setImage(new Image(defaultImage));
            }
        } catch (Exception e) {
            try {
                InputStream defaultImage = getClass().getResourceAsStream("/tn/esprit/easytripdesktopapp/assets/default-hotel.png");
                imageView.setImage(new Image(defaultImage));
            } catch (Exception ex) {
                System.err.println("Erreur lors du chargement de l'image par défaut: " + ex.getMessage());
            }
        }

        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        // Informations de l'hôtel
        Label nameLabel = new Label(hotel.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label addressLabel = new Label("Adresse: " + hotel.getAdresse());
        Label cityLabel = new Label("Ville: " + hotel.getCity());

        // Notation par étoiles
        HBox ratingBox = new HBox(5);
        int rating = hotel.getRating();
        for (int i = 1; i <= 5; i++) {
            Label starLabel = new Label(i <= rating ? "★" : "☆");
            starLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: " + (i <= rating ? "#FFD700" : "#C0C0C0") + ";");
            ratingBox.getChildren().add(starLabel);
        }

        // Prix et promotion
        Label priceLabel = new Label();
        if (hotel.getPromotion() != null) {
            priceLabel.setText(String.format("Prix: %.2f € (Promo: %.2f €)",
                    hotel.getPrice(), hotel.getDiscountedPrice()));
        } else {
            priceLabel.setText(String.format("Prix: %.2f €", hotel.getPrice()));
        }
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");

        Label promotionLabel = new Label("Promotion: " +
                (hotel.getPromotion() != null ? hotel.getPromotion().getTitle() : "Aucune"));
        promotionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #FF5722;");

        // Boutons d'action
        HBox buttonBox = new HBox(10);
        Button updateButton = new Button("Modifier");
        updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        updateButton.setOnAction(e -> updateHotel(hotel));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> deleteHotel(hotel));

        buttonBox.getChildren().addAll(updateButton, deleteButton);

        // Assemblage de la carte
        card.getChildren().addAll(
                imageView, nameLabel, addressLabel, cityLabel,
                ratingBox, priceLabel, promotionLabel, buttonBox
        );

        return card;
    }

    private void updateHotel(Hotel hotel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/ModifierHotel.fxml"));
            Parent root = loader.load();

            ModifierHotel controller = loader.getController();
            controller.setHotel(hotel);
            controller.setAffichageHotelController(this);

            Stage stage = new Stage();
            stage.setTitle("Modifier l'hôtel");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'interface de modification: " + e.getMessage());
        }
    }

    private void deleteHotel(Hotel hotel) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer l'hôtel " + hotel.getName() + " ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                hotelService.delete(hotel);
                hotels.remove(hotel);
                applyFilters();
            } catch (Exception e) {
                showAlert("Erreur", "Échec de la suppression: " + e.getMessage());
            }
        }
    }

    public void refreshHotels() {
        loadHotels();
    }

    @FXML
    private void goToAddHotel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/AjouterHotel.fxml"));
            Parent root = loader.load();

            AjouterHotel controller = loader.getController();
            controller.setAffichageHotel(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un hôtel");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'interface d'ajout: " + e.getMessage());
        }
    }

    @FXML
    private void goToAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Dashboard.fxml"));
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}