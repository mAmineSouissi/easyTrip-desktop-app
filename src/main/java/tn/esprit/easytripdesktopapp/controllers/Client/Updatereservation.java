package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;

import java.io.IOException;

public class Updatereservation {

    @FXML
    private TextField mailres;

    @FXML
    private TextField nomres;

    @FXML
    private TextField phoneres;

    @FXML
    private TextField prenomres;

    @FXML
    private Spinner<Integer> nbrepersonne;

    private Reservation currentReservation;


    public void initData(Reservation reservation) {
        this.currentReservation = reservation;
        if (reservation != null) {
            mailres.setText(reservation.getEmail());
            nomres.setText(reservation.getNom());
            phoneres.setText(String.valueOf(reservation.getPhone()));
            prenomres.setText(reservation.getPrenom());


            SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);  // Min = 1, Max = 100
            nbrepersonne.setValueFactory(valueFactory);

            nbrepersonne.getValueFactory().setValue(reservation.getPlaces());
        }
    }


    @FXML
    void modifier(ActionEvent event) {
        if (currentReservation != null) {
            currentReservation.setNom(nomres.getText());
            currentReservation.setPrenom(prenomres.getText());
            currentReservation.setEmail(mailres.getText());
            currentReservation.setPhone(Integer.parseInt(phoneres.getText()));
            currentReservation.setPlaces(nbrepersonne.getValue());


            ServiceReservation serviceReservation = new ServiceReservation();
            serviceReservation.update(currentReservation);

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Reservation/listreservation.fxml"));
                mailres.getScene().setRoot(root);
            } catch (IOException e) {
                System.out.println("Erreur lors du retour à la liste : " + e.getMessage());
            }
        }
    }
}
