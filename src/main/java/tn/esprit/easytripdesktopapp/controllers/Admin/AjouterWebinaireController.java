package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.models.Webinaire;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;
import tn.esprit.easytripdesktopapp.services.ServiceWebinaire;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Map pour associer le nom de l'hôtel à son ID
    private Map<String, Integer> hotelMap = new HashMap<>();

    @FXML
    public void initialize() {
        loadHotels();
    }

    private void loadHotels() {
        List<Hotel> hotels = hotelService.getAll();
        for (Hotel hotel : hotels) {
            hotelComboBox.getItems().add(hotel.getName());
            hotelMap.put(hotel.getName(), hotel.getId());
        }
    }

    @FXML
    private void save() {
        String titre = title.getText();
        String desc = description.getText();
        LocalDateTime debut = LocalDateTime.parse(debutDateTime.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime fin = LocalDateTime.parse(finitDateTime.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String lien = link.getText();

        // Récupérer l'ID de l'hôtel sélectionné
        String selectedHotelName = hotelComboBox.getValue();
        int hotelId = hotelMap.get(selectedHotelName);

        // Créer un nouvel objet Webinaire
        Webinaire webinaire = new Webinaire();
        webinaire.setTitle(titre);
        webinaire.setDescription(desc);
        webinaire.setDebutDateTime(debut);
        webinaire.setFinitDateTime(fin);
        webinaire.setLink(lien);

        // Récupérer l'hôtel associé
        Hotel hotel = hotelService.getById(hotelId);
        webinaire.setHotel(hotel);

        // Ajouter le webinaire à la base de données
        webinaireService.add(webinaire);

        // Afficher un message de succès ou rediriger l'utilisateur
        System.out.println("Webinaire ajouté avec succès !");
    }
}