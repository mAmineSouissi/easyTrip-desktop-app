package tn.esprit.easytripdesktopapp.controllers.Agence;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class afficher_agence implements Initializable {

    @FXML
    private FlowPane cardContainer;
    @FXML
    private VBox checkboxContainer;
    @FXML
    private TextField searchField;

    private final CRUDService<Agence> agenceService = new ServiceAgence();
    private List<CheckBox> checkBoxes = new ArrayList<>();
    private List<Agence> allAgences;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();

        // Écouteur pour la recherche dynamique
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
    }

    private void loadData() {
        allAgences = agenceService.getAll();
        loadCheckBoxes();
        loadAgences(allAgences);
    }

    private void loadCheckBoxes() {
        checkboxContainer.getChildren().clear();
        checkBoxes.clear();

        for (Agence agence : allAgences) {
            CheckBox checkBox = new CheckBox(agence.getNom());
            checkBox.setOnAction(event -> filterAgences());
            checkBoxes.add(checkBox);
            checkboxContainer.getChildren().add(checkBox);
        }
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

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

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

        Text nomAgence = new Text(agence.getNom());
        nomAgence.getStyleClass().add("card-title");
        nomAgence.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button btnModifier = new Button("Modifier");
        btnModifier.setOnAction(event -> openUpdateAgence(agence));

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setOnAction(event -> confirmDelete(agence));

        HBox buttonBox = new HBox(10, btnModifier, btnSupprimer);
        buttonBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(imageView, nomAgence, buttonBox);
        return card;
    }

    private void openUpdateAgence(Agence agence) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/update_agence.fxml"));
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
        loadData(); // Mettre à jour les agences et les checkboxes
    }

    @FXML
    public void openAddAgence(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/ajouter_agence.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Mise à jour dynamique après ajout
            stage.setOnHiding(event -> loadData());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterAgences() {
        List<Agence> filteredAgences = new ArrayList<>();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                Agence agence = getAgenceFromCheckBox(checkBox);
                if (agence != null) {
                    filteredAgences.add(agence);
                }
            }
        }
        if (filteredAgences.isEmpty()) {
            filteredAgences.addAll(allAgences);
        }
        loadAgences(filteredAgences);
    }

    private Agence getAgenceFromCheckBox(CheckBox checkBox) {
        return allAgences.stream().filter(a -> a.getNom().equals(checkBox.getText())).findFirst().orElse(null);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        List<Agence> filteredAgences = allAgences.stream()
                .filter(a -> a.getNom().toLowerCase().contains(keyword))
                .toList();
        loadAgences(filteredAgences);
    }
}
