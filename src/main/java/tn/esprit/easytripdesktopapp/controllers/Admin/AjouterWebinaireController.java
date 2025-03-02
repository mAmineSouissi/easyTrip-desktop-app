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
                || finitDateTime.getText().isEmpty() || link.getText().isEmpty() || hotelComboBox.getValue() == null) {
            showAlert("Erreur de saisie", "Tous les champs doivent être remplis.");
            return;
        }

        // Valider le format des dates
        LocalDateTime debut;
        LocalDateTime fin;
        try {
            debut = LocalDateTime.parse(debutDateTime.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fin = LocalDateTime.parse(finitDateTime.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            showAlert("Erreur de format", "Le format de date et heure doit être 'yyyy-MM-dd HH:mm:ss'.");
            return;
        }

        // Vérifier que la date de début est avant la date de fin
        if (debut.isAfter(fin)) {
            showAlert("Erreur de date", "La date de début doit être avant la date de fin.");
            return;
        }

        // Vérifier que le lien est valide (optionnel)
        if (!link.getText().startsWith("http://") && !link.getText().startsWith("https://")) {
            showAlert("Erreur de lien", "Le lien doit commencer par 'http://' ou 'https://'.");
            return;
        }

        // Récupérer les valeurs des champs
        String titre = title.getText();
        String desc = description.getText();
        String lien = link.getText();
        String selectedHotelName = hotelComboBox.getValue();
        int hotelId = hotelMap.get(selectedHotelName);

        // Créer et sauvegarder le webinaire
        Webinaire webinaire = new Webinaire();
        webinaire.setTitle(titre);
        webinaire.setDescription(desc);
        webinaire.setDebutDateTime(debut);
        webinaire.setFinitDateTime(fin);
        webinaire.setLink(lien);

        Hotel hotel = hotelService.getById(hotelId);
        webinaire.setHotel(hotel);

        webinaireService.add(webinaire);

        // Rafraîchir la liste des webinaires si un callback est défini
        if (refreshCallback != null) {
            refreshCallback.accept(null);
        }

        // Afficher un message de succès
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