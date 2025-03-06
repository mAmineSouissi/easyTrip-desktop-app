package tn.esprit.easytripdesktopapp.controllers.Client;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.*;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Addreservation {
    private final ServiceReservation sr = new ServiceReservation();
    UserSession session = UserSession.getInstance();
    @FXML
    private TextField mailres;
    @FXML
    private TextField nomres;
    @FXML
    private TextField phoneres;
    @FXML
    private TextField prenomres;
    private Hotel hotel;
    private Ticket ticket;
    private OfferTravel offer;

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setOffer(OfferTravel offer) {
        this.offer = offer;
    }

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
            r.setUser_id(session.getUser().getId());
            r.setNom(nomres.getText());
            r.setPrenom(prenomres.getText());
            r.setPhone(Integer.parseInt(phoneres.getText()));
            r.setEmail(mailres.getText());
            r.setHotel_id(hotel != null ? hotel.getId() : -1);
            r.setTravel_id(offer != null ? offer.getId() : -1);
            r.setTicket_id(ticket != null ? ticket.getIdTicket() : -1);
            r.setStatus("En attente");
            sr.add(r);
            System.out.println("Réservation ajoutée avec succès");
        } catch (NumberFormatException e) {
            System.out.println("Le téléphone doit être un nombre valide.");
        }
    }

    private boolean allFieldsFilled() {
        return !nomres.getText().isEmpty() && !prenomres.getText().isEmpty() && !phoneres.getText().isEmpty() && !mailres.getText().isEmpty();
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
    void reset(ActionEvent event) {
        mailres.clear();
        nomres.clear();
        phoneres.clear();
        prenomres.clear();
    }

    @FXML
    void retour(ActionEvent actionEvent) {
        Stage stage;
        try {
            ResourceBundle resourcesBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/Dashboard.fxml"), resourcesBundle);
            Parent root = loader.load();
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface d'accueil.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
