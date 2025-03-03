
package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class afficher_agence implements Initializable {

    @FXML
    private FlowPane cardContainer;
    @FXML
    private ComboBox<String> agenceComboBox; // Remplace les checkboxes
    @FXML
    private TextField searchField;

    private final CRUDService<Agence> agenceService = new ServiceAgence();
    private List<Agence> allAgences;

    UserSession session=UserSession.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();

        // Écouteur pour la recherche dynamique
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        // Écouteur pour filtrer les agences selon la sélection
        agenceComboBox.setOnAction(event -> filterAgences());
    }

    private void loadData() {
        allAgences = agenceService.getAll();
        loadComboBox();
        loadAgences(allAgences);
    }

    private void loadComboBox() {
        agenceComboBox.getItems().clear();
        agenceComboBox.getItems().add("Toutes les agences"); // Option pour afficher tout
        for (Agence agence : allAgences) {
            agenceComboBox.getItems().add(agence.getNom());
        }
        agenceComboBox.getSelectionModel().selectFirst();
    }

    private void loadAgences(List<Agence> agences) {
        cardContainer.getChildren().clear();
        for (Agence agence : agences) {
            VBox card = createAgenceCard(agence);
            cardContainer.getChildren().add(card);
        }
    }

    private VBox createAgenceCard(Agence agence) {
        VBox card = new VBox();
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 1);");

        // ImageView pour l'image de l'agence
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200); // Largeur fixe ou utilisez -fx-max-width dans CSS
        imageView.setFitHeight(150); // Hauteur fixe ou utilisez -fx-max-height dans CSS
        imageView.setPreserveRatio(false); // Désactiver le ratio pour remplir le conteneur

        if (agence.getImage() != null && !agence.getImage().isEmpty()) {
            try {
                Image img = new Image(agence.getImage());
                imageView.setImage(img);
            } catch (Exception e) {
                imageView.setImage(new Image("file:src/main/resources/images/default_agence.png"));
            }
        } else {
            imageView.setImage(new Image("file:src/main/resources/images/default_agence.png"));
        }

        // Nom de l'agence
        Text nomAgence = new Text(agence.getNom());
        nomAgence.getStyleClass().add("card-title");
        nomAgence.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Bouton Détails
        Button btnDetails = new Button("Détails");
        btnDetails.getStyleClass().add("button-details"); // Appliquer le style Détails
        btnDetails.setOnAction(event -> showDetails(agence));

        // Bouton Modifier
        Button btnModifier = new Button("Modifier");
        btnModifier.getStyleClass().add("button-modifier"); // Appliquer le style Modifier
        btnModifier.setOnAction(event -> openUpdateAgence(agence));

        // Bouton Supprimer
        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.getStyleClass().add("button-supprimer"); // Appliquer le style Supprimer
        btnSupprimer.setOnAction(event -> confirmDelete(agence));

        // HBox pour les boutons
        HBox buttonBox = new HBox(10, btnDetails, btnModifier, btnSupprimer);
        buttonBox.setAlignment(Pos.CENTER);

        // Ajouter les éléments à la carte
        card.getChildren().addAll(imageView, nomAgence, buttonBox);
        return card;
    }

    private void openUpdateAgence(Agence agence) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/update_agence.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            update_agence controller = loader.getController();
            controller.setAgence(agence);

            // Mise à jour dynamique après modification
            stage.setOnHiding(event -> loadData());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmDelete(Agence agence) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setContentText("Voulez-vous vraiment supprimer l'agence " + agence.getNom() + " ?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteAgence(agence);
            }
        });
    }

    private void deleteAgence(Agence agence) {
        agenceService.delete(agence);
        loadData(); // Mettre à jour les agences et la liste déroulante
    }

    @FXML
    public void openAddAgence(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/ajouter_agence.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Mise à jour dynamique après ajout
            stage.setOnHiding(event -> loadData());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void filterAgences() {
        String selectedAgence = agenceComboBox.getValue();

        if (selectedAgence == null || selectedAgence.equals("Toutes les agences")) {
            loadAgences(allAgences);
            return;
        }

        List<Agence> filteredAgences = allAgences.stream()
                .filter(a -> a.getNom().equals(selectedAgence))
                .toList();

        loadAgences(filteredAgences);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        List<Agence> filteredAgences = allAgences.stream()
                .filter(a -> a.getNom().toLowerCase().contains(keyword))
                .toList();
        loadAgences(filteredAgences);
    }

    public void navigateBack(ActionEvent actionEvent) {
        Stage stage;
        try {
            ResourceBundle resourcesBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/TablesAdmin.fxml"), resourcesBundle);
            Parent root = loader.load();
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(Agence agence) {
        Alert detailAlert = new Alert(Alert.AlertType.INFORMATION);
        detailAlert.setTitle("Détails de l'agence");
        detailAlert.setHeaderText(null);

        // Créer un contenu détaillé
        String content = "Nom: " + agence.getNom() + "\n"
                + "Adresse: " + agence.getAddress() + "\n"
                + "Téléphone: " + agence.getPhone() + "\n"
                + "Email: " + agence.getEmail();

        detailAlert.setContentText(content);
        detailAlert.showAndWait();
    }
}
