package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.services.ServiceOfferTravel;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class chart implements Initializable {

    @FXML
    private BarChart<String, Number> offerChart;

    private final ServiceOfferTravel offerTravelService = new ServiceOfferTravel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Charger les données initiales du graphique
        loadOfferChart();
    }

    @FXML
    private void refreshChart() {
        // Actualisation manuelle du graphique
        loadOfferChart();
    }

    private void loadOfferChart() {
        // Obtenir les données (nombre d'offres par agence)
        Map<Agence, Integer> offerCountByAgency = offerTravelService.getOfferCountByAgency();

        // Effacer les données existantes du graphique
        offerChart.getData().clear();

        // Créer une nouvelle série de données pour le graphique
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Offres de voyage");

        // Ajouter les données au graphique
        for (Map.Entry<Agence, Integer> entry : offerCountByAgency.entrySet()) {
            Agence agence = entry.getKey();
            Integer offerCount = entry.getValue();
            series.getData().add(new XYChart.Data<>(agence.getNom(), offerCount));
        }

        // Ajouter la série au graphique
        offerChart.getData().add(series);
    }
}