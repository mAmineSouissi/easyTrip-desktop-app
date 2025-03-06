package tn.esprit.controllers;

import com.mysql.cj.conf.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.esprit.models.cars;
import tn.esprit.test.MainFx;
import tn.esprit.test.MyListener;

import java.awt.event.MouseEvent;
import java.io.File;

public class CarController {
    @FXML
    private Label LocationLabel;

    @FXML
    private Label ModelLabel;

    @FXML
    private Label PriceLabel;

    @FXML
    private Label SeatsLabel;
    @FXML
    private ImageView img;
    @FXML
    private ImageView notavaible;

    private cars car;
    private MyListener myListener;



    public void setCarData(cars car, MyListener myListener) {
        this.car = car;
        this.myListener = myListener;
        LocationLabel.setText(car.getLocation());
        ModelLabel.setText(car.getModel());
        PriceLabel.setText(MainFx.CURRENCY + car.getPrice());
        SeatsLabel.setText(Integer.toString(car.getSeats()));
        if (car.getStatus() == cars.availability.NOT_AVAILABLE) {
            notavaible.setVisible(true);
        }else
        {
            notavaible.setVisible(false);
        }
        // Handle image loading
        if (car.getImage() == null || car.getImage().isEmpty()) {
            System.out.println("Image non trouvée pour la voiture : " + car.getModel());
        } else {
            try {
                File file = new File(car.getImage());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    img.setImage(image);
                } else {
                    System.out.println("Fichier image introuvable : " + car.getImage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erreur lors du chargement de l'image : " + car.getImage());
            }
        }
    }


    @FXML
    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        System.out.println("Card clicked: " + car.getModel()); // Debug statement
        myListener.onClickListener(car);
    }
}


