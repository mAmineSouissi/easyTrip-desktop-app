package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Ticket;
import tn.esprit.easytripdesktopapp.services.ServiceTicket;
import tn.esprit.easytripdesktopapp.utils.CurrencyConverter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AffichageTicket {

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

        // Ajouter un Listener sur le champ de recherche pour la recherche dynamique
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters(); // Appliquer les filtres à chaque changement de texte
        });

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
            // Récupérer le chemin de l'image depuis le ticket
            String imagePath = ticket.getCityImage();

            // Si le chemin est une URL web (commence par http ou https), charger directement
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                // Charger l'image depuis le web
                Image image = new Image(imagePath);
                imageView.setImage(image);
            }
            // Si le chemin est local (depuis le bureau ou un autre dossier)
            else {
                // Ajouter le préfixe "file:" si nécessaire
                if (!imagePath.startsWith("file:")) {
                    imagePath = "file:" + imagePath;
                }
                // Charger l'image locale
                Image image = new Image(imagePath);
                imageView.setImage(image);
            }

            // Ajuster la taille de l'image
            imageView.setFitWidth(200); // Ajuster la largeur de l'image
            imageView.setFitHeight(150); // Ajuster la hauteur de l'image
            imageView.setPreserveRatio(true); // Conserver le ratio de l'image
        } catch (Exception e) {
            // Si l'image ne peut pas être chargée, utiliser une image par défaut
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
            convertedPrice = currencyConverter.convert(ticket.getPrice(), "EUR", selectedCurrency); // Par défaut, EUR
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion du prix : " + e.getMessage());
            convertedPrice = ticket.getPrice(); // Utiliser le prix d'origine en cas d'erreur
        }
        Text priceText = new Text("Prix : " + String.format("%.2f", convertedPrice) + " " + selectedCurrency);

        // Ajouter les informations de la promotion
        Text promotionText = new Text("Promotion : " + (ticket.getPromotion() != null ? ticket.getPromotion().getTitle() : "Aucune promotion"));
        promotionText.setStyle("-fx-font-size: 14px; -fx-text-fill: #FF5722;");

        // Boutons pour modifier et supprimer
        HBox buttonBox = new HBox(10);
        Button updateButton = new Button("Modifier");
        updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
        updateButton.setOnAction(e -> updateTicket(ticket));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");
        deleteButton.setOnAction(e -> deleteTicket(ticket));

        buttonBox.getChildren().addAll(updateButton, deleteButton);

        // Ajouter les éléments à la carte
        card.getChildren().addAll(imageView, airlineText, departureText, arrivalText, priceText, promotionText, buttonBox);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/agent/Accueil.fxml"));
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

    private void updateTicket(Ticket ticket) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/agent/ModifierTicket.fxml"));
            Parent root = loader.load();

            ModifierTicketController modifierController = loader.getController();
            modifierController.setTicket(ticket);

            Stage stage = new Stage();
            stage.setTitle("Modifier un Ticket");
            stage.setScene(new Scene(root, 600, 400));
            stage.showAndWait();
            loadTickets(); // Recharger les tickets après modification
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface de modification.");
        }
    }

    private void deleteTicket(Ticket ticket) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce ticket ?");
        alert.setContentText("Ticket sélectionné : " + ticket.getAirline());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ticketService.delete(ticket);
            loadTickets(); // Recharger les tickets après suppression
        }
    }

    @FXML
    private void goToAddTicket() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/agent/AjouterTicket.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);;
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Ticket");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface Ajouter un Ticket.");
        }
    }

    @FXML
    private void refreshTickets() {
        loadTickets(); // Recharger les tickets
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}