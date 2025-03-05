package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.models.Webinaire;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;
import tn.esprit.easytripdesktopapp.services.ServiceWebinaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AjouterWebinaireController {

    @FXML
    private TextField title;

    @FXML
    private TextField description;

    @FXML
    private TextField debutDateTime;

    @FXML
    private TextField finitDateTime;

    @FXML
    private TextField link;

    @FXML
    private ComboBox<String> hotelComboBox;

    private final ServiceHotel hotelService = new ServiceHotel();
    private final ServiceWebinaire webinaireService = new ServiceWebinaire();

    private Map<String, Integer> hotelMap = new HashMap<>();
    private Consumer<Void> refreshCallback;

    @FXML
    public void initialize() {
        loadHotels();
    }

    /**
     * Charge la liste des hôtels dans la ComboBox.
     */
    private void loadHotels() {
        List<Hotel> hotels = hotelService.getAll();
        for (Hotel hotel : hotels) {
            System.out.println("Ajout de l'hôtel dans ComboBox : " + hotel.getName() + " (ID: " + hotel.getId() + ")");
            hotelComboBox.getItems().add(hotel.getName());
            hotelMap.put(hotel.getName(), hotel.getId());
        }
    }

    /**
     * Sauvegarde un nouveau webinaire après avoir validé les saisies.
     */
    @FXML
    private void save() {
        // Vérifier que tous les champs obligatoires sont remplis
        if (title.getText().isEmpty() || description.getText().isEmpty() || debutDateTime.getText().isEmpty()
                || finitDateTime.getText().isEmpty()  || hotelComboBox.getValue() == null) {
            showAlert("Erreur de saisie", "Tous les champs doivent être remplis, y compris l'hôtel.");
            return;
        }

        LocalDateTime debut;
        LocalDateTime fin;
        try {
            debut = LocalDateTime.parse(debutDateTime.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fin = LocalDateTime.parse(finitDateTime.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            showAlert("Erreur de format", "Le format de date et heure doit être 'yyyy-MM-dd HH:mm:ss'.");
            return;
        }

        if (debut.isAfter(fin)) {
            showAlert("Erreur de date", "La date de début doit être avant la date de fin.");
            return;
        }


        String titre = title.getText();
        String desc = description.getText();
        String selectedHotelName = hotelComboBox.getValue();
        Integer hotelId = hotelMap.get(selectedHotelName);

        if (hotelId == null) {
            showAlert("Erreur", "L'hôtel sélectionné n'est pas valide.");
            return;
        }

        Webinaire webinaire = new Webinaire();
        webinaire.setTitle(titre);
        webinaire.setDescription(desc);
        webinaire.setDebutDateTime(debut);
        webinaire.setFinitDateTime(fin);


        Hotel hotel = hotelService.getById(hotelId);
        if (hotel == null) {
            showAlert("Erreur", "L'hôtel avec l'ID " + hotelId + " n'existe pas dans la base de données.");
            return;
        }
        webinaire.setHotel(hotel);

        try {
            webinaireService.add(webinaire);
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", e.getMessage());
            return;
        } catch (RuntimeException e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout du webinaire : " + e.getMessage());
            return;
        }

        if (refreshCallback != null) {
            refreshCallback.accept(null);
        }

        showAlert("Succès", "Webinaire ajouté avec succès !");
    }

    /**
     * Affiche une alerte avec un message d'erreur ou de succès.
     *
     * @param title   Le titre de l'alerte.
     * @param message Le message à afficher.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Définit le callback pour rafraîchir la liste des webinaires après l'ajout.
     *
     * @param refreshCallback Le callback à exécuter.
     */
    public void setRefreshCallback(Consumer<Void> refreshCallback) {
        this.refreshCallback = refreshCallback;
    }
}