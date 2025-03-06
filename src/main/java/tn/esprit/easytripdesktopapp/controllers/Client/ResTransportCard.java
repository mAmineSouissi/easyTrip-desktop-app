package tn.esprit.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import tn.esprit.models.Res_Transport;
import tn.esprit.models.cars;
import tn.esprit.services.ResTransportService;
import tn.esprit.test.MyListener1;
import javafx.scene.input.MouseEvent;

import java.text.SimpleDateFormat;

public class ResTransportCard {
    @FXML
    private Label DateDebLabel;

    @FXML
    private Label DateFinLabel;

    @FXML
    private Label StatuesLabel;

    @FXML
    private ImageView img_annule;

    @FXML
    private ImageView img_done;

    @FXML
    private Label inprogress_label;



    private Res_Transport res_transport;
    private MyListener1 myListener1;

    public void setResData(Res_Transport res_transport, MyListener1 myListener1) {
        this.res_transport = res_transport;
        this.myListener1 = myListener1;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String startDateFormatted = dateFormatter.format(res_transport.getStartDate());
        String endDateFormatted = dateFormatter.format(res_transport.getEndDate());
        DateDebLabel.setText(startDateFormatted);
        DateFinLabel.setText(endDateFormatted);
        StatuesLabel.setText(res_transport.getStatus().toString());
        if (res_transport.getStatus() == Res_Transport.Status.IN_PROGRESS) {
            inprogress_label.setVisible(true);
            img_annule.setVisible(false);
            img_done.setVisible(false);
        }else if (res_transport.getStatus() == Res_Transport.Status.DONE)
        {
            inprogress_label.setVisible(false);
            img_annule.setVisible(false);
            img_done.setVisible(true);
        }else {
            inprogress_label.setVisible(false);
            img_annule.setVisible(true);
            img_done.setVisible(false);
        }
    }

    @FXML
    void click1(MouseEvent event) {
    //System.out.println("Reservation Clicked : " + res_transport);
    myListener1.onClickListener(res_transport);
    }
}
