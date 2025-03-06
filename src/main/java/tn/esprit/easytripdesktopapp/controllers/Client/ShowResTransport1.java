package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.MyListener1;
import tn.esprit.easytripdesktopapp.models.Res_Transport;
import tn.esprit.easytripdesktopapp.services.CarsService;
import tn.esprit.easytripdesktopapp.services.ResTransportService;
import tn.esprit.easytripdesktopapp.models.Res_Transport;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tn.esprit.easytripdesktopapp.models.cars;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class ShowResTransport1 implements Initializable {


    @FXML
    private VBox carListContainer;

    @FXML
    private VBox chosenCar;

    @FXML
    private Label datedeblabel;

    @FXML
    private Label datefinlabel;

    @FXML
    private GridPane grid;

    @FXML
    private Label sommelabel;


    @FXML
    private ScrollPane scroll;

    @FXML
    private Label statuelabel;
    private MyListener1 myListener1;
    private Res_Transport selectedTransport;

    private List<Res_Transport> ResList = new ArrayList<>();
    private ResTransportService resTransportService = new ResTransportService();

    private List<Res_Transport> getData() {
        List<Res_Transport> ResList = new ArrayList<>();
        try {
            ResList = resTransportService.getAllResTransport(); // Assurez-vous que cette méthode renvoie une liste de voitures depuis la base de données
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResList;
    }
    private void setChosenRes( Res_Transport res_transport) {
        selectedTransport = res_transport;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String startDateFormatted = dateFormatter.format(res_transport.getStartDate());
        String endDateFormatted = dateFormatter.format(res_transport.getEndDate());
        datedeblabel.setText(startDateFormatted);
        datefinlabel.setText(endDateFormatted);
        statuelabel.setText(res_transport.getStatus().toString());

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResList = getData(); // Récupérer les données correctement
        if(ResList.size() > 0){
            setChosenRes(ResList.get(0));
            myListener1 =new MyListener1() {
                @Override
                public void onClickListener(Res_Transport res) {
                    setChosenRes(res);
                }
            };
        }
        if (ResList.isEmpty()) {
            System.out.println("Aucune voiture trouvée !");
        }

        // Set spacing between grid cells
        grid.setHgap(20); // Horizontal gap between cards
        grid.setVgap(70); // Vertical gap between cards

        int column = 0;
        int row = 1;
        try {
            for (Res_Transport res_transport : ResList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/ResTransport_Card.fxml"));
                AnchorPane anchorPane = loader.load();

                ResTransportCard resController = loader.getController();
                resController.setResData(res_transport,myListener1);

                if (column == 1) { // Afficher les voitures en 3 colonnes
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
                GridPane.setMargin(anchorPane, new Insets(40));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void refreshCarCards() {
        // Clear the existing cards from the GridPane
        grid.getChildren().clear();

        ResList = getData(); // Récupérer les données correctement
        if(ResList.size() > 0){
            setChosenRes(ResList.get(0));
            myListener1 =new MyListener1() {
                @Override
                public void onClickListener(Res_Transport res) {
                    setChosenRes(res);
                }
            };
        }
        if (ResList.isEmpty()) {
            System.out.println("Aucune voiture trouvée !");
        }

        // Set spacing between grid cells
        grid.setHgap(20); // Horizontal gap between cards
        grid.setVgap(70); // Vertical gap between cards

        int column = 0;
        int row = 1;
        try {
            for (Res_Transport res_transport : ResList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/ResTransport_Card.fxml"));
                AnchorPane anchorPane = loader.load();

                ResTransportCard resController = loader.getController();
                resController.setResData(res_transport,myListener1);

                if (column == 1) { // Afficher les voitures en 3 colonnes
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
                GridPane.setMargin(anchorPane, new Insets(40));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void DeleteSelectedReservation(MouseEvent event) {
        // Check if a reservation is selected
        if (selectedTransport == null) {
            showAlert("Error", "Please select a reservation to delete.");
            return;
        }

        // Confirm deletion with the user
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this reservation?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the selected reservation
            resTransportService.DeleteResTransport(selectedTransport.getId());

            // Reload the reservations
            refreshCarCards();



            // Show success message
            showAlert("Success", "Reservation deleted successfully!");
        }
    }

    @FXML
    void SommeSelectedReservation(MouseEvent event) {
        if (selectedTransport == null) {
            sommelabel.setText("Sélectionnez une réservation !");
            return;
        }

        CarsService carsService = new CarsService();
        cars car = carsService.getCarById(selectedTransport.getCarId());

        if (car != null) {
            double pricePerDay = car.getPrice(); // Prix par jour de la voiture
            long diffInMillis = selectedTransport.getEndDate().getTime() - selectedTransport.getStartDate().getTime();
            System.out.println(diffInMillis);
            long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
            if (days < 1) days = 1; // Ensure at least one day


            double total = pricePerDay * days;

            // Afficher le résultat dans le label
            sommelabel.setText(String.format("%.2f DT", total));
        } else {
            sommelabel.setText("Voiture introuvable !");
        }
    }

    @FXML
    void UpdateSelectedReservation(MouseEvent event) {
        // Check if a reservation is selected
        if (selectedTransport == null) {
            showAlert("Error", "Please select a reservation to update.");
            return;
        }

        // Load the UpdateResTransport FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/UpdateResTransport.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Get the UpdateResTransport controller
        UpdateResTransport controller = loader.getController();

        // Pass the selected reservation data to the UpdateResTransport controller
        controller.setResTransportData(selectedTransport);
        System.out.println(selectedTransport);

        // Show the UpdateResTransport page
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }
    @FXML
    void BackToShow(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/ShowCar1.fxml"));
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
}
