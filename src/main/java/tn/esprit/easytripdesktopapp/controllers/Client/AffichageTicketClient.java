package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Ticket;
import tn.esprit.easytripdesktopapp.services.ServiceTicket;
import tn.esprit.easytripdesktopapp.utils.CurrencyConverter;
import tn.esprit.easytripdesktopapp.utils.WeatherAPI;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AffichageTicketClient {

    @FXML
    private FlowPane cardContainer; // Conteneur pour les cartes de tickets

    @FXML
    private TextField searchField; // Champ de recherche

    @FXML
    private ComboBox<Float> priceFilter; // Filtre par prix maximum

    @FXML
    private ComboBox<String> currencyComboBox; // Sélection de la devise

    @FXML
    private Button backButton; // Bouton pour retourner à l'accueil

    private final CRUDService<Ticket> ticketService = new ServiceTicket();
    private List<Ticket> tickets;
    private CurrencyConverter currencyConverter = new CurrencyConverter();

    @FXML
    public void initialize() {
        // Initialiser le filtre de prix avec des valeurs
        priceFilter.getItems().addAll(50f, 100f, 150f, 200f, 250f, 300f);

        // Initialiser la sélection de la devise
        currencyComboBox.getItems().addAll("EUR", "USD", "GBP", "JPY", "CAD"); // Ajoutez d'autres devises si nécessaire
        currencyComboBox.setValue("EUR"); // Par défaut, EUR

        // Recharger les tickets lorsque la devise change
        currencyComboBox.setOnAction(e -> loadTickets());

        loadTickets(); // Charger les tickets au démarrage
    }

    @FXML
    private void loadTickets() {
        cardContainer.getChildren().clear(); // Vider le conteneur avant de le remplir
        tickets = ticketService.getAll(); // Récupérer tous les tickets

        for (Ticket ticket : tickets) {
            // Créer une carte pour chaque ticket
            VBox card = createTicketCard(ticket);
            cardContainer.getChildren().add(card);
        }
    }

    // Méthode pour créer une carte de ticket
    private VBox createTicketCard(Ticket ticket) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 10, 0, 0);");

        // Ajouter l'image de la ville
        ImageView imageView = new ImageView();
        try {
            String imagePath = ticket.getCityImage();
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                Image image = new Image(imagePath);
                imageView.setImage(image);
            } else {
                if (!imagePath.startsWith("file:")) {
                    imagePath = "file:" + imagePath;
                }
                Image image = new Image(imagePath);
                imageView.setImage(image);
            }
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            imageView.setImage(new Image("file:src/main/resources/default_image.png"));
            System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }

        // Ajouter les informations du ticket
        Text airlineText = new Text("Compagnie : " + ticket.getAirline());
        Text departureText = new Text("Départ : " + ticket.getDepartureCity() + " - " + ticket.getDepartureDate() + " " + ticket.getDepartureTime());
        Text arrivalText = new Text("Arrivée : " + ticket.getArrivalCity() + " - " + ticket.getArrivalDate() + " " + ticket.getArrivalTime());

        // Convertir le prix dans la devise sélectionnée
        String selectedCurrency = currencyComboBox.getValue();
        double convertedPrice;
        try {
            convertedPrice = currencyConverter.convert(ticket.getPrice(), "EUR", selectedCurrency);
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion du prix : " + e.getMessage());
            convertedPrice = ticket.getPrice();
        }
        Text priceText = new Text("Prix : " + String.format("%.2f", convertedPrice) + " " + selectedCurrency);

        // Ajouter la météo de la ville d'arrivée
        Text weatherText = new Text("Météo : " + WeatherAPI.getWeather(ticket.getArrivalCity()));

        // Bouton pour voir les détails
        Button detailsButton = new Button("Voir les détails");
        detailsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
        detailsButton.setOnAction(e -> showTicketDetails(ticket));

        // Ajouter les éléments à la carte
        card.getChildren().addAll(imageView, airlineText, departureText, arrivalText, priceText, weatherText, detailsButton);

        return card;
    }

    @FXML
    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        Float maxPrice = priceFilter.getValue();

        // Filtrer les tickets en fonction des critères
        List<Ticket> filteredTickets = tickets.stream()
                .filter(ticket -> ticket.getAirline().toLowerCase().contains(searchText) ||
                        ticket.getDepartureCity().toLowerCase().contains(searchText) ||
                        ticket.getArrivalCity().toLowerCase().contains(searchText))
                .filter(ticket -> maxPrice == null || ticket.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        // Effacer les cartes actuelles et afficher les tickets filtrés
        cardContainer.getChildren().clear();
        for (Ticket ticket : filteredTickets) {
            VBox card = createTicketCard(ticket);
            cardContainer.getChildren().add(card);
        }
    }

    @FXML
    private void resetFilters() {
        searchField.clear(); // Réinitialiser le champ de recherche
        priceFilter.getSelectionModel().clearSelection(); // Réinitialiser le filtre de prix
        loadTickets(); // Recharger tous les tickets
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

    private void showTicketDetails(Ticket ticket) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/DetailTicket.fxml"));
            Parent root = loader.load();

            DetailTicket detailController = loader.getController();
            detailController.setTicket(ticket);

            Stage stage = new Stage();
            stage.setTitle("Détails du Ticket");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des détails du ticket.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void goBack(ActionEvent actionEvent) {
        Stage stage;
        try {
            ResourceBundle resourcesBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/Dashboard.fxml"), resourcesBundle);
            Parent root = loader.load();
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Client Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface d'accueil.");
        }
    }
}