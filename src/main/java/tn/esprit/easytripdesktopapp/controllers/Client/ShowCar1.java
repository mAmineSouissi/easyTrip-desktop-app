package tn.esprit.easytripdesktopapp.controllers.Client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import tn.esprit.easytripdesktopapp.controllers.Client.AddRes_Transport;
import tn.esprit.easytripdesktopapp.interfaces.MyListener;
import tn.esprit.easytripdesktopapp.services.CarsService;
import tn.esprit.easytripdesktopapp.models.Res_Transport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.cars;
import tn.esprit.easytripdesktopapp.services.CarsService;
import tn.esprit.easytripdesktopapp.controllers.Admin.UpdateCar;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import tn.esprit.easytripdesktopapp.models.cars;
import javafx.scene.control.RadioButton;


import java.util.*;

import javafx.scene.input.MouseEvent;

public class ShowCar1 implements Initializable {
    @FXML
    private ImageView carimage;

    @FXML
    private Label modellabel;

    @FXML
    private VBox chosenCar;

    @FXML
    private Button reservationave;

    @FXML
    private Label locationlabel;

    @FXML
    private Label pricelabel;

    @FXML
    private Label seatslabel;

    @FXML
    private ComboBox<String> currencyComboBox;

    @FXML
    private TextField keywordtextfield;

    @FXML
    private ImageView locationIcon;
    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;
    private cars selectedCar;
    @FXML
    private VBox carListContainer;
    @FXML
    private WebView mapView;

    private Image image;

    ObservableList<cars> carsObservableList = FXCollections.observableArrayList();

    private List<cars> carsList = new ArrayList<>();
    private final CurrencyConverter currencyConverter = new CurrencyConverter();

    private CarsService carsService = new CarsService();

