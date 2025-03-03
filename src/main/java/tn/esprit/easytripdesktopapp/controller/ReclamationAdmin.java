package tn.esprit.easytripdesktopapp.controllers.Admin;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.type.PhoneNumber;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;
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
    private Connection connection;

    private Reclamation selectedReclamation;

    // Map to track whether a communication has been sent to a specific client
    private final Map<Integer, Set<String>> communicationSentMap = new HashMap<>();

    private static final String EXCEL_FILE_PATH = "reclamations.xlsx";
    public ReclamationAdmin() {
            this.connection = MyDataBase.getInstance().getCnx();
    }
   

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
        System.out.println("Selected reclamation: " + reclamation); // Log pour vérifier
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
            String filePath = "C://Users//ousse//Desktop//easyTrip-desktop-app-test/exported_reclamations.xlsx"; // Use an absolute path
            ServiceReclamation reclamationService = new ServiceReclamation();
            List<Reclamation> reclamations = reclamationService.getAll();

            // Debug: Check if reclamations list is empty
            if (reclamations.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No reclamations found to export.");
                return;
            }
            System.out.println("Reclamations to export: " + reclamations);

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Reclamations");
                System.out.println("Workbook and sheet created successfully.");

                // Create header row
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("ID");
                headerRow.createCell(1).setCellValue("User ID");
                headerRow.createCell(2).setCellValue("Issue");
                headerRow.createCell(3).setCellValue("Category");
                headerRow.createCell(4).setCellValue("Status");
                headerRow.createCell(5).setCellValue("Date");

                // Populate data rows
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
                System.out.println("Data written to sheet successfully.");

                // Write workbook to file
                try (FileOutputStream outFile = new FileOutputStream(filePath)) {
                    workbook.write(outFile);
                    System.out.println("File written successfully to: " + filePath);
                }
            }

            showAlert(Alert.AlertType.INFORMATION, "Réclamations exportées avec succès dans : " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'export Excel : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage());
        }
    }
    @FXML
    void synchronizeWithDatabase(ActionEvent event) {
        try {
            String filePath = "exported_reclamations.xlsx";
            List<Reclamation> modifiedReclamations = readModifiedExcelFile(filePath);
            synchronizeWithDatabase(modifiedReclamations);
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

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                int id = (int) row.getCell(0).getNumericCellValue();
                int userId = (int) row.getCell(1).getNumericCellValue();
                String issue = row.getCell(2).getStringCellValue();
                String category = row.getCell(3).getStringCellValue();
                String status = row.getCell(4).getStringCellValue();
                Date date = Date.valueOf(row.getCell(5).getStringCellValue());

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

        for (Reclamation modifiedReclamation : modifiedReclamations) {
            boolean existsInDatabase = false;

            for (Reclamation databaseReclamation : databaseReclamations) {
                if (databaseReclamation.getId() == modifiedReclamation.getId()) {
                    existsInDatabase = true;

                    if (!databaseReclamation.equals(modifiedReclamation)) {
                        reclamationService.update(modifiedReclamation);
                    }
                    break;
                }
            }

            if (!existsInDatabase) {
                reclamationService.add(modifiedReclamation);
            }
        }

        for (Reclamation databaseReclamation : databaseReclamations) {
            boolean existsInExcel = false;

            for (Reclamation modifiedReclamation : modifiedReclamations) {
                if (databaseReclamation.getId() == modifiedReclamation.getId()) {
                    existsInExcel = true;
                    break;
                }
            }

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
    void sendEmail(ActionEvent event) {
        if (selectedReclamation == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner une réclamation pour envoyer un e-mail.");
            return;
        }

        int userId = selectedReclamation.getUserId();
        System.out.println("Attempting to send email to user ID: " + userId); // Log the userId

        // Check if an email has already been sent to this client
        if (communicationSentMap.containsKey(userId) && communicationSentMap.get(userId).contains("email")) {
            showAlert(Alert.AlertType.WARNING, "Un e-mail a déjà été envoyé à ce client.");
            return;
        }

        String clientEmail = getEmailByUserId(userId);
        System.out.println("Retrieved email for user " + userId + ": " + clientEmail); // Log the retrieved email

        if (clientEmail == null || clientEmail.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Aucun e-mail trouvé pour cet utilisateur.");
            return;
        }

        String clientName = "Om"; // Nom de l'utilisateur
        String clientSurname = "Sehli"; // Prénom de l'utilisateur

        String subject = "Votre réclamation a été traitée";
        String body = "Bonjour " + clientName + " " + clientSurname + ",\n\n" +
                "Votre réclamation concernant '" + selectedReclamation.getIssue() + "' a été traitée. " +
                "Le statut est maintenant '" + selectedReclamation.getStatus() + "'.\n\n" +
                "Cordialement,\nL'équipe de support.";

        try {
            if (sendReclamationEmail(clientEmail, selectedReclamation.getStatus(), selectedReclamation.getIssue())) {
                // Mark this client as having received an email
                communicationSentMap.computeIfAbsent(userId, k -> new HashSet<>()).add("email");
                System.out.println("Email sent to user " + userId + ". Map updated: " + communicationSentMap); // Log the updated map
                showAlert(Alert.AlertType.INFORMATION, "E-mail envoyé avec succès à " + clientEmail);
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de l'envoi de l'e-mail.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }
    }

    public String getEmailByUserId(int userId) {

        String email = null;

        try (
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT email FROM user WHERE id = ?")) {

            System.out.println("Executing query: SELECT email FROM user WHERE id = ? for user ID: " + userId);

            // Définir le paramètre userId avant d'exécuter la requête
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    email = resultSet.getString("email");
                    System.out.println("Email retrieved for user " + userId + ": " + email);
                } else {
                    System.out.println("No email found for user " + userId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'e-mail : " + e.getMessage());
            e.printStackTrace();
        }

        return email;
    }

    public boolean sendReclamationEmail(String email, String status, String issue) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        final String senderEmail = "aminesouissi681@gmail.com"; // Replace with your email
        final String password = "cimh ylri oahd pvlz"; // Replace with your app password

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            javax.mail.internet.MimeMessage message = new javax.mail.internet.MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Mise à jour du statut de votre réclamation");

            String emailBody = "Bonjour,\n\n"
                    + "Nous avons une mise à jour concernant votre réclamation.\n\n"
                    + "Statut de la réclamation : " + status + "\n"
                    + "Description du problème : " + issue + "\n\n"
                    + "Si vous avez des questions supplémentaires ou besoin d'assistance, n'hésitez pas à contacter notre équipe de support.\n\n"
                    + "Cordialement,\n"
                    + "L'équipe Easy Trip";

            message.setText(emailBody);
            Transport.send(message);

            System.out.println("E-mail de réclamation envoyé à : " + email);
            return true;
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'e-mail de réclamation : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
@FXML
    public String getPhoneNumberByUserId(int userId) {

    String phoneNumber = null;

        try (
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT phone FROM user WHERE id = ?")) {

            System.out.println("Executing query: SELECT phone FROM user WHERE id = ? for user ID: " + userId);

            // Définir le paramètre userId avant d'exécuter la requête
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    phoneNumber = resultSet.getString("phone");
                    System.out.println("Phone number retrieved for user " + userId + ": " + phoneNumber);
                } else {
                    System.out.println("No phone number found for user " + userId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du numéro de téléphone : " + e.getMessage());
            e.printStackTrace();
        }

        return phoneNumber;
    }

    @FXML
    public void sendSms(ActionEvent actionEvent) {
        if (selectedReclamation == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner une réclamation pour envoyer un SMS.");
            return;
        }

        int userId = selectedReclamation.getUserId();

        // Check if an SMS has already been sent to this client
        if (communicationSentMap.containsKey(userId) && communicationSentMap.get(userId).contains("sms")) {
            showAlert(Alert.AlertType.WARNING, "Un SMS a déjà été envoyé à ce client.");
            return;
        }

        String recipientPhoneNumber = getPhoneNumberByUserId(userId);

        if (recipientPhoneNumber == null || recipientPhoneNumber.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Aucun numéro de téléphone trouvé pour cet utilisateur.");
            return;
        }

        if (!isValidPhoneNumber(recipientPhoneNumber)) {
            showAlert(Alert.AlertType.ERROR, "Le numéro de téléphone n'est pas valide. Il doit être au format international (ex: +2162865XXXX).");
            return;
        }

        String clientName = "Oussema"; // User's name
        String clientSurname = "Msehli"; // User's surname

        String smsBody = "Bonjour " + clientName + " " + clientSurname + ",\n\n" +
                "Votre réclamation concernant '" + selectedReclamation.getIssue() + "' a été traitée. " +
                "Le statut est maintenant '" + selectedReclamation.getStatus() + "'.\n\n" +
                "Cordialement,\nL'équipe de support.";

        if (sendSMS(recipientPhoneNumber, smsBody)) {
            // Mark this client as having received an SMS
            communicationSentMap.computeIfAbsent(userId, k -> new HashSet<>()).add("sms");
            System.out.println("SMS sent to user " + userId + ". Map updated: " + communicationSentMap); // Log the updated map
            showAlert(Alert.AlertType.INFORMATION, "SMS envoyé avec succès à " + recipientPhoneNumber);
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'envoi du SMS.");
        }
    }
    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\+[1-9]\\d{1,14}$");
    }
    public boolean sendSMS(String recipientPhoneNumber, String smsBody) {
        try {
            // Initialize Twilio
            Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);

            // Send the SMS
            com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message.creator(
                    new PhoneNumber(recipientPhoneNumber), // To
                    new PhoneNumber(TWILIO_PHONE_NUMBER), // From
                    smsBody // Message body
            ).create();

            System.out.println("SMS sent successfully! SID: " + message.getSid());
            return true;
        } catch (ApiException e) {
            System.err.println("Error sending SMS: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}