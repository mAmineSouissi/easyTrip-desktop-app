package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;
import tn.esprit.easytripdesktopapp.models.Hotel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AffichageHotel {

    @FXML
    private FlowPane cardContainer;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<Integer> ratingFilter;

    @FXML
    private ComboBox<Float> priceFilter;

    @FXML
    private Button refreshButton;

    @FXML
    private Button backButton;

    private final CRUDService<Hotel> hotelService = new ServiceHotel();
    private List<Hotel> hotels;

    @FXML
    public void initialize() {
        ratingFilter.getItems().addAll(1, 2, 3, 4, 5);
        priceFilter.getItems().addAll(50f, 100f, 150f, 200f, 250f, 300f);

        loadHotels();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters();
        });
    }

    @FXML
    private void loadHotels() {
        cardContainer.getChildren().clear();
        hotels = hotelService.getAll();

        for (Hotel hotel : hotels) {
            VBox card = new VBox(10);
            card.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 10, 0, 0);");

            ImageView imageView = new ImageView(new Image(hotel.getImage()));
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);

            Label nameLabel = new Label("Nom: " + hotel.getName());
            nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label addressLabel = new Label("Adresse: " + hotel.getAdresse());
            Label cityLabel = new Label("Ville: " + hotel.getCity());

            HBox ratingBox = new HBox(5);
            int rating = hotel.getRating();
            for (int i = 1; i <= 5; i++) {
                Label starLabel = new Label(i <= rating ? "★" : "☆");
                starLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: " + (i <= rating ? "#FFD700" : "#C0C0C0") + ";");
                ratingBox.getChildren().add(starLabel);
            }

            Label priceLabel = new Label("Prix: " + hotel.getPrice() + " €");
            priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");

            Label promotionLabel = new Label("Promotion: " + (hotel.getPromotion() != null ? hotel.getPromotion().getTitle() : "Aucune promotion"));
            promotionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #FF5722;");

            HBox buttonBox = new HBox(10);
            Button updateButton = new Button("Modifier");
            updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
            updateButton.setOnAction(e -> updateHotel(hotel));

            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");
            deleteButton.setOnAction(e -> deleteHotel(hotel));

            buttonBox.getChildren().addAll(updateButton, deleteButton);

            card.getChildren().addAll(imageView, nameLabel, addressLabel, cityLabel, ratingBox, priceLabel, promotionLabel, buttonBox);

            cardContainer.getChildren().add(card);
        }
    }

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
            VBox card = new VBox(10);
            card.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 10, 0, 0);");

            ImageView imageView = new ImageView(new Image(hotel.getImage()));
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);

            Label nameLabel = new Label("Nom: " + hotel.getName());
            nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label addressLabel = new Label("Adresse: " + hotel.getAdresse());
            Label cityLabel = new Label("Ville: " + hotel.getCity());

            HBox ratingBox = new HBox(5);
            int rating = hotel.getRating();
            for (int i = 1; i <= 5; i++) {
                Label starLabel = new Label(i <= rating ? "★" : "☆");
                starLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: " + (i <= rating ? "#FFD700" : "#C0C0C0") + ";");
                ratingBox.getChildren().add(starLabel);
            }

            Label priceLabel = new Label("Prix: " + hotel.getPrice() + " €");
            priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");

            Label promotionLabel = new Label("Promotion: " + (hotel.getPromotion() != null ? hotel.getPromotion().getTitle() : "Aucune promotion"));
            promotionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #FF5722;");

            HBox buttonBox = new HBox(10);
            Button updateButton = new Button("Modifier");
            updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
            updateButton.setOnAction(e -> updateHotel(hotel));

            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");
            deleteButton.setOnAction(e -> deleteHotel(hotel));

            buttonBox.getChildren().addAll(updateButton, deleteButton);

            card.getChildren().addAll(imageView, nameLabel, addressLabel, cityLabel, ratingBox, priceLabel, promotionLabel, buttonBox);

            cardContainer.getChildren().add(card);
        }
    }

    @FXML
    private void resetFilters() {
        searchField.clear();
        ratingFilter.getSelectionModel().clearSelection();
        priceFilter.getSelectionModel().clearSelection();
        loadHotels();
    }

    @FXML
    private void goToAddHotel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/agent/AjouterHotel.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Hôtel");
            stage.setScene(scene);
            stage.show();
            loadHotels();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface Ajouter un Hôtel.");
        }
    }

    private void updateHotel(Hotel hotel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/agent/ModifierHotel.fxml"));
            Parent root = loader.load();

            ModifierHotel modifierController = loader.getController();
            modifierController.setHotel(hotel);

            Stage stage = new Stage();
            stage.setTitle("Modifier un Hôtel");
            stage.setScene(new Scene(root, 600, 400));
            stage.showAndWait();

            // Recharger les hôtels après la modification
            loadHotels();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface de modification.");
        }
    }

    private void deleteHotel(Hotel hotel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet hôtel ?");
        alert.setContentText("Hôtel sélectionné : " + hotel.getName());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            hotelService.delete(hotel);
            loadHotels();
        }
    }

    @FXML
    private void refreshPage() {
        loadHotels();
    }

    @FXML
    private void goToAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/agent/Accueil.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Accueil");
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