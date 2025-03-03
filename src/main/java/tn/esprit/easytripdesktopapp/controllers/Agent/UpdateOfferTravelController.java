package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.models.Category;
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;
import tn.esprit.easytripdesktopapp.services.ServiceOfferTravel;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateOfferTravelController implements Initializable {

    @FXML private TextField tfDeparture, tfDestination, tfHotelName, tfFlightName, tfPrice;
    @FXML private DatePicker dpDepartureDate, dpArrivalDate;
    @FXML private TextArea taDescription;
    @FXML private ComboBox<Agence> cbAgence;
    @FXML private ComboBox<Promotion> cbPromotion;
    @FXML private ComboBox<Category> cbCategory;
    @FXML private ImageView imgOffer;
    @FXML private Button btnAjouter, btnChoisirImage;

    private OfferTravel selectedOffer;
    private ServiceOfferTravel serviceOfferTravel = new ServiceOfferTravel();
    private ServiceAgence serviceAgence = new ServiceAgence();
    private ServicePromotion servicePromotion = new ServicePromotion();
    private String imagePath = "";

    private float initialPrice; // Pour stocker le prix initial (avant promotion)

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAgences();
        loadPromotions();
    }

    public void setSelectedOffer(OfferTravel offer) {
        this.selectedOffer = offer;
        populateFields();
    }

    private void populateFields() {
        if (selectedOffer != null) {
            tfDeparture.setText(selectedOffer.getDeparture());
            tfDestination.setText(selectedOffer.getDestination());

            // Conversion Date → LocalDate
            if (selectedOffer.getDeparture_date() != null) {
                Date sqlDepartureDate = new Date(selectedOffer.getDeparture_date().getTime());
                dpDepartureDate.setValue(sqlDepartureDate.toLocalDate());
            }

            if (selectedOffer.getArrival_date() != null) {
                Date sqlArrivalDate = new Date(selectedOffer.getArrival_date().getTime());
                dpArrivalDate.setValue(sqlArrivalDate.toLocalDate());
            }

            tfHotelName.setText(selectedOffer.getHotelName());
            tfFlightName.setText(selectedOffer.getFlightName());
            taDescription.setText(selectedOffer.getDiscription());

            // Récupérer le prix initial (avant promotion)
            initialPrice = getInitialPrice(selectedOffer.getPrice(), selectedOffer.getPromotion());
            tfPrice.setText(String.valueOf(initialPrice)); // Afficher le prix initial

            // Sélectionner l'agence et la promotion correspondantes
            cbAgence.setValue(selectedOffer.getAgence());
            cbPromotion.setValue(selectedOffer.getPromotion()); // Peut être null si aucune promotion
            cbCategory.setValue(selectedOffer.getCategory());

            // Affichage de l'image
            if (selectedOffer.getImage() != null && !selectedOffer.getImage().isEmpty()) {
                imagePath = selectedOffer.getImage();
                File file = new File(imagePath);
                if (file.exists()) {
                    imgOffer.setImage(new Image(file.toURI().toString()));
                }
            }
        }
    }

    @FXML
    private void updateOffer(ActionEvent event) {
        try {
            String departure = tfDeparture.getText();
            String destination = tfDestination.getText();
            LocalDate departureLocalDate = dpDepartureDate.getValue();
            LocalDate arrivalLocalDate = dpArrivalDate.getValue();
            String hotelName = tfHotelName.getText();
            String flightName = tfFlightName.getText();
            String description = taDescription.getText();
            float initialPrice = Float.parseFloat(tfPrice.getText()); // Prix initial saisi par l'utilisateur
            Agence agence = cbAgence.getValue();
            Promotion promotion = cbPromotion.getValue(); // Peut être null si "Aucune promotion" est sélectionnée
            Category category = cbCategory.getValue();

            // Vérification des champs
            if (departure.isEmpty() || destination.isEmpty() || hotelName.isEmpty() || flightName.isEmpty() || description.isEmpty() || category == null || agence == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires !", Alert.AlertType.ERROR);
                return;
            }

            // Convertir LocalDate → java.sql.Date
            Date departureDate = (departureLocalDate != null) ? Date.valueOf(departureLocalDate) : null;
            Date arrivalDate = (arrivalLocalDate != null) ? Date.valueOf(arrivalLocalDate) : null;

            // Calculer le prix final après application de la nouvelle promotion (ou sans promotion)
            float finalPrice = getFinalPrice(initialPrice, promotion);

            // Mettre à jour l'offre
            selectedOffer.setDeparture(departure);
            selectedOffer.setDestination(destination);
            selectedOffer.setDeparture_date(departureDate);
            selectedOffer.setArrival_date(arrivalDate);
            selectedOffer.setHotelName(hotelName);
            selectedOffer.setFlightName(flightName);
            selectedOffer.setDiscription(description);
            selectedOffer.setPrice(finalPrice); // Enregistrer le prix final après promotion
            selectedOffer.setAgence(agence);
            selectedOffer.setPromotion(promotion); // Peut être null si "Aucune promotion" est sélectionnée
            selectedOffer.setCategory(category);
            selectedOffer.setImage(imagePath); // Mise à jour de l'image

            // Enregistrer la mise à jour
            serviceOfferTravel.update(selectedOffer);

            showAlert("Succès", "L'offre a été mise à jour avec succès !", Alert.AlertType.INFORMATION);
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide !", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.getAbsolutePath();
            imgOffer.setImage(new Image(file.toURI().toString()));
        }
    }

    private void loadAgences() {
        ObservableList<Agence> agences = FXCollections.observableArrayList(serviceAgence.getAll());
        cbAgence.setItems(agences);
        cbAgence.setConverter(new StringConverter<>() {
            @Override
            public String toString(Agence agence) {
                return (agence != null) ? agence.getNom() : "";
            }

            @Override
            public Agence fromString(String string) {
                return cbAgence.getItems().stream()
                        .filter(a -> a.getNom().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void loadPromotions() {
        ObservableList<Promotion> promotions = FXCollections.observableArrayList(servicePromotion.getAll());

        // Ajouter une option "Aucune promotion" avec une valeur null
        promotions.add(0, null); // Ajouter en première position

        cbPromotion.setItems(promotions);
        cbPromotion.setConverter(new StringConverter<>() {
            @Override
            public String toString(Promotion promotion) {
                return (promotion != null) ? promotion.getTitle() : "Aucune promotion"; // Afficher "Aucune promotion" si null
            }

            @Override
            public Promotion fromString(String string) {
                return cbPromotion.getItems().stream()
                        .filter(p -> p != null && p.getTitle().equals(string))
                        .findFirst()
                        .orElse(null); // Retourner null si "Aucune promotion" est sélectionnée
            }
        });
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnAjouter.getScene().getWindow();
        stage.close();
    }

    // Méthode pour calculer le prix final après application de la promotion
    private float getFinalPrice(float initialPrice, Promotion promotion) {
        if (promotion != null) {
            return initialPrice - (initialPrice * promotion.getDiscount_percentage() / 100);
        } else {
            return initialPrice; // Aucune promotion, le prix reste inchangé
        }
    }

    // Méthode pour récupérer le prix initial avant application de la promotion
    private float getInitialPrice(float currentPrice, Promotion promotion) {
        if (promotion != null) {
            return currentPrice / (1 - (promotion.getDiscount_percentage() / 100));
        } else {
            return currentPrice; // Aucune promotion, le prix initial est le prix actuel
        }
    }
}