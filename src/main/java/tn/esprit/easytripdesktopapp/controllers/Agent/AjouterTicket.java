package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Ticket;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServiceTicket;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class AjouterTicket {

    private final ServiceTicket ticketService = new ServiceTicket();
    private final ServicePromotion promotionService = new ServicePromotion(); // Service pour gérer les promotions
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
    private TextField agencyId;
    @FXML
    private ComboBox<String> promotionTitle; // Remplacement de promotionId par une ComboBox
    @FXML
    private Button uploadButton;

    UserSession session= UserSession.getInstance();

    @FXML
    public void initialize() {
        ticketClass.getItems().addAll("Economy", "Business", "First");
        ticketType.getItems().addAll("One-way", "Round-trip");

        // Charger les titres des promotions dans la ComboBox
        List<Promotion> promotions = promotionService.getAll();
        for (Promotion promotion : promotions) {
            promotionTitle.getItems().add(promotion.getTitle());
        }

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

        departureDate.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (arrivalDate.getValue() != null && newDate != null && arrivalDate.getValue().isBefore(newDate)) {
                arrivalDate.setValue(newDate);
            }
        });
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
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
                int flightNum = Integer.parseInt(flightNumber.getText());
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
                int agId = Integer.parseInt(agencyId.getText());
                String promoTitle = promotionTitle.getValue();

                // Récupérer l'ID de la promotion à partir du titre
                Promotion selectedPromotion = promotionService.getByTitle(promoTitle);
                if (selectedPromotion == null) {
                    showAlert("Erreur", "Promotion non trouvée.");
                    return;
                }

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
                ticket.setPrice(pr);
                ticket.setTicketType(tType);
                ticket.setCityImage(cityImg);
                ticket.setAgencyId(agId);
                ticket.setPromotionId(selectedPromotion.getId());
                ticket.setUserId(session.getUser().getId());

                ticketService.add(ticket);
                flightNumber.getScene().getWindow().hide();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Numéro de vol et prix doivent être des nombres valides.");
            }
        }
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
        if (agencyId.getText().isEmpty()) errors.append("ID de l'agence requis.\n");
        if (promotionTitle.getValue() == null) errors.append("Promotion requise.\n");

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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}