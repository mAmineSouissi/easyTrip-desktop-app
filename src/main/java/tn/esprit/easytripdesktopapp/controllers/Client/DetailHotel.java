package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Feedback;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.services.ServiceFeedback;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;
import java.util.List;

public class DetailHotel {

    @FXML
    private VBox detailContainer;

    @FXML
    private ImageView hotelImage;

    @FXML
    private Label nameLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label typeRoomLabel;

    @FXML
    private Label numRoomLabel;

    @FXML
    private Label promotionLabel;

    @FXML
    private Label averageRatingLabel; // Nouveau Label pour la note moyenne

    @FXML
    private VBox ratingDistributionBox; // Nouveau VBox pour la distribution des étoiles

    private Hotel hotel;

    UserSession session = UserSession.getInstance();

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        loadHotelDetails();
    }

    private void loadHotelDetails() {
        if (hotel != null) {
            hotelImage.setImage(new Image(hotel.getImage()));
            nameLabel.setText("Nom: " + hotel.getName());
            addressLabel.setText("Adresse: " + hotel.getAdresse());
            cityLabel.setText("Ville: " + hotel.getCity());
            ratingLabel.setText("Note: " + hotel.getRating());
            priceLabel.setText("Prix: " + hotel.getPrice() + " €");
            descriptionLabel.setText("Description: " + hotel.getDescription());
            typeRoomLabel.setText("Type de chambre: " + hotel.getTypeRoom());
            numRoomLabel.setText("Nombre de chambres: " + hotel.getNumRoom());

            if (hotel.getPromotion() != null) {
                promotionLabel.setText("Promotion: " + hotel.getPromotion().getDiscount_percentage() + "% de réduction");
            } else {
                promotionLabel.setText("Promotion: Aucune promotion");
            }


            List<Feedback> feedbacks = getFeedbacksForHotel(hotel.getId());
            double averageRating = hotel.getAverageRating(feedbacks);
            int[] ratingDistribution = hotel.getRatingDistribution(feedbacks);

            averageRatingLabel.setText(String.format("Note moyenne: %.1f/5", averageRating));

            ratingDistributionBox.getChildren().clear();
            for (int i = 5; i >= 1; i--) {
                HBox ratingRow = new HBox(10);
                ratingRow.setAlignment(Pos.CENTER_LEFT);

                Label starsLabel = new Label("★".repeat(i) + "☆".repeat(5 - i));
                starsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #FFD700;");

                Label countLabel = new Label(String.valueOf(ratingDistribution[i - 1]));
                countLabel.setStyle("-fx-font-size: 14px;");

                ratingRow.getChildren().addAll(starsLabel, countLabel);
                ratingDistributionBox.getChildren().add(ratingRow);
            }
        }
    }

    private List<Feedback> getFeedbacksForHotel(int hotelId) {
        ServiceFeedback feedbackService = new ServiceFeedback();
        return feedbackService.getFeedbacksByOfferId(hotelId);
    }

    @FXML
    private void handleReserveButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Reservation/addreservation.fxml"));
            Parent root = loader.load();
            Addreservation detailController = loader.getController();
            detailController.setHotel(hotel);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reservation Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFeedbackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/FeedbackUser.fxml"));
            Parent root = loader.load();

            FeedbackUser detailController = loader.getController();
            detailController.setHotel(hotel);

            Stage stage = new Stage();
            stage.setTitle("Détails de l'hôtel");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des détails de l'hôtel.");
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    public void handleReserveButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Reservation/addreservation.fxml"));
            Parent root = loader.load();
            Addreservation detailController = loader.getController();
            detailController.setHotel(hotel);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reservation Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}