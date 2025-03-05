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
import java.util.function.Consumer;

public class ModifierWebinaireController {

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
    private Webinaire webinaire;
    private Consumer<Void> refreshCallback;

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

    public void setWebinaire(Webinaire webinaire, Consumer<Void> refreshCallback) {
        this.webinaire = webinaire;
        this.refreshCallback = refreshCallback;
        title.setText(webinaire.getTitle());
        description.setText(webinaire.getDescription());
        debutDateTime.setText(webinaire.getDebutDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        finitDateTime.setText(webinaire.getFinitDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        link.setText(webinaire.getLink());
        hotelComboBox.setValue(webinaire.getHotel().getName());
    }

    @FXML
    private void update() {
        webinaire.setTitle(title.getText());
        webinaire.setDescription(description.getText());
        webinaire.setDebutDateTime(LocalDateTime.parse(debutDateTime.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        webinaire.setFinitDateTime(LocalDateTime.parse(finitDateTime.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        webinaire.setLink(link.getText());

        String selectedHotelName = hotelComboBox.getValue();
        int hotelId = hotelMap.get(selectedHotelName);
        Hotel hotel = hotelService.getById(hotelId);
        webinaire.setHotel(hotel);

        webinaireService.update(webinaire);

        if (refreshCallback != null) {
            refreshCallback.accept(null); // Rafraîchir la liste des webinaires
        }

        System.out.println("Webinaire mis à jour avec succès !");
    }
}