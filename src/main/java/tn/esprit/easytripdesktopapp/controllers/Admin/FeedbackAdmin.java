package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Feedback;
import tn.esprit.easytripdesktopapp.services.ServiceFeedback;



import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import java.sql.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class FeedbackAdmin {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField messageField;

    @FXML
    private TextField offerIdField;

    @FXML
    private Spinner<Integer> ratingSpinner;

    @FXML
    private TextField searchField;

    @FXML
    private TextField userIdField;

    @FXML
    private HBox feedbackContainer;

    @FXML
    private Label userIdError;

    @FXML
    private Label offerIdError;

    @FXML
    private Label ratingError;

    @FXML
    private Label messageError;

    @FXML
    private Label dateError;

    @FXML
    private Label searchError;

    private Feedback selectedFeedback;

    @FXML
    void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1);
        ratingSpinner.setValueFactory(valueFactory);

        loadFeedbacks();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
    }

    @FXML
    void backToHome(ActionEvent event) {
        try {
            ResourceBundle loginBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/TablesAdmin.fxml"), loginBundle);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteFeedback(ActionEvent event) {
        try {
            Feedback selectedFeedback = getSelectedFeedback();
            if (selectedFeedback == null) {
                showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner un feedback à supprimer.");
                return;
            }
            ServiceFeedback feedbackService = new ServiceFeedback();
            feedbackService.delete(selectedFeedback);

            showAlert(Alert.AlertType.CONFIRMATION, "Feedback supprimé avec succès.");
            loadFeedbacks();
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression du feedback : " + e.getMessage());
        }
    }

    @FXML
    void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ServiceFeedback feedbackService = new ServiceFeedback();
        ObservableList<Feedback> allFeedbacks = FXCollections.observableArrayList(feedbackService.getAll());
        ObservableList<Feedback> filteredList = allFeedbacks
                .filtered(feedback -> String.valueOf(feedback.getUserId()).contains(searchText) ||
                        String.valueOf(feedback.getOfferId()).contains(searchText) ||
                        String.valueOf(feedback.getRating()).contains(searchText) ||
                        feedback.getMessage().toLowerCase().contains(searchText));
        displayFeedbacks(filteredList);
    }

    @FXML
    void updateFeedback(ActionEvent event) {
        try {
            if (!validateInputs()) {
                return;
            }
            Feedback selectedFeedback = getSelectedFeedback();
            if (selectedFeedback == null) {
                showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner un feedback à mettre à jour.");
                return;
            }
            selectedFeedback.setUserId(Integer.parseInt(userIdField.getText()));
            selectedFeedback.setOfferId(Integer.parseInt(offerIdField.getText()));
            selectedFeedback.setRating(ratingSpinner.getValue());
            selectedFeedback.setMessage(messageField.getText());
            selectedFeedback.setDate(Date.valueOf(datePicker.getValue()));

            ServiceFeedback feedbackService = new ServiceFeedback();
            feedbackService.update(selectedFeedback);

            showAlert(Alert.AlertType.CONFIRMATION, "Feedback mis à jour avec succès.");
            loadFeedbacks();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour du feedback : " + e.getMessage());
        }
    }

    private void loadFeedbacks() {
        ServiceFeedback feedbackService = new ServiceFeedback();
        ObservableList<Feedback> feedbacks = FXCollections.observableArrayList(feedbackService.getAll());
        displayFeedbacks(feedbacks);
    }

    private void displayFeedbacks(ObservableList<Feedback> feedbacks) {
        feedbackContainer.getChildren().clear();

        for (Feedback feedback : feedbacks) {
            AnchorPane card = createFeedbackCard(feedback);
            feedbackContainer.getChildren().add(card);
        }
    }

    private AnchorPane createFeedbackCard(Feedback feedback) {
        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("feedback-card");

        Text userIdText = new Text("ID Utilisateur: " + feedback.getUserId());
        Text offerIdText = new Text("ID Offre: " + feedback.getOfferId());
        Text ratingText = new Text("Note: " + feedback.getRating());
        Text messageText = new Text("Message: " + feedback.getMessage());
        Text dateText = new Text("Date: " + feedback.getDate());

        AnchorPane.setTopAnchor(userIdText, 10.0);
        AnchorPane.setLeftAnchor(userIdText, 10.0);

        AnchorPane.setTopAnchor(offerIdText, 30.0);
        AnchorPane.setLeftAnchor(offerIdText, 10.0);

        AnchorPane.setTopAnchor(ratingText, 50.0);
        AnchorPane.setLeftAnchor(ratingText, 10.0);

        AnchorPane.setTopAnchor(messageText, 70.0);
        AnchorPane.setLeftAnchor(messageText, 10.0);

        AnchorPane.setTopAnchor(dateText, 90.0);
        AnchorPane.setLeftAnchor(dateText, 10.0);

        card.getChildren().addAll(userIdText, offerIdText, ratingText, messageText, dateText);

        card.setOnMouseClicked(event -> fillFormWithFeedback(feedback));

        return card;
    }

    private void fillFormWithFeedback(Feedback feedback) {
        this.selectedFeedback = feedback;
        userIdField.setText(String.valueOf(feedback.getUserId()));
        offerIdField.setText(String.valueOf(feedback.getOfferId()));
        ratingSpinner.getValueFactory().setValue(feedback.getRating());
        messageField.setText(feedback.getMessage());
        datePicker.setValue(feedback.getDate().toLocalDate());
    }

    private Feedback getSelectedFeedback() {
        return selectedFeedback;
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Gestion des Feedbacks");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void clearFields() {
        userIdField.clear();
        offerIdField.clear();
        ratingSpinner.getValueFactory().setValue(1);
        messageField.clear();
        datePicker.setValue(null);
        selectedFeedback = null;
    }

    private boolean validateInputs() {
        boolean isValid = true;

        userIdError.setText("");
        offerIdError.setText("");
        ratingError.setText("");
        messageError.setText("");
        dateError.setText("");

        try {
            int userId = Integer.parseInt(userIdField.getText().trim());
            if (userId <= 0) {
                userIdError.setText("L'ID utilisateur doit être un nombre positif.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            userIdError.setText("L'ID utilisateur doit être un nombre valide.");
            isValid = false;
        }

        try {
            int offerId = Integer.parseInt(offerIdField.getText().trim());
            if (offerId <= 0) {
                offerIdError.setText("L'ID de l'offre doit être un nombre positif.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            offerIdError.setText("L'ID de l'offre doit être un nombre valide.");
            isValid = false;
        }

        if (messageField.getText().trim().isEmpty()) {
            messageError.setText("Le champ message ne peut pas être vide.");
            isValid = false;
        }

        if (datePicker.getValue() == null) {
            dateError.setText("La date doit être sélectionnée.");
            isValid = false;
        } else if (datePicker.getValue().isAfter(java.time.LocalDate.now())) {
            dateError.setText("La date ne peut pas être dans le futur.");
            isValid = false;
        }

        if (ratingSpinner.getValue() == null || ratingSpinner.getValue() < 1 || ratingSpinner.getValue() > 5) {
            ratingError.setText("La note doit être entre 1 et 5.");
            isValid = false;
        }

        return isValid;
    }




    public void exportStatisticsToPDF(int offerId, String filePath) {
        ServiceFeedback feedbackService = new ServiceFeedback();
        List<Feedback> feedbacks = feedbackService.getFeedbacksByOfferId(offerId);

        if (feedbacks.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucun feedback trouvé pour cette offre.");
            return;
        }

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Titre du document
            document.add(new Paragraph("Statistiques des feedbacks pour l'offre ID: " + offerId));

            // Calcul des statistiques
            double averageRating = feedbacks.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
            long totalFeedbacks = feedbacks.size();

            // Ajout des statistiques au PDF
            document.add(new Paragraph("Nombre total de feedbacks: " + totalFeedbacks));
            document.add(new Paragraph("Note moyenne: " + String.format("%.2f", averageRating)));

            // Ajout des détails des feedbacks
            document.add(new Paragraph("\nDétails des feedbacks:"));
            for (Feedback feedback : feedbacks) {
                document.add(new Paragraph("ID Utilisateur: " + feedback.getUserId()));
                document.add(new Paragraph("Note: " + feedback.getRating()));
                document.add(new Paragraph("Message: " + feedback.getMessage()));
                document.add(new Paragraph("Date: " + feedback.getDate()));
                document.add(new Paragraph("-----------------------------"));
            }

            document.close();
            showAlert(Alert.AlertType.INFORMATION, "PDF généré avec succès: " + filePath);
        } catch (DocumentException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la génération du PDF: " + e.getMessage());
        }
    }

    @FXML
    void exportStatisticsToPDF(ActionEvent event) {
        try {
            int offerId = Integer.parseInt(offerIdField.getText().trim());
            if (offerId <= 0) {
                showAlert(Alert.AlertType.WARNING, "Veuillez entrer un ID d'offre valide.");
                return;
            }

            // Chemin du fichier PDF à générer
            String filePath = "statistics_offer_" + offerId + ".pdf";
            exportStatisticsToPDF(offerId, filePath);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "L'ID de l'offre doit être un nombre valide.");
        }
    }
}