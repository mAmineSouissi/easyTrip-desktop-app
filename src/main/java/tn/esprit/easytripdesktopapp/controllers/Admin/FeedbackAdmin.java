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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tn.esprit.easytripdesktopapp.models.Feedback;
import tn.esprit.easytripdesktopapp.services.ServiceFeedback;



import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

    private static final String EXCEL_FILE_PATH = "feedbacks.xlsx";

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
            stage.setTitle("Admin Screen");
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

            // Supprimer le feedback dans la base de données
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

            // Mettre à jour le feedback dans la base de données
            ServiceFeedback feedbackService = new ServiceFeedback();
            feedbackService.update(selectedFeedback);

            showAlert(Alert.AlertType.CONFIRMATION, "Feedback mis à jour avec succès.");
            loadFeedbacks();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour du feedback : " + e.getMessage());
        }
    }

    @FXML
    void exportToExcel(ActionEvent event) {
        try {
            // Chemin du fichier Excel à générer
            String filePath = "exported_feedbacks.xlsx";

            // Charger les feedbacks depuis la base de données
            ServiceFeedback feedbackService = new ServiceFeedback();
            List<Feedback> feedbacks = feedbackService.getAll();

            // Créer un nouveau fichier Excel
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Feedbacks");

                // Créer l'en-tête
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("ID");
                headerRow.createCell(1).setCellValue("User ID");
                headerRow.createCell(2).setCellValue("Offer ID");
                headerRow.createCell(3).setCellValue("Rating");
                headerRow.createCell(4).setCellValue("Message");
                headerRow.createCell(5).setCellValue("Date");

                // Remplir les données
                int rowNum = 1;
                for (Feedback feedback : feedbacks) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(feedback.getId());
                    row.createCell(1).setCellValue(feedback.getUserId());
                    row.createCell(2).setCellValue(feedback.getOfferId());
                    row.createCell(3).setCellValue(feedback.getRating());
                    row.createCell(4).setCellValue(feedback.getMessage());
                    row.createCell(5).setCellValue(feedback.getDate().toString());
                }

                // Écrire dans le fichier
                try (FileOutputStream outFile = new FileOutputStream(filePath)) {
                    workbook.write(outFile);
                }
            }

            showAlert(Alert.AlertType.INFORMATION, "Feedbacks exportés avec succès dans : " + filePath);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'export Excel : " + e.getMessage());
        }
    }

    @FXML
    void synchronizeWithDatabase(ActionEvent event) {
        try {
            // Chemin du fichier Excel modifié
            String filePath = "exported_feedbacks.xlsx";

            // Lire les feedbacks modifiés depuis le fichier Excel
            List<Feedback> modifiedFeedbacks = readModifiedExcelFile(filePath);

            // Synchroniser les changements avec la base de données
            synchronizeWithDatabase(modifiedFeedbacks);

            // Recharger les feedbacks pour afficher les modifications
            loadFeedbacks();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la synchronisation : " + e.getMessage());
        }
    }

    private List<Feedback> readModifiedExcelFile(String filePath) {
        List<Feedback> feedbacks = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through rows (skip the header row)
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                // Read data from each cell
                int id = (int) row.getCell(0).getNumericCellValue();
                int userId = (int) row.getCell(1).getNumericCellValue();
                int offerId = (int) row.getCell(2).getNumericCellValue();
                int rating = (int) row.getCell(3).getNumericCellValue();
                String message = row.getCell(4).getStringCellValue();
                Date date = Date.valueOf(row.getCell(5).getStringCellValue());

                // Create a Feedback object and add it to the list
                Feedback feedback = new Feedback(id, userId, offerId, rating, message, date);
                feedbacks.add(feedback);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la lecture du fichier Excel modifié : " + e.getMessage());
        }
        return feedbacks;
    }

    private void synchronizeWithDatabase(List<Feedback> modifiedFeedbacks) {
        ServiceFeedback feedbackService = new ServiceFeedback();
        List<Feedback> databaseFeedbacks = feedbackService.getAll();

        // Synchronize changes
        for (Feedback modifiedFeedback : modifiedFeedbacks) {
            boolean existsInDatabase = false;

            // Check if the feedback exists in the database
            for (Feedback databaseFeedback : databaseFeedbacks) {
                if (databaseFeedback.getId() == modifiedFeedback.getId()) {
                    existsInDatabase = true;

                    // Update the feedback if it has changed
                    if (!databaseFeedback.equals(modifiedFeedback)) {
                        feedbackService.update(modifiedFeedback);
                    }
                    break;
                }
            }

            // If the feedback does not exist in the database, add it
            if (!existsInDatabase) {
                feedbackService.add(modifiedFeedback);
            }
        }

        // Check for feedbacks that were deleted in the Excel file
        for (Feedback databaseFeedback : databaseFeedbacks) {
            boolean existsInExcel = false;

            for (Feedback modifiedFeedback : modifiedFeedbacks) {
                if (databaseFeedback.getId() == modifiedFeedback.getId()) {
                    existsInExcel = true;
                    break;
                }
            }

            // If the feedback does not exist in the Excel file, delete it from the database
            if (!existsInExcel) {
                feedbackService.delete(databaseFeedback);
            }
        }

        showAlert(Alert.AlertType.INFORMATION, "Synchronisation avec la base de données terminée.");
    }

    private void loadFeedbacks() {
        // Charger les feedbacks depuis la base de données
        ServiceFeedback feedbackService = new ServiceFeedback();
        List<Feedback> feedbacks = feedbackService.getAll();

        // Afficher les feedbacks dans la CardView
        ObservableList<Feedback> observableFeedbacks = FXCollections.observableArrayList(feedbacks);
        displayFeedbacks(observableFeedbacks);
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