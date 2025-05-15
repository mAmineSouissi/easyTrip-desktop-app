package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import tn.esprit.easytripdesktopapp.services.ServicePromotion;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.utils.CurrencyConverter;
import tn.esprit.easytripdesktopapp.utils.UserSession;
import tn.esprit.easytripdesktopapp.utils.WeatherAPI;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AffichageTicket {
    private final ServiceTicket ticketService = new ServiceTicket();
    private final ServicePromotion promotionService = new ServicePromotion();
    private final CurrencyConverter currencyConverter = new CurrencyConverter();
    UserSession session = UserSession.getInstance();
    @FXML
    private FlowPane cardContainer;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Float> priceFilter;
    @FXML
    private ComboBox<String> currencyComboBox;
    @FXML
    private Button backButton;
    private List<Ticket> tickets;
    ResourceBundle bundel;

    @FXML
    public void initialize() {
        bundel = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
        priceFilter.getItems().addAll(50f, 100f, 150f, 200f, 250f, 300f);
        currencyComboBox.getItems().addAll("EUR", "USD", "GBP", "JPY", "CAD");
        currencyComboBox.setValue("EUR");
        currencyComboBox.setOnAction(e -> loadTickets());
        loadTickets();
    }

    @FXML
    private void loadTickets() {
        cardContainer.getChildren().clear();
        tickets = ticketService.getTicketsByUserId(session.getUser().getId());
        for (Ticket ticket : tickets) {
            VBox card = createTicketCard(ticket);
            cardContainer.getChildren().add(card);
        }
    }

    private VBox createTicketCard(Ticket ticket) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 10, 0, 0);");

        // Image de la ville
        ImageView cityImageView = new ImageView();
        loadImage(ticket.getCityImage(), cityImageView, 200, 150);

        // Image de la compagnie aérienne
        ImageView airlineImageView = new ImageView();
        loadImage(ticket.getImageAirline(), airlineImageView, 100, 50);

        Text airlineText = new Text("Compagnie : " + ticket.getAirline());
        Text departureText = new Text("Départ : " + ticket.getDepartureCity() + " - " + ticket.getDepartureDate() + " " + ticket.getDepartureTime());
        Text arrivalText = new Text("Arrivée : " + ticket.getArrivalCity() + " - " + ticket.getArrivalDate() + " " + ticket.getArrivalTime());

        String selectedCurrency = currencyComboBox.getValue();
        double convertedPrice;
        try {
            convertedPrice = currencyConverter.convert(ticket.getPrice(), "EUR", selectedCurrency);
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion du prix : " + e.getMessage());
            convertedPrice = ticket.getPrice();
        }
        Text priceText = new Text("Prix : " + String.format("%.2f", convertedPrice) + " " + selectedCurrency);

        String promotionTitle = "Aucune promotion";
        if (ticket.getPromotionId() > 0) {
            Promotion promotion = promotionService.getById(ticket.getPromotionId());
            if (promotion != null) {
                promotionTitle = promotion.getTitle();
            }
        }
        Text promotionText = new Text("Promotion : " + promotionTitle);

        Text weatherText = new Text("Météo : " + WeatherAPI.getWeather(ticket.getArrivalCity()));

        HBox buttonBox = new HBox(10);
        Button updateButton = new Button("Modifier");
        updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
        updateButton.setOnAction(e -> updateTicket(ticket));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");
        deleteButton.setOnAction(e -> deleteTicket(ticket));

        buttonBox.getChildren().addAll(updateButton, deleteButton);

        card.getChildren().addAll(cityImageView, airlineImageView, airlineText, departureText, arrivalText, priceText, promotionText, weatherText, buttonBox);

        return card;
    }

    private void loadImage(String imagePath, ImageView imageView, double width, double height) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                    imageView.setImage(new Image(imagePath));
                } else {
                    if (!imagePath.startsWith("file:")) {
                        imagePath = "file:" + imagePath;
                    }
                    imageView.setImage(new Image(imagePath));
                }
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                imageView.setPreserveRatio(true);
            } else {
                imageView.setImage(new Image("file:src/main/resources/default_image.png"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            imageView.setImage(new Image("file:src/main/resources/default_image.png"));
        }
    }

    @FXML
    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        Float maxPrice = priceFilter.getValue();

        List<Ticket> filteredTickets = tickets.stream()
                .filter(ticket -> ticket.getAirline().toLowerCase().contains(searchText) ||
                        ticket.getDepartureCity().toLowerCase().contains(searchText) ||
                        ticket.getArrivalCity().toLowerCase().contains(searchText))
                .filter(ticket -> maxPrice == null || ticket.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        cardContainer.getChildren().clear();
        for (Ticket ticket : filteredTickets) {
            VBox card = createTicketCard(ticket);
            cardContainer.getChildren().add(card);
        }
    }

    @FXML
    private void resetFilters() {
        searchField.clear();
        priceFilter.getSelectionModel().clearSelection();
        loadTickets();
    }

    @FXML
    private void goToAccueil(ActionEvent actionEvent) {
        Stage stage;
        Scene scene;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(getClass().getResource(
                    "/tn/esprit/easytripdesktopapp/FXML/Agent/Dashboard.fxml"),
                    bundel);
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(bundel.getString(session.getUser().getRole().toLowerCase() + "_dashboard"));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateTicket(Ticket ticket) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/ModifierTicket.fxml"));
            Parent root = loader.load();

            ModifierTicketController modifierController = loader.getController();
            modifierController.setTicket(ticket);

            Stage stage = new Stage();
            stage.setTitle("Modifier un Ticket");
            stage.setScene(new Scene(root, 600, 400));
            stage.showAndWait();
            loadTickets();
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
            loadTickets();
        }
    }

    @FXML
    private void goToAddTicket() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/AjouterTicket.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);
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
        loadTickets();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}