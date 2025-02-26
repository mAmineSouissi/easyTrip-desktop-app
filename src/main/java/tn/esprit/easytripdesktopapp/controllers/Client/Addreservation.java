package tn.esprit.easytripdesktopapp.controllers.Client;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;

public class Addreservation {
    @FXML
    private TextField mailres;

    @FXML
    private TextField nomres;

    @FXML
    private TextField phoneres;

    @FXML
    private TextField prenomres;

    private ServiceReservation sr = new ServiceReservation();

    UserSession session = UserSession.getInstance();

    @FXML
    public void initialize() {
        if (session.getUser() != null) {
            nomres.setText(session.getUser().getName());
            prenomres.setText(session.getUser().getSurname());
            phoneres.setText(session.getUser().getPhone());
            mailres.setText(session.getUser().getEmail());
        }
    }

    @FXML
    void ajouterR(ActionEvent event) {
        System.out.println("Name:" + session.getUser().getName());
        try {
           if (!allFieldsFilled()) {
                afficherErreur("Veuillez remplir tous les champs obligatoires.");
                return;
            }
            if (!isValidPhoneNumber(phoneres.getText())) {
                afficherErreur("Veuillez saisir un numéro de téléphone valide.");
                return;
            }
            if (!isValidEmail(mailres.getText())) {
                afficherErreur("Veuillez saisir un E-mail valide.");
                return;
            }

            Reservation r = new Reservation();
            r.setUserId(session.getUser().getId());
            r.setNom(nomres.getText());
            r.setPrenom(prenomres.getText());
            r.setPhone(Integer.parseInt(phoneres.getText()));
            r.setEmail(mailres.getText());
            sr.add(r);
            System.out.println("Réservation ajoutée avec succès");
        } catch (NumberFormatException e) {
            System.out.println("Le téléphone doit être un nombre valide.");
        }
    }

    private boolean allFieldsFilled() {
        return !nomres.getText().isEmpty() &&
                !prenomres.getText().isEmpty() &&
                !phoneres.getText().isEmpty() &&
                !mailres.getText().isEmpty();
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{8}");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void naviger(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Reservation/listreservation.fxml"));
            phoneres.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void reset(ActionEvent event) {
        mailres.clear();
        nomres.clear();
        phoneres.clear();
        prenomres.clear();
    }


}