    private MyListener myListener;
    private List<cars> getData() {
        List<cars> carsList = new ArrayList<>();
        try {
            carsList = carsService.getAllCars(); // Assurez-vous que cette méthode renvoie une liste de voitures depuis la base de données
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carsList;
    }






    private void setChosenCar( cars car) {
        selectedCar = car;
        modellabel.setText(car.getModel());
        //locationlabel.setText(car.getLocation());
        if (car.getStatus() == cars.availability.NOT_AVAILABLE) {
            //reservationave.setText("notAvaible");
            reservationave.setVisible(false);
        }else {
            reservationave.setVisible(true);
        }

        // Convertir le prix dans la devise sélectionnée
        String selectedCurrency = currencyComboBox.getValue();
        double convertedPrice;
        try {
            convertedPrice = currencyConverter.convert(selectedCar.getPrice(), "EUR", selectedCurrency); // Par défaut, EUR
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion du prix : " + e.getMessage());
            convertedPrice = selectedCar.getPrice(); // Utiliser le prix d'origine en cas d'erreur
        }

        refreshCarPrice();

        pricelabel.setText(String.valueOf(convertedPrice));


        seatslabel.setText(Integer.toString(car.getSeats()));
        if (car.getImage() == null || car.getImage().isEmpty()) {
            System.out.println("Image non trouvée pour la voiture : " + car.getModel());
        } else {
            try {
                File file = new File(car.getImage());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    carimage.setImage(image);
                } else {
                    System.out.println("Fichier image introuvable : " + car.getImage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erreur lors du chargement de l'image : " + car.getImage());
            }
        }
        chosenCar.setStyle(" -fx-background-color: #0e7ee0;\n" +
                "    -fx-background-radius: 30;");

    }



    // Refreshes the price label when the currency is changed
    private void refreshCarPrice() {
        String selectedCurrency = currencyComboBox.getValue();
        double convertedPrice;
        try {
            convertedPrice = currencyConverter.convert(selectedCar.getPrice(), "EUR", selectedCurrency); // Convert from EUR
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion du prix : " + e.getMessage());
            convertedPrice = selectedCar.getPrice(); // Use the original price if conversion fails
        }

        pricelabel.setText(String.valueOf(convertedPrice));
    }



    ResourceBundle bundel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        bundel = ResourceBundle.getBundle("i18n.messages", Locale.getDefault());



        // Initialiser la sélection de la devise
        currencyComboBox.getItems().addAll("EUR", "USD", "GBP", "JPY", "CAD"); // Ajoutez d'autres devises si nécessaire
        currencyComboBox.setValue("EUR"); // Par défaut, EUR


        currencyComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedCar != null && newValue != null) {
                refreshCarPrice();
            }
        });


        carsList = getData(); // Fetch data from the database
        carsObservableList.addAll(carsList);

        // Set up the FilteredList
        FilteredList<cars> filteredData = new FilteredList<>(carsObservableList, p -> true);

        // Bind the search text to the FilteredList
        keywordtextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(car -> {
                if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                    return true; // Show all cars if the search text is empty
                }

                String searchText = newValue.toLowerCase();

                // Check if the car's model or location matches the search text
                if (car.getModel() != null && car.getModel().toLowerCase().contains(searchText)) {
                    return true;
                } else if (car.getLocation() != null && car.getLocation().toLowerCase().contains(searchText)) {
                    return true;
                } else {
                    return false; // Hide the car if it doesn't match
                }
            });

            // Refresh the GridPane with the filtered data
            refreshCardContainer(filteredData);
        });

        // Initialize the GridPane with all cars
        refreshCardContainer(filteredData);
    }

    @FXML
    void GoAddResTransport(ActionEvent event) {
        // Vérifiez si une voiture est sélectionnée
        if (selectedCar == null) {
            showAlert("Error", "Please select a car to proceed.");
            return;
        }

        try {
            // Load the AddRes_Transport FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/AddRes_Transport.fxml"));
            Parent root = loader.load();

            // Get the AddRes_Transport controller
            AddRes_Transport controller = loader.getController();

            // Pass the selected car's ID to the AddRes_Transport controller
            controller.setCarId(selectedCar.getId());


            // Show the AddRes_Transport page
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println(selectedCar);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the reservation page.");
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
    void goToShowResTransport(javafx.scene.input.MouseEvent event) {
        try {
            // Charger le fichier FXML de la page ShowResTransport1
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/ShowResTransport1.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la page ShowResTransport1
            ShowResTransport1 showResTransport1Controller = loader.getController();

            // Passer des données si nécessaire (optionnel)
            // Par exemple, vous pouvez passer l'ID de la voiture sélectionnée


            // Créer une nouvelle scène et afficher la page
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle (optionnel)
            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page des réservations de transport.");
        }
    }

    @FXML
    void handleLocationClick(MouseEvent event) {
        openMapPage(event);
    }


    public void openMapPage(MouseEvent mouseEvent) {
        if (selectedCar != null) {
            try {
                // Load the map.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/MapPage.fxml"));
                Parent root = loader.load();

                // Get the MapController and pass the selected car's location
                MapController mapController = loader.getController();

                // Show the map page
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Maps");
                stage.show();
                mapController.setLocation(selectedCar.getLocation());

            } catch (Exception e) {
                System.err.println("Error loading map page: " + e.getMessage());
            }
        } else {
            System.out.println("No car selected.");
        }
    }

    private void refreshCardContainer(FilteredList<cars> filteredData) {
        grid.getChildren().clear();
//        bundel = ResourceBundle.getBundle("i18n.messages", Locale.getDefault());



        // Initialiser la sélection de la devise
        currencyComboBox.getItems().addAll("EUR", "USD", "GBP", "JPY", "CAD"); // Ajoutez d'autres devises si nécessaire
        currencyComboBox.setValue("EUR"); // Par défaut, EUR


        currencyComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedCar != null && newValue != null) {
                refreshCarPrice();
            }
        });


        carsList = getData(); // Récupérer les données correctement
        if(carsList.size() > 0){
            setChosenCar(carsList.get(0));
            myListener =new MyListener() {
                @Override
                public void onClickListener(cars car) {
                    setChosenCar(car);
                }
            };
        }
        if (carsList.isEmpty()) {
            System.out.println("Aucune voiture trouvée !");
        }

        // Set spacing between grid cells
        grid.setHgap(20); // Horizontal gap between cards
        grid.setVgap(75); // Vertical gap between cards

        int column = 0;
        int row = 1;
        try {
            for (cars car : carsList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/Car.fxml"));
                AnchorPane anchorPane = loader.load();

                CarController carController = loader.getController();
                carController.setCarData(car,myListener);

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
                GridPane.setMargin(anchorPane, new Insets(40));
            }
            FilteredList<cars> filteredeData = new FilteredList<>(carsObservableList, c -> true);

            keywordtextfield.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredeData.setPredicate(car -> {
                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true; // Show all cards if search text is empty
                    }

                    String searchText = newValue.toLowerCase();

                    // Check if the car's model or location matches the search text
                    if (car.getModel().toLowerCase().contains(searchText)) {
                        return true;
                    } else if (car.getLocation().toLowerCase().contains(searchText)) {
                        return true;
                    } else {
                        return false; // Hide the card if it doesn't match
                    }
                });

                // Refresh the card container with the filtered data
                refreshCardContainer(filteredData);
            });




        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void TriASEC(ActionEvent event) {
        System.out.println("Tri ASC déclenché");
        sortCarsByPriceAscending();
    }

    @FXML
    void TriDESC(ActionEvent event) {
        System.out.println("Tri DSC déclenché");
        sortCarsByPriceDescending();
    }
    private void sortCarsByPriceAscending() {
        carsList.sort(Comparator.comparingDouble(cars::getPrice));
        refreshCardContainer(new FilteredList<>(FXCollections.observableArrayList(carsList), p -> true));
    }

    private void sortCarsByPriceDescending() {
        carsList.sort(Comparator.comparingDouble(cars::getPrice).reversed());
        refreshCardContainer(new FilteredList<>(FXCollections.observableArrayList(carsList), p -> true));
    }

    public void goHome(ActionEvent actionEvent) {
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
}



