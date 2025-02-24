package tn.esprit.easytripdesktopapp.controllers.Admin.Agence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;

import java.io.File;

public class ajouter_agence {

    @FXML
    private TextField address;

    @FXML
    private TextField email;

    @FXML
    private ImageView image;

    @FXML
    private TextField name;

    @FXML
    private TextField phone;

    @FXML
    private Button chooseImageButton;

    private String imageUrl;


    CRUDService<Agence> agence = new ServiceAgence();


    @FXML
    void save(ActionEvent event) {

        if (name.getText().isEmpty() || address.getText().isEmpty() || phone.getText().isEmpty() || email.getText().isEmpty() || imageUrl == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !", Alert.AlertType.ERROR);
            return;
        }

        Agence ag = new Agence();
        ag.setNom(name.getText());
        ag.setAddress(address.getText());
        ag.setPhone(phone.getText());
        ag.setEmail(email.getText());
        ag.setImage(imageUrl);

        agence.add(ag);

        showAlert("Succès", "L'agence a été ajoutée avec succès !", Alert.AlertType.INFORMATION);


        clearFields();
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        name.clear();
        address.clear();
        phone.clear();
        email.clear();
        image.setImage(null);
        imageUrl = null;
    }




    @FXML
    private void chooseImage() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));


        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {

            imageUrl = selectedFile.toURI().toString();


            Image img = new Image(imageUrl);
            image.setImage(img);
        }
    }


}
