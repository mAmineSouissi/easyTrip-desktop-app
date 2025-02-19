package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AffichageHotel {

    @FXML
    private ListView<String> listViewHotels;  // Déclaration du ListView
    private final CRUDService<Hotel> hotelService = new ServiceHotel();  // Service pour gérer les hôtels
    private List<Hotel> hotels;  // Liste des hôtels pour garder une référence

    @FXML
    public void initialize() {
        loadHotels();  // Charger les hôtels à l'initialisation
    }

    // Méthode pour charger les hôtels dans la ListView
    @FXML
    private void loadHotels() {
        listViewHotels.getItems().clear();  // Vider la ListView avant de charger les nouveaux éléments
        hotels = hotelService.getAll();  // Récupérer la liste des hôtels

        for (Hotel hotel : hotels) {
            // Formater les informations de l'hôtel pour afficher toutes les colonnes
            String hotelDisplay = String.format(
                    "Nom: %s\nAdresse: %s\nVille: %s\nNote: %d\nDescription: %s\nPrix: %.2f\nType de chambre: %s\nNombre de chambres: %d\nImage: %s\n",
                    hotel.getName(),
                    hotel.getAdresse(),
                    hotel.getCity(),
                    hotel.getRating(),
                    hotel.getDescription(),
                    hotel.getPrice(),
                    hotel.getTypeRoom(),
                    hotel.getNumRoom(),
                    hotel.getImage()
            );
            listViewHotels.getItems().add(hotelDisplay);  // Ajouter dans la ListView
        }
    }

    // Méthode pour naviguer vers l'interface Ajouter un Hôtel
    @FXML
    private void goToAddHotel() {
        try {
            // Charger le fichier FXML de l'interface AjouterHotel
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterHotel.fxml"));
            AnchorPane root = loader.load();  // Charger en tant qu'AnchorPane

            // Créer une nouvelle scène avec le nœud racine
            Scene scene = new Scene(root, 600, 400);

            // Créer une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Hôtel");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface Ajouter un Hôtel.");
        }
    }

    // Méthode pour mettre à jour un hôtel
    @FXML
    private void updateHotel() {
        // Récupérer l'index de l'hôtel sélectionné dans la ListView
        int selectedIndex = listViewHotels.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Récupérer l'hôtel sélectionné
            Hotel selectedHotel = hotels.get(selectedIndex);

            // Ouvrir une nouvelle fenêtre pour mettre à jour l'hôtel
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierHotel.fxml"));
                AnchorPane root = loader.load();  // Charger en tant qu'AnchorPane

                // Passer l'hôtel sélectionné au contrôleur de la fenêtre de modification
                ModifierHotel modifierController = loader.getController();
                modifierController.setHotel(selectedHotel);

                Stage stage = new Stage();
                stage.setTitle("Modifier un Hôtel");
                stage.setScene(new Scene(root, 600, 400));
                stage.showAndWait();  // Attendre que la fenêtre de modification soit fermée

                // Recharger la liste des hôtels après la mise à jour
                loadHotels();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors du chargement de l'interface de modification.");
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un hôtel à mettre à jour.");
        }
    }

    // Méthode pour supprimer un hôtel
    @FXML
    private void deleteHotel() {
        int selectedIndex = listViewHotels.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Récupérer l'hôtel sélectionné
            Hotel selectedHotel = hotels.get(selectedIndex);

            // Demander à l'utilisateur s'il souhaite vraiment supprimer l'hôtel
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet hôtel ?");
            alert.setContentText("Hôtel sélectionné : " + selectedHotel.getName());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer l'hôtel
                hotelService.delete(selectedHotel);
                loadHotels();  // Recharger la liste des hôtels après suppression
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un hôtel à supprimer.");
        }
    }

    // Méthode pour afficher un message d'alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}