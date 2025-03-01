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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Reclamation;
import tn.esprit.easytripdesktopapp.services.ServiceReclamation;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ReclamationAdmin {

    @FXML
    private HBox cardContainer;
    @FXML
    private TextField userIdField;
    @FXML
    private Label userIdError;
    @FXML
    private TextField issueField;
    @FXML
    private Label issueError;
    @FXML
    private TextField categoryField;
    @FXML
    private Label categoryError;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Label statusError;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label dateError;
    @FXML
    private TextField searchField;

    private Reclamation selectedReclamation;

    private static final String EXCEL_FILE_PATH = "reclamations.xlsx";

    @FXML
    void initialize() {
        statusComboBox.setItems(FXCollections.observableArrayList("En attente", "En cours", "Résolue", "Fermée"));
        loadReclamations();
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

    private void loadReclamations() {
        ServiceReclamation reclamationService = new ServiceReclamation();
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList(reclamationService.getAll());
        displayReclamations(reclamations);
    }

    private void displayReclamations(ObservableList<Reclamation> reclamations) {
        cardContainer.getChildren().clear();

        for (Reclamation rec : reclamations) {
            VBox card = new VBox();
            card.getStyleClass().add("card");

            Label userIdLabel = new Label("ID Utilisateur: " + rec.getUserId());
            userIdLabel.getStyleClass().add("card-label");

            Label issueLabel = new Label("Problème: " + rec.getIssue());
            issueLabel.getStyleClass().add("card-label");

            Label categoryLabel = new Label("Catégorie: " + rec.getCategory());
            categoryLabel.getStyleClass().add("card-label");

            Label statusLabel = new Label("Statut: " + rec.getStatus());
            statusLabel.getStyleClass().add("card-label");

            Label dateLabel = new Label("Date: " + rec.getDate());
            dateLabel.getStyleClass().add("card-label");

            card.getChildren().addAll(userIdLabel, issueLabel, categoryLabel, statusLabel, dateLabel);
            card.setOnMouseClicked(event -> handleCardClick(rec));

            cardContainer.getChildren().add(card);
        }
    }

    private void handleCardClick(Reclamation reclamation) {
        selectedReclamation = reclamation;
        fillFormWithReclamation(reclamation);
    }

    private void fillFormWithReclamation(Reclamation reclamation) {
        userIdField.setText(String.valueOf(reclamation.getUserId()));
        issueField.setText(reclamation.getIssue());
        categoryField.setText(reclamation.getCategory());
        statusComboBox.setValue(reclamation.getStatus());
        datePicker.setValue(reclamation.getDate().toLocalDate());
    }

    @FXML
    void deleteReclamation(ActionEvent event) {
        try {
            if (selectedReclamation == null) {
                showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner une réclamation à supprimer.");
                return;
            }
            ServiceReclamation reclamationService = new ServiceReclamation();
            reclamationService.delete(selectedReclamation);
            showAlert(Alert.AlertType.CONFIRMATION, "Réclamation supprimée avec succès.");
            loadReclamations();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @FXML
    void handleSearch() {
        String query = searchField.getText().toLowerCase();
        ServiceReclamation reclamationService = new ServiceReclamation();
        ObservableList<Reclamation> allReclamations = FXCollections.observableArrayList(reclamationService.getAll());
        ObservableList<Reclamation> filteredList = allReclamations
                .filtered(reclamation -> reclamation.getIssue().toLowerCase().contains(query) ||
                        reclamation.getCategory().toLowerCase().contains(query) ||
                        reclamation.getStatus().toLowerCase().contains(query) ||
                        String.valueOf(reclamation.getUserId()).contains(query));
        displayReclamations(filteredList);
    }

    @FXML
    void updateReclamation(ActionEvent event) {
        try {
            if (!validateInputs()) {
                return;
            }

            if (selectedReclamation == null) {
                showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner une réclamation à mettre à jour.");
                return;
            }

            selectedReclamation.setUserId(Integer.parseInt(userIdField.getText()));
            selectedReclamation.setIssue(issueField.getText());
            selectedReclamation.setCategory(categoryField.getText());
            selectedReclamation.setStatus(statusComboBox.getValue());
            selectedReclamation.setDate(Date.valueOf(datePicker.getValue()));

            ServiceReclamation reclamationService = new ServiceReclamation();
            reclamationService.update(selectedReclamation);

            showAlert(Alert.AlertType.CONFIRMATION, "Réclamation mise à jour avec succès.");
            loadReclamations();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @FXML
    void exportToExcel(ActionEvent event) {
        try {
            // Chemin du fichier Excel à générer
            String filePath = "exported_reclamations.xlsx";

            // Charger les réclamations depuis la base de données
            ServiceReclamation reclamationService = new ServiceReclamation();
            List<Reclamation> reclamations = reclamationService.getAll();

            // Créer un nouveau fichier Excel
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Reclamations");

                // Créer l'en-tête
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("ID");
                headerRow.createCell(1).setCellValue("User ID");
                headerRow.createCell(2).setCellValue("Issue");
                headerRow.createCell(3).setCellValue("Category");
                headerRow.createCell(4).setCellValue("Status");
                headerRow.createCell(5).setCellValue("Date");

                // Remplir les données
                int rowNum = 1;
                for (Reclamation reclamation : reclamations) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(reclamation.getId());
                    row.createCell(1).setCellValue(reclamation.getUserId());
                    row.createCell(2).setCellValue(reclamation.getIssue());
                    row.createCell(3).setCellValue(reclamation.getCategory());
                    row.createCell(4).setCellValue(reclamation.getStatus());
                    row.createCell(5).setCellValue(reclamation.getDate().toString());
                }

                // Écrire dans le fichier
                try (FileOutputStream outFile = new FileOutputStream(filePath)) {
                    workbook.write(outFile);
                }
            }

            showAlert(Alert.AlertType.INFORMATION, "Réclamations exportées avec succès dans : " + filePath);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'export Excel : " + e.getMessage());
        }
    }

    @FXML
    void synchronizeWithDatabase(ActionEvent event) {
        try {
            // Chemin du fichier Excel modifié
            String filePath = "exported_reclamations.xlsx";

            // Lire les réclamations modifiées depuis le fichier Excel
            List<Reclamation> modifiedReclamations = readModifiedExcelFile(filePath);

            // Synchroniser les changements avec la base de données
            synchronizeWithDatabase(modifiedReclamations);

            // Recharger les réclamations pour afficher les modifications
            loadReclamations();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la synchronisation : " + e.getMessage());
        }
    }

    private List<Reclamation> readModifiedExcelFile(String filePath) {
        List<Reclamation> reclamations = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through rows (skip the header row)
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                // Read data from each cell
                int id = (int) row.getCell(0).getNumericCellValue();
                int userId = (int) row.getCell(1).getNumericCellValue();
                String issue = row.getCell(2).getStringCellValue();
                String category = row.getCell(3).getStringCellValue();
                String status = row.getCell(4).getStringCellValue();
                Date date = Date.valueOf(row.getCell(5).getStringCellValue());

                // Create a Reclamation object and add it to the list
                Reclamation reclamation = new Reclamation(id, userId, issue, date, category, status);
                reclamations.add(reclamation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la lecture du fichier Excel modifié : " + e.getMessage());
        }
        return reclamations;
    }

    private void synchronizeWithDatabase(List<Reclamation> modifiedReclamations) {
        ServiceReclamation reclamationService = new ServiceReclamation();
        List<Reclamation> databaseReclamations = reclamationService.getAll();

        // Synchronize changes
        for (Reclamation modifiedReclamation : modifiedReclamations) {
            boolean existsInDatabase = false;

            // Check if the reclamation exists in the database
            for (Reclamation databaseReclamation : databaseReclamations) {
                if (databaseReclamation.getId() == modifiedReclamation.getId()) {
                    existsInDatabase = true;

                    // Update the reclamation if it has changed
                    if (!databaseReclamation.equals(modifiedReclamation)) {
                        reclamationService.update(modifiedReclamation);
                    }
                    break;
                }
            }

            // If the reclamation does not exist in the database, add it
            if (!existsInDatabase) {
                reclamationService.add(modifiedReclamation);
            }
        }

        // Check for reclamations that were deleted in the Excel file
        for (Reclamation databaseReclamation : databaseReclamations) {
            boolean existsInExcel = false;

            for (Reclamation modifiedReclamation : modifiedReclamations) {
                if (databaseReclamation.getId() == modifiedReclamation.getId()) {
                    existsInExcel = true;
                    break;
                }
            }

            // If the reclamation does not exist in the Excel file, delete it from the database
            if (!existsInExcel) {
                reclamationService.delete(databaseReclamation);
            }
        }

        showAlert(Alert.AlertType.INFORMATION, "Synchronisation avec la base de données terminée.");
    }

    private boolean validateInputs() {
        boolean isValid = true;

        userIdError.setText("");
        issueError.setText("");
        categoryError.setText("");
        statusError.setText("");
        dateError.setText("");

        try {
            int userId = Integer.parseInt(userIdField.getText());
            if (userId <= 0) {
                userIdError.setText("L'ID utilisateur doit être un nombre positif.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            userIdError.setText("L'ID utilisateur doit être un nombre valide.");
            isValid = false;
        }

        if (issueField.getText().trim().isEmpty()) {
            issueError.setText("Le problème ne peut pas être vide.");
            isValid = false;
        }

        if (categoryField.getText().trim().isEmpty()) {
            categoryError.setText("La catégorie ne peut pas être vide.");
            isValid = false;
        }

        if (statusComboBox.getValue() == null || statusComboBox.getValue().trim().isEmpty()) {
            statusError.setText("Le statut doit être sélectionné.");
            isValid = false;
        }

        if (datePicker.getValue() == null) {
            dateError.setText("La date doit être sélectionnée.");
            isValid = false;
        }

        return isValid;
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Gestion des Réclamations");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    public void sendEmail(ActionEvent actionEvent) {
        if (selectedReclamation == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner une réclamation pour envoyer un e-mail.");
            return;
        }

        // Fetch the email of the user associated with the selected reclamation
        String clientEmail = getEmailByUserId(selectedReclamation.getUserId());

        if (clientEmail == null || clientEmail.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Aucun e-mail trouvé pour cet utilisateur.");
            return;
        }

        // Simulate user information (name, surname)
        String clientName = "Oussema"; // User's name
        String clientSurname = "Msehli"; // User's surname

        // Email subject and body
        String subject = "Votre réclamation a été traitée";
        String body = "Bonjour " + clientName + " " + clientSurname + ",\n\n" +
                "Votre réclamation concernant '" + selectedReclamation.getIssue() + "' a été traitée. " +
                "Le statut est maintenant '" + selectedReclamation.getStatus() + "'.\n\n" +
                "Cordialement,\nL'équipe de support.";

        // Send the email
        try {
            if (sendReclamationEmail(clientEmail, subject, body)) {
                showAlert(Alert.AlertType.INFORMATION, "E-mail envoyé avec succès à " + clientEmail);
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de l'envoi de l'e-mail.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }
    }

    public boolean sendReclamationEmail(String recipientEmail, String subject, String body) {
        // Email configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Use Gmail's SMTP server
        props.put("mail.smtp.port", "587"); // Port for TLS

        // Email credentials (use your application's email and password/app password)
        final String senderEmail = "omsehli@gmail.com"; // Replace with your email
        final String password = "cimh ylri oahd pvlz"; // Replace with your app password

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send the message
            Transport.send(message);

            System.out.println("E-mail envoyé avec succès à : " + recipientEmail);
            return true;
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getEmailByUserId(int userId) {
        String email = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establish a connection to the database
            connection = MyDataBase.getInstance().getCnx();

            // Prepare the SQL query to fetch the email by userId
            String query = "SELECT email FROM User WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // If a result is found, retrieve the email
            if (resultSet.next()) {
                email = resultSet.getString("email");
            }

            System.out.println("Email retrieved for user " + userId + ": " + email);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return email;
    }
}