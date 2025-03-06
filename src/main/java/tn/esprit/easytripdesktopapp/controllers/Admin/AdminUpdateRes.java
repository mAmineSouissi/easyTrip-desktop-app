package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import tn.esprit.easytripdesktopapp.models.Res_Transport;
import tn.esprit.easytripdesktopapp.models.cars;
import tn.esprit.easytripdesktopapp.services.ResTransportService;

import java.time.LocalDate;
import java.util.Date;


public class AdminUpdateRes {
    @FXML
    private ComboBox<Res_Transport.Status> statusComboBox;
    private int resId;
    private Res_Transport res_transport;
    private ResTransportService resTransportService = new ResTransportService();
    private int carid;
    private Date startDate;
    private Date endDate;
    @FXML
    public void initialize() {
        statusComboBox.getItems().addAll(Res_Transport.Status.values());

    }
    public void setReservationData(Res_Transport res_transport) {
        this.resId = res_transport.getId();
        this.carid = res_transport.getCarId();
        statusComboBox.setValue(res_transport.getStatus());
    }
    @FXML
    void update(ActionEvent event) {
        if (res_transport == null) {
            System.out.println("Error: res_transport is null!");
            return;
        }

        try {
            int user_id = 1;
            this.startDate = res_transport.getStartDate();
            this.endDate = res_transport.getEndDate();
            Res_Transport.Status status1 = res_transport.getStatus();

            Res_Transport UpdatedRes = new Res_Transport(resId, user_id, carid, startDate, endDate, status1);
            resTransportService.UpdateResTransport(UpdatedRes);

            System.out.println("Status updated successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter numbers for seats and price.");
        }
    }
}
