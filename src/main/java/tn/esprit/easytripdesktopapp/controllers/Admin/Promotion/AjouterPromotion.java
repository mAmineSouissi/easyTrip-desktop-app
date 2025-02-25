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

public class AjouterPromotion {

    @FXML
    private TextField title;

    @FXML
    private TextArea description;

    @FXML
    private TextField discountPercentage;

    @FXML
    private DatePicker validUntil;

    CRUDService<Promotion> promotionService = new ServicePromotion();
    ServiceAgence serviceAgence = new ServiceAgence();

    @FXML
    void save(ActionEvent event) {
        if (title.getText().isEmpty() || description.getText().isEmpty() || discountPercentage.getText().isEmpty() || validUntil.getValue() == null) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis !", Alert.AlertType.ERROR);
            return;
        }

        // Vérifier si la date choisie est avant aujourd'hui
        if (validUntil.getValue().isBefore(java.time.LocalDate.now())) {
            afficherAlerte("Erreur", "La date de validité ne peut pas être antérieure à aujourd'hui !", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Convertir discountPercentage en float
            float discount = Float.parseFloat(discountPercentage.getText());

            // Vérifier que le pourcentage de réduction est compris entre 0 et 100
            if (discount < 0 || discount > 100) {
                afficherAlerte("Erreur", "Le pourcentage de réduction doit être compris entre 0 et 100 !", Alert.AlertType.ERROR);
                return;
            }

            // Convertir validUntil en java.sql.Date
            Date sqlDate = Date.valueOf(validUntil.getValue());

            // Créer une nouvelle promotion
            Promotion p = new Promotion();
            p.setTitle(title.getText());
            p.setDescription(description.getText());
            p.setDiscount_percentage(discount);
            p.setValid_until(sqlDate);

            // Ajouter la promotion via le service
            promotionService.add(p);
            afficherAlerte("Succès", "Promotion ajoutée avec succès !", Alert.AlertType.INFORMATION);

            // Envoyer un e-mail à toutes les agences
            envoyerEmailAuxAgences(p);
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Le pourcentage de réduction doit être un nombre valide !", Alert.AlertType.ERROR);
        }
    }

    private void envoyerEmailAuxAgences(Promotion promotion) {
        List<String> agencyEmails = serviceAgence.getAllAgencyEmails(); // Récupérer les e-mails des agences

        if (agencyEmails.isEmpty()) {
            afficherAlerte("Info", "Aucune agence n'a été trouvée pour l'envoi de l'e-mail.", Alert.AlertType.INFORMATION);
            return;
        }

        String smtpHost = "smtp.gmail.com";
        String smtpPort = "587";
        String username = "youssefcarma@gmail.com";
        String password = "fvbj ygsn jsin aapn";

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
                message.setSubject("Nouvelle Promotion: " + promotion.getTitle());
                message.setText("Description: " + promotion.getDescription() + "\n" +
                        "Pourcentage de réduction: " + promotion.getDiscount_percentage() + "%\n" +
                        "Valide jusqu'au: " + promotion.getValid_until());

                Transport.send(message);
            }

            afficherAlerte("Succès", "E-mails envoyés avec succès aux agences !", Alert.AlertType.INFORMATION);
        } catch (MessagingException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Erreur lors de l'envoi des e-mails : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType typeAlerte) {
        Alert alerte = new Alert(typeAlerte);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }
}
