package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

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

        if (validUntil.getValue().isBefore(java.time.LocalDate.now())) {
            afficherAlerte("Erreur", "La date de validité ne peut pas être antérieure à aujourd'hui !", Alert.AlertType.ERROR);
            return;
        }

        try {
            float discount = Float.parseFloat(discountPercentage.getText());
            Date sqlDate = Date.valueOf(validUntil.getValue());

            Promotion p = new Promotion();
            p.setTitle(title.getText());
            p.setDescription(description.getText());
            p.setDiscount_percentage(discount);
            p.setValid_until(sqlDate);

            promotionService.add(p);
            afficherAlerte("Succès", "Promotion ajoutée avec succès !", Alert.AlertType.INFORMATION);

            // Envoyer un e-mail à toutes les agences via Infobip
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

        String apiKey = "7de7b8a9173b9ca371edbc28d6f8354b-5b2c3c88-c463-4e15-be8a-4b1060a6d31b"; // Remplace par ta clé API Infobip
        String senderEmail = "Azzouz.MohamedYoussef@esprit.tn"; // Remplace par ton e-mail validé sur Infobip

        String sujet = "Nouvelle Promotion: " + promotion.getTitle();
        String contenu = "Description: " + promotion.getDescription() + "\n" +
                "Pourcentage de réduction: " + promotion.getDiscount_percentage() + "%\n" +
                "Valide jusqu'au: " + promotion.getValid_until();

        // Construire JSON correctement formaté
        String jsonBody = "{"
                + "\"from\": \"" + senderEmail + "\","
                + "\"to\": [\"" + String.join("\", \"", agencyEmails) + "\"],"
                + "\"subject\": \"" + sujet + "\","
                + "\"text\": \"" + contenu + "\""
                + "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://z366rx.api.infobip.com/email/3/send"))
                .header("Authorization", "App " + apiKey)
                .header("Accept", "application/json")  // Ajout de l'en-tête Accept
                // .header("Content-Type", "application/json") // Retirer cette ligne
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Réponse de l'API Infobip: " + response.body());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                afficherAlerte("Succès", "E-mails envoyés avec succès aux agences !", Alert.AlertType.INFORMATION);
            } else {
                afficherAlerte("Erreur", "Échec de l'envoi des e-mails. Code: " + response.statusCode() + " Message: " + response.body(), Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
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
