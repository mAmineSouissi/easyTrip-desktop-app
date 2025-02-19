package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;

public class ModifierHotel {

    @FXML
    private TextField name;
    @FXML
    private TextField adresse;
    @FXML
    private TextField city;
    @FXML
    private TextField rating;
    @FXML
    private TextField description;
    @FXML
    private TextField price;
    @FXML
    private TextField typeroom;
    @FXML
    private TextField numroom;
    @FXML
    private TextField image;

    private Hotel hotelToUpdate;
    private final ServiceHotel hotelService = new ServiceHotel();

    // Méthode pour initialiser les champs avec les informations de l'hôtel à mettre à jour
    public void setHotel(Hotel hotel) {
        this.hotelToUpdate = hotel;
        name.setText(hotel.getName());
        adresse.setText(hotel.getAdresse());
        city.setText(hotel.getCity());
        rating.setText(String.valueOf(hotel.getRating()));
        description.setText(hotel.getDescription());
        price.setText(String.valueOf(hotel.getPrice()));
        typeroom.setText(hotel.getTypeRoom());
        numroom.setText(String.valueOf(hotel.getNumRoom()));
        image.setText(hotel.getImage());
    }

    // Méthode pour enregistrer les modifications
    @FXML
    private void saveChanges() {
        // Mettre à jour l'objet Hotel avec les nouvelles valeurs
        hotelToUpdate.setName(name.getText());
        hotelToUpdate.setAdresse(adresse.getText());
        hotelToUpdate.setCity(city.getText());
        hotelToUpdate.setRating(Integer.parseInt(rating.getText()));
        hotelToUpdate.setDescription(description.getText());
        hotelToUpdate.setPrice(Float.parseFloat(price.getText()));
        hotelToUpdate.setTypeRoom(typeroom.getText());
        hotelToUpdate.setNumRoom(Integer.parseInt(numroom.getText()));
        hotelToUpdate.setImage(image.getText());

        // Appeler le service pour mettre à jour l'hôtel dans la base de données
        hotelService.update(hotelToUpdate);

        // Fermer la fenêtre de mise à jour
        name.getScene().getWindow().hide();
    }
}