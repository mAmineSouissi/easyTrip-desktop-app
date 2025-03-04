package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Ticket;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServiceTicket;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.io.File;
import java.util.List;

public class ModifierTicketController {

    @FXML
    private TextField flightNumber;
    @FXML
    private TextField airline;
    @FXML
    private TextField departureCity;
    @FXML
    private TextField arrivalCity;
    @FXML
    private TextField departureDate;
    @FXML
    private TextField departureTime;
    @FXML
    private TextField arrivalDate;
    @FXML
    private TextField arrivalTime;
    @FXML
    private TextField ticketClass;
    @FXML
    private TextField price;
    @FXML
    private TextField ticketType;
    @FXML
    private TextField cityImage;
    @FXML
    private Button uploadButton;
    @FXML
    private ComboBox<String> promotionTitle;

    private Ticket ticketToUpdate;
    private final ServiceTicket ticketService = new ServiceTicket();
    private final ServicePromotion promotionService = new ServicePromotion();

    public void setTicket(Ticket ticket) {
        this.ticketToUpdate = ticket;
        flightNumber.setText(String.valueOf(ticket.getFlightNumber()));
        airline.setText(ticket.getAirline());
        departureCity.setText(ticket.getDepartureCity());
        arrivalCity.setText(ticket.getArrivalCity());
        departureDate.setText(ticket.getDepartureDate());
        departureTime.setText(ticket.getDepartureTime());
        arrivalDate.setText(ticket.getArrivalDate());
        arrivalTime.setText(ticket.getArrivalTime());
        ticketClass.setText(ticket.getTicketClass());
        price.setText(String.valueOf(ticket.getPrice())); // Afficher le prix actuel
        ticketType.setText(ticket.getTicketType());
        cityImage.setText(ticket.getCityImage());

        initializePromotionComboBox(ticket.getPromotionId());
    }

    private void initializePromotionComboBox(int currentPromotionId) {
        promotionTitle.getItems().add("Aucune promotion");
        List<Promotion> promotions = promotionService.getAll();
        for (Promotion promotion : promotions) {
            promotionTitle.getItems().add(promotion.getTitle());
        }

        if (currentPromotionId > 0) {
            Promotion currentPromotion = promotionService.getById(currentPromotionId);
            if (currentPromotion != null) {
                promotionTitle.setValue(currentPromotion.getTitle());
            } else {
                promotionTitle.setValue("Aucune promotion");
            }
        } else {
            promotionTitle.setValue("Aucune promotion");
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
    private void saveChanges() {
        if (validateFields()) {
            try {
                ticketToUpdate.setFlightNumber(Integer.parseInt(flightNumber.getText()));
                ticketToUpdate.setAirline(airline.getText());
                ticketToUpdate.setDepartureCity(departureCity.getText());
                ticketToUpdate.setArrivalCity(arrivalCity.getText());
                ticketToUpdate.setDepartureDate(departureDate.getText());
                ticketToUpdate.setDepartureTime(departureTime.getText());
                ticketToUpdate.setArrivalDate(arrivalDate.getText());
                ticketToUpdate.setArrivalTime(arrivalTime.getText());
                ticketToUpdate.setTicketClass(ticketClass.getText());
                ticketToUpdate.setTicketType(ticketType.getText());
                ticketToUpdate.setCityImage(cityImage.getText());

                // Utiliser le prix saisi dans le champ price comme base
                float pr = Float.parseFloat(price.getText());
                String promoTitle = promotionTitle.getValue();
                int promotionId = 0;

                if (!promoTitle.equals("Aucune promotion")) {
                    Promotion selectedPromotion = promotionService.getByTitle(promoTitle);
                    if (selectedPromotion != null) {
                        promotionId = selectedPromotion.getId();
                        pr = pr * (1 - selectedPromotion.getDiscount_percentage() / 100); // Appliquer la réduction au prix saisi
                    }
                }
                ticketToUpdate.setPromotionId(promotionId);
                ticketToUpdate.setPrice(pr); // Prix final après réduction ou prix saisi si aucune promotion

                ticketService.update(ticketToUpdate);
                flightNumber.getScene().getWindow().hide();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le numéro de vol et le prix doivent être des nombres valides.");
            }
        }
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        try {
            int flightNum = Integer.parseInt(flightNumber.getText());
            if (flightNum < 0) errors.append("Le numéro de vol doit être un nombre positif.\n");
        } catch (NumberFormatException e) {
            errors.append("Le numéro de vol doit être un nombre entier.\n");
        }

        if (airline.getText().isEmpty()) errors.append("La compagnie aérienne est obligatoire.\n");
        if (departureCity.getText().isEmpty()) errors.append("La ville de départ est obligatoire.\n");
        if (arrivalCity.getText().isEmpty()) errors.append("La ville d'arrivée est obligatoire.\n");
        if (departureDate.getText().isEmpty()) errors.append("La date de départ est obligatoire.\n");
        if (departureTime.getText().isEmpty()) errors.append("L'heure de départ est obligatoire.\n");
        if (arrivalDate.getText().isEmpty()) errors.append("La date d'arrivée est obligatoire.\n");
        if (arrivalTime.getText().isEmpty()) errors.append("L'heure d'arrivée est obligatoire.\n");
        if (ticketClass.getText().isEmpty()) errors.append("La classe est obligatoire.\n");
        try {
            float pr = Float.parseFloat(price.getText());
            if (pr < 0) errors.append("Le prix doit être un nombre positif.\n");
        } catch (NumberFormatException e) {
            errors.append("Le prix doit être un nombre valide.\n");
        }
        if (ticketType.getText().isEmpty()) errors.append("Le type de ticket est obligatoire.\n");
        if (cityImage.getText().isEmpty()) errors.append("L'image de la ville est obligatoire.\n");
        if (promotionTitle.getValue() == null) errors.append("La promotion doit être sélectionnée.\n");

        if (errors.length() > 0) {
            showAlert("Erreur de validation", errors.toString());
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