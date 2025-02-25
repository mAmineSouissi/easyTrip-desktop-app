package tn.esprit.easytripdesktopapp.controllers.Admin.Promotion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.sql.Date;
import java.util.List;
import java.util.Properties;

public class UpdatePromotion {

    @FXML
    private TextField title;

    @FXML
    private TextArea description;

    @FXML
    private TextField discountPercentage;

    @FXML
    private DatePicker validUntil;

    private Promotion selectedPromotion;

    CRUDService<Promotion> promotionService = new ServicePromotion();
    ServiceAgence serviceAgence = new ServiceAgence();

    // Méthode pour recevoir la promotion sélectionnée
    public void setPromotion(Promotion promotion) {
        this.selectedPromotion = promotion;

        // Remplir les champs avec les données existantes
        title.setText(promotion.getTitle());
        description.setText(promotion.getDescription());
        discountPercentage.setText(String.valueOf(promotion.getDiscount_percentage()));

        // Convertir java.util.Date en LocalDate pour le DatePicker
        if (promotion.getValid_until() != null) {
            Date sqlDate = new Date(promotion.getValid_until().getTime());
            validUntil.setValue(sqlDate.toLocalDate());
        }
    }

    @FXML
    public void updatePromotion(ActionEvent actionEvent) {
        if (title.getText().isEmpty() || description.getText().isEmpty() || discountPercentage.getText().isEmpty() || validUntil.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !", Alert.AlertType.ERROR);
            return;
        }

        // Vérifier si la date choisie est avant aujourd'hui
        if (validUntil.getValue().isBefore(java.time.LocalDate.now())) {
            showAlert("Erreur", "La date de validité ne peut pas être antérieure à aujourd'hui !", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Convertir discountPercentage en float
            float discount = Float.parseFloat(discountPercentage.getText());

            // Vérifier que le pourcentage de réduction est compris entre 0 et 100
            if (discount < 0 || discount > 100) {
                showAlert("Erreur", "Le pourcentage de réduction doit être compris entre 0 et 100 !", Alert.AlertType.ERROR);
                return;
            }

            // Convertir validUntil en java.util.Date
            java.util.Date utilDate = Date.valueOf(validUntil.getValue());

            // Mettre à jour la promotion sélectionnée
            selectedPromotion.setTitle(title.getText());
            selectedPromotion.setDescription(description.getText());
            selectedPromotion.setDiscount_percentage(discount);
            selectedPromotion.setValid_until(utilDate);

            // Appeler la méthode de mise à jour du service
            promotionService.update(selectedPromotion);
            showAlert("Succès", "Promotion mise à jour avec succès !", Alert.AlertType.INFORMATION);

            // Envoyer un e-mail aux agences pour les informer de la mise à jour
            envoyerEmailAuxAgences(selectedPromotion);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le pourcentage de réduction doit être un nombre valide !", Alert.AlertType.ERROR);
        }
    }

    // Méthode pour envoyer un e-mail aux agences
    private void envoyerEmailAuxAgences(Promotion promotion) {
        List<String> agencyEmails = serviceAgence.getAllAgencyEmails(); // Récupérer les e-mails des agences

        if (agencyEmails.isEmpty()) {
            showAlert("Info", "Aucune agence n'a été trouvée pour l'envoi de l'e-mail.", Alert.AlertType.INFORMATION);
            return;
        }

        String smtpHost = "smtp.gmail.com";
        String smtpPort = "587";
        String username = "youssefcarma@gmail.com"; // Remplace par ton e-mail
        String password = "fvbj ygsn jsin aapn"; // Remplace par ton mot de passe

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            for (String recipient : agencyEmails) {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject("Mise à jour de la Promotion: " + promotion.getTitle());
                message.setText("Description: " + promotion.getDescription() + "\n" +
                        "Nouveau pourcentage de réduction: " + promotion.getDiscount_percentage() + "%\n" +
                        "Valide jusqu'au: " + promotion.getValid_until());

                Transport.send(message);
            }

            showAlert("Succès", "E-mails envoyés avec succès aux agences !", Alert.AlertType.INFORMATION);
        } catch (MessagingException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'envoi des e-mails : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}