package tn.esprit.easytripdesktopapp.controller;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;


public class SendGridUtil {
    public static void sendEmail(String to, String from, String subject, String body) {
      Dotenv dotenv = Dotenv.load();
      String apiKey = dotenv.get("API_KEY");
        Email fromEmail = new Email(from); // Utilisez l'adresse e-mail vérifiée
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        // Envoi de l'e-mail
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Code de statut : " + response.getStatusCode());
            System.out.println("Corps de la réponse : " + response.getBody());
            System.out.println("En-têtes de la réponse : " + response.getHeaders());
        } catch (IOException ex) {
            System.err.println("Erreur lors de l'envoi de l'e-mail : " + ex.getMessage());
        }
    }
}
