package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Ticket;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServiceTicket;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class AjouterTicket {

    @FXML
    private TextField flightNumber;
    @FXML
    private TextField airline;
    @FXML
    private TextField departureCity;
    @FXML
    private TextField arrivalCity;
    @FXML
    private DatePicker departureDate;
    @FXML
    private TextField departureTime;
    @FXML
    private DatePicker arrivalDate;
    @FXML
    private TextField arrivalTime;
    @FXML
    private ComboBox<String> ticketClass;
    @FXML
    private TextField price;
    @FXML
    private ComboBox<String> ticketType;
    @FXML
    private TextField cityImage;
    @FXML
    private ComboBox<String> promotionComboBox; // ComboBox pour les promotions
    @FXML
    private CheckBox promotionSwitch; // CheckBox pour activer/désactiver la promotion
    @FXML
    private Button uploadButton;

    private final ServiceTicket ticketService = new ServiceTicket();
    private final ServicePromotion promotionService = new ServicePromotion();

    @FXML
    public void initialize() {
        // Initialiser les ComboBox
        ticketClass.getItems().addAll("Economy", "Business", "First");
        ticketType.getItems().addAll("One-way", "Round-trip");

        // Charger les promotions dans le ComboBox
        loadPromotions();

        // Activer/désactiver le ComboBox des promotions en fonction du CheckBox
        promotionSwitch.setOnAction(e -> togglePromotion());

        // Configurer les DatePickers pour empêcher les dates passées
        departureDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #EEEEEE;");
                }
            }
        });

        arrivalDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (departureDate.getValue() != null && item.isBefore(departureDate.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #EEEEEE;");
                }
            }
        });

        // Mettre à jour la date d'arrivée si la date de départ change
        departureDate.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (arrivalDate.getValue() != null && newDate != null && arrivalDate.getValue().isBefore(newDate)) {
                arrivalDate.setValue(newDate);
            }
        });
    }

    private void loadPromotions() {
        List<Promotion> promotions = promotionService.getAll();
        for (Promotion promotion : promotions) {
            promotionComboBox.getItems().add(promotion.getTitle());
        }
    }

    @FXML
    private void togglePromotion() {
        if (promotionSwitch.isSelected()) {
            promotionComboBox.setDisable(false); // Activer le ComboBox des promotions
        } else {
            promotionComboBox.setDisable(true); // Désactiver le ComboBox des promotions
            promotionComboBox.setValue(null);   // Effacer la sélection de la promotion
        }
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            cityImage.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void save() {
        if (validateFields()) {
            try {
                // Parse flightNumber
                int flightNum = flightNumber.getText() != null && !flightNumber.getText().isEmpty()
                        ? Integer.parseInt(flightNumber.getText())
                        : 0; // Default value or throw an error

                // Ensure flightNum is valid
                if (flightNum <= 0) {
                    showAlert("Erreur", "Le numéro de vol doit être un nombre valide et positif.");
                    return;
                }

                // Other fields
                String air = airline.getText();
                String depCity = departureCity.getText();
                String arrCity = arrivalCity.getText();
                LocalDate depDate = departureDate.getValue();
                String depTime = departureTime.getText();
                LocalDate arrDate = arrivalDate.getValue();
                String arrTime = arrivalTime.getText();
                String tClass = ticketClass.getValue();
                float pr = Float.parseFloat(price.getText());
                String tType = ticketType.getValue();
                String cityImg = cityImage.getText();

                // Gérer la promotion
                Promotion promotion = null;
                if (promotionSwitch.isSelected()) {
                    String promotionTitle = promotionComboBox.getValue();
                    if (promotionTitle != null) {
                        promotion = promotionService.getByTitle(promotionTitle);
                    }
                }

                // Appliquer la promotion au prix
                float finalPrice = calculateDiscountedPrice(pr, promotion);

                // Create the Ticket object
                Ticket ticket = new Ticket();
                ticket.setFlightNumber(flightNum);
                ticket.setAirline(air);
                ticket.setDepartureCity(depCity);
                ticket.setArrivalCity(arrCity);
                ticket.setDepartureDate(depDate.toString());
                ticket.setDepartureTime(depTime);
                ticket.setArrivalDate(arrDate.toString());
                ticket.setArrivalTime(arrTime);
                ticket.setTicketClass(tClass);
                ticket.setPrice(finalPrice);
                ticket.setTicketType(tType);
                ticket.setCityImage(cityImg);
                ticket.setPromotion(promotion); // Associer la promotion (peut être null)

                // Save the ticket
                ticketService.add(ticket);

                // Close the window
                flightNumber.getScene().getWindow().hide();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Numéro de vol et prix doivent être des nombres valides.");
            }
        }
    }

    private float calculateDiscountedPrice(float originalPrice, Promotion promotion) {
        if (promotion != null) {
            float discountPercentage = promotion.getDiscount_percentage();
            return originalPrice * (1 - discountPercentage / 100);
        }
        return originalPrice; // Si aucune promotion n'est sélectionnée, retourner le prix original
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (flightNumber.getText().isEmpty()) errors.append("Numéro de vol requis.\n");
        if (airline.getText().isEmpty()) errors.append("Compagnie aérienne requise.\n");
        if (departureCity.getText().isEmpty()) errors.append("Ville de départ requise.\n");
        if (arrivalCity.getText().isEmpty()) errors.append("Ville d'arrivée requise.\n");
        if (departureDate.getValue() == null) errors.append("Date de départ requise.\n");
        if (departureTime.getText().isEmpty()) errors.append("Heure de départ requise.\n");
        if (arrivalDate.getValue() == null) errors.append("Date d'arrivée requise.\n");
        if (arrivalTime.getText().isEmpty()) errors.append("Heure d'arrivée requise.\n");
        if (ticketClass.getValue() == null) errors.append("Classe requise.\n");
        if (ticketType.getValue() == null) errors.append("Type de billet requis.\n");
        if (cityImage.getText().isEmpty()) errors.append("Image de la ville requise.\n");

        // Validation du format de l'heure pour departureTime et arrivalTime
        if (!isValidTimeFormat(departureTime.getText())) {
            errors.append("Format de l'heure de départ invalide. Utilisez HH:MM.\n");
        }
        if (!isValidTimeFormat(arrivalTime.getText())) {
            errors.append("Format de l'heure d'arrivée invalide. Utilisez HH:MM.\n");
        }

        // Validation de la promotion uniquement si elle est activée
        if (promotionSwitch.isSelected() && promotionComboBox.getValue() == null) {
            errors.append("Promotion requise.\n");
        }

        try {
            float pr = Float.parseFloat(price.getText());
            if (pr < 0) errors.append("Le prix doit être positif.\n");
        } catch (NumberFormatException e) {
            errors.append("Prix invalide.\n");
        }

        if (errors.length() > 0) {
            showAlert("Validation", errors.toString());
            return false;
        }
        return true;
    }

    private boolean isValidTimeFormat(String time) {
        // Regex pour valider le format HH:MM
        String timeRegex = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
        return time.matches(timeRegex);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}