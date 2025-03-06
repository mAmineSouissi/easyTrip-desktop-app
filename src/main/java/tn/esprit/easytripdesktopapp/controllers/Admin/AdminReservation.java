package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import tn.esprit.models.Res_Transport;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tn.esprit.models.cars;
import tn.esprit.services.CarsService;
import tn.esprit.services.ResTransportService;
import tn.esprit.test.MainFx;
import tn.esprit.test.MyListener;
import tn.esprit.test.MyListener1;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AdminReservation implements Initializable {
    @FXML
    private VBox carListContainer;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;
    @FXML
    private ComboBox<Res_Transport.Status> statusComboBox;
    private int resId;
    private List<Res_Transport> ResList = new ArrayList<>();
    private ResTransportService resTransportService = new ResTransportService();
    private Res_Transport.Status status1;
    private int carid;
    private Date startDate;
    private Date endDate;
    private Res_Transport res_transport = new Res_Transport();
    private List<Res_Transport> getData() {
        List<Res_Transport> ResList = new ArrayList<>();
        try {
            ResList = resTransportService.getAllResTransport(); // Assurez-vous que cette méthode renvoie une liste de voitures depuis la base de données
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResList;
    }
    public void refresh(){
        grid.getChildren().clear();
        statusComboBox.getItems().addAll(Res_Transport.Status.values());
        ResList = getData(); // Récupérer les données correctement

        if (ResList.isEmpty()) {
            System.out.println("Aucune voiture trouvée !");
        }

        // Set spacing between grid cells
        grid.setHgap(20); // Horizontal gap between cards
        grid.setVgap(20); // Vertical gap between cards

        int column = 0;
        int row = 1;
        try {
            for (Res_Transport res_transport : ResList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminResCard.fxml"));
                AnchorPane anchorPane = loader.load();

                AdminResCard resController = loader.getController();
                resController.setResAdminData(res_transport);

                if (column == 3) { // Afficher les voitures en 3 colonnes
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
                grid.setPadding(new Insets(10));
                grid.setAlignment(Pos.CENTER);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                // Set margin around each card
                GridPane.setMargin(anchorPane, new Insets(30));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        statusComboBox.getItems().addAll(Res_Transport.Status.values());
        ResList = getData(); // Récupérer les données correctement

        if (ResList.isEmpty()) {
            System.out.println("Aucune voiture trouvée !");
        }

        // Set spacing between grid cells
        grid.setHgap(20); // Horizontal gap between cards
        grid.setVgap(20); // Vertical gap between cards

        int column = 0;
        int row = 1;
        try {
            for (Res_Transport res_transport : ResList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminResCard.fxml"));
                AnchorPane anchorPane = loader.load();

                AdminResCard resController = loader.getController();
                resController.setResAdminData(res_transport);

                if (column == 3) { // Afficher les voitures en 3 colonnes
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
                grid.setPadding(new Insets(10));
                grid.setAlignment(Pos.CENTER);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                // Set margin around each card
                GridPane.setMargin(anchorPane, new Insets(30));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void BackHome(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Get the UpdateResTransport controlle
        // Show the UpdateResTransport page
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void setReservationData(Res_Transport res_transport) {
        this.resId = res_transport.getId();
        this.carid = res_transport.getCarId();
        this.startDate = res_transport.getStartDate();
        this.endDate = res_transport.getEndDate();

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

            status1 = statusComboBox.getValue();

            Res_Transport UpdatedRes = new Res_Transport(resId,user_id,carid,startDate,endDate,status1);
            resTransportService.UpdateResTransport(UpdatedRes);

            showAlert("Success", "Reservation Status Updated successfully!");

            refresh();

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter numbers for seats and price.");
        }

    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
