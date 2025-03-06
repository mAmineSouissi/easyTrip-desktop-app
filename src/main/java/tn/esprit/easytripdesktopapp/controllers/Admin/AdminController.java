package tn.esprit.controllers;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import tn.esprit.controllers.AddRes_Transport;
import tn.esprit.models.Res_Transport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.models.cars;
import tn.esprit.services.CarsService;
import tn.esprit.controllers.UpdateCar;
import tn.esprit.test.MainFx;
import tn.esprit.test.MyListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private VBox carListContainer;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    private CarsService carsService = new CarsService();
    private List<cars> carsList = new ArrayList<>();

    private List<cars> getData() {
        List<cars> carsList = new ArrayList<>();
        try {
            carsList = carsService.getAllCars(); // Assurez-vous que cette méthode renvoie une liste de voitures depuis la base de données
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carsList;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carsList = getData(); // Récupérer les données correctement

        if (carsList.isEmpty()) {
            System.out.println("Aucune voiture trouvée !");
        }

        // Set spacing between grid cells
        grid.setHgap(20); // Horizontal gap between cards
        grid.setVgap(60); // Vertical gap between cards

        int column = 0;
        int row = 1;
        try {
            for (cars car : carsList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminCarCard.fxml"));
                AnchorPane anchorPane = loader.load();

                AdminCarCard carController = loader.getController();
                carController.setCarAdminData(car);

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
    void GoToAddCar(MouseEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCar.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle (stage) à partir de l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène de la fenêtre actuelle
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page des réservations.");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void GoToReservation(MouseEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminReservation.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle (stage) à partir de l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène de la fenêtre actuelle
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page des réservations.");
        }
    }


}
