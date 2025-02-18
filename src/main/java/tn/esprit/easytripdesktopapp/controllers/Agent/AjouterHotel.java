package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;

public class AjouterHotel {
    @FXML
    private TextField name; // Renommé
    @FXML
    private TextField adresse; // Renommé
    @FXML
    private TextField city; // Renommé
    @FXML
    private TextField rating; // Renommé
    @FXML
    private TextField description; // Renommé
    @FXML
    private TextField price; // Renommé
    @FXML
    private TextField typeroom; // Renommé
    @FXML
    private TextField numroom; // Renommé
    @FXML
    private TextField image; // Renommé

    @FXML
    private Label lbHotels;

    CRUDService<Hotel> sh = new ServiceHotel();

    @FXML
    public void save(ActionEvent actionEvent) {
        // Créer un nouvel hôtel avec les données du formulaire
        Hotel hotel = new Hotel();
        hotel.setName(name.getText()); // Utilisation du nouveau nom
        hotel.setAdresse(adresse.getText()); // Utilisation du nouveau nom
        hotel.setCity(city.getText()); // Utilisation du nouveau nom
        hotel.setRating(Integer.parseInt(rating.getText())); // Utilisation du nouveau nom
        hotel.setDescription(description.getText()); // Utilisation du nouveau nom
        hotel.setPrice(Float.parseFloat(price.getText())); // Utilisation du nouveau nom
        hotel.setTypeRoom(typeroom.getText()); // Utilisation du nouveau nom
        hotel.setNumRoom(Integer.parseInt(numroom.getText())); // Utilisation du nouveau nom
        hotel.setImage(image.getText()); // Utilisation du nouveau nom

        // Ajouter l'hôtel via le service
        sh.add(hotel);

        // Afficher un message de confirmation
        System.out.println("Hôtel ajouté avec succès !");
    }

    @FXML
    public void afficherHotels(ActionEvent actionEvent) {
        // Récupérer tous les hôtels et les afficher dans le label
        lbHotels.setText(sh.getAll().toString());
    }
}