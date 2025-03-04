package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;

import java.io.IOException;
import java.util.Map;

public class Reservationstatics {

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private PieChart pieChart;

    @FXML
    void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        ServiceReservation sr = new ServiceReservation();
        Map<String, Integer> data = sr.getReservationsByDate();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Réservations par date");
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().clear();
        barChart.getData().add(series);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        pieChart.setData(pieChartData);
    }
    @FXML
    private Button btnRetour; // Assure-toi d'avoir lié ce bouton dans ton FXML

    @FXML
    void retour() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Reservation/listreservation.fxml"));
            btnRetour.getScene().setRoot(root); // Utilise btnRetour pour récupérer la scène
        } catch (IOException e) {
            e.printStackTrace(); // Affiche l'erreur en détail
        }
    }

}
