package tn.esprit.easytripdesktopapp.controllers.Agent.Travel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.services.ServiceOfferTravel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherOfferTravelController implements Initializable {

    @FXML
    private FlowPane cardContainer;
    @FXML
    private ComboBox<String> offerComboBox; // Liste déroulante pour filtrer par destination
    @FXML
    private TextField searchField;

    private final CRUDService<OfferTravel> offerService = new ServiceOfferTravel();
    private List<OfferTravel> allOffers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        offerComboBox.setOnAction(event -> filterOffers());
    }

    private void loadData() {
        allOffers = offerService.getAll();
        loadComboBox();
        loadOffers(allOffers);
    }

    private void loadComboBox() {
        offerComboBox.getItems().clear();
        offerComboBox.getItems().add("Toutes les destinations"); // Option pour afficher tout
        for (OfferTravel offer : allOffers) {
            offerComboBox.getItems().add(offer.getDestination());
        }
        offerComboBox.getSelectionModel().selectFirst();
    }

    private void loadOffers(List<OfferTravel> offers) {
        cardContainer.getChildren().clear();
        for (OfferTravel offer : offers) {
            VBox card = createOfferCard(offer);
            cardContainer.getChildren().add(card);
        }
    }

    private VBox createOfferCard(OfferTravel offer) {
        VBox card = new VBox();
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 1);");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        String imagePath = offer.getImage();
        File file = (imagePath != null) ? new File(imagePath) : null;

        if (file != null && file.exists()) {
            imageView.setImage(new Image(file.toURI().toString()));
        } else {
            imageView.setImage(new Image("file:src/main/resources/images/default_offer.png"));
        }

        Text destinationText = new Text("Destination : " + offer.getDestination());
        destinationText.getStyleClass().add("card-title");
        destinationText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Text priceText = new Text("Prix : " + offer.getPrice() + " DT");
        priceText.setStyle("-fx-font-size: 14px;");

        Button btnModifier = new Button("Modifier");
        btnModifier.getStyleClass().add("button-modifier");
        btnModifier.setOnAction(event -> openUpdateOffer(offer));

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.getStyleClass().add("button-supprimer");
        btnSupprimer.setOnAction(event -> confirmDelete(offer));

        HBox buttonBox = new HBox(10, btnModifier, btnSupprimer);
        buttonBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(imageView, destinationText, priceText, buttonBox);
        return card;
    }

    private void openUpdateOffer(OfferTravel offer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/Travel/update_offer_travel.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            UpdateOfferTravelController controller = loader.getController();
            controller.setSelectedOffer(offer);

            stage.setOnHiding(event -> loadData()); // Rafraîchir après modification

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmDelete(OfferTravel offer) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setContentText("Voulez-vous vraiment supprimer l'offre pour " + offer.getDestination() + " ?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteOffer(offer);
            }
        });
    }

    private void deleteOffer(OfferTravel offer) {
        offerService.delete(offer);
        loadData();
    }

    @FXML
    public void openAddOffer(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Agence/Travel/ajouter_offer_travel.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            stage.setOnHiding(event -> loadData()); // Rafraîchir après ajout

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void filterOffers() {
        String selectedDestination = offerComboBox.getValue();

        if (selectedDestination == null || selectedDestination.equals("Toutes les destinations")) {
            loadOffers(allOffers);
            return;
        }

        List<OfferTravel> filteredOffers = allOffers.stream()
                .filter(o -> o.getDestination().equals(selectedDestination))
                .toList();

        loadOffers(filteredOffers);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        List<OfferTravel> filteredOffers = allOffers.stream()
                .filter(o -> o.getDestination().toLowerCase().contains(keyword))
                .toList();
        loadOffers(filteredOffers);
    }
}
