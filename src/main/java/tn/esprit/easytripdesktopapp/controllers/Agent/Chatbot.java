package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.easytripdesktopapp.services.ChatbotService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Chatbot implements Initializable {

    @FXML
    private VBox chatContainer;

    @FXML
    private VBox chatMessages;

    @FXML
    private TextField chatInput;

    @FXML
    private Button sendButton;

    @FXML
    private ScrollPane scrollPane;

    // Classe interne pour stocker un message (texte et type)
    private static class ChatMessage {
        private String text;
        private String styleClass;

        public ChatMessage(String text, String styleClass) {
            this.text = text;
            this.styleClass = styleClass;
        }

        public String getText() {
            return text;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }

    // Liste statique pour stocker les messages (partagée entre toutes les instances du Chatbot)
    private static List<ChatMessage> messageHistory = new ArrayList<>();

    // Méthode pour réinitialiser l'historique des messages (appelée lors de la fermeture de l'application)
    public static void resetMessageHistory() {
        messageHistory.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialisation du contrôleur Chatbot...");
        System.out.println("chatContainer: " + chatContainer);
        System.out.println("chatMessages: " + chatMessages);
        System.out.println("chatInput: " + chatInput);
        System.out.println("sendButton: " + sendButton);
        System.out.println("scrollPane: " + scrollPane);
        if (scrollPane != null) {
            System.out.println("Contenu du scrollPane: " + scrollPane.getContent());
        }

        // Vérifier si chatMessages est null et créer un fallback si nécessaire
        if (chatMessages == null) {
            System.err.println("chatMessages est null ! Création d'un nouveau VBox...");
            chatMessages = new VBox();
            chatMessages.setSpacing(10);
            chatMessages.getStyleClass().add("chat-messages");
            chatMessages.setMinWidth(350);
            chatMessages.setPrefWidth(380);
            if (scrollPane != null) {
                scrollPane.setContent(chatMessages);
            }
        }

        // Activer les barres de défilement verticales
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);

        // Définir un padding pour chatMessages pour ajouter de l'espace en bas
        chatMessages.setPadding(new javafx.geometry.Insets(10, 10, 40, 10));

        // Faire défiler automatiquement vers le bas
        chatMessages.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            Platform.runLater(() -> {
                scrollPane.setVvalue(1.0); // Forcer le défilement jusqu'en bas
            });
        });

        // Charger les messages précédents depuis l'historique
        loadPreviousMessages();

        // Ajouter un message de bienvenue uniquement si l'historique est vide
        if (messageHistory.isEmpty()) {
            addMessage("Bienvenue dans le chatbot !", "bot-message");
        }
    }

    @FXML
    protected void sendMessage() {
        String userMessage = chatInput.getText().trim();
        if (userMessage != null && !userMessage.isEmpty()) {
            addMessage(userMessage, "user-message");
            String botResponse = ChatbotService.getChatbotResponse(userMessage);
            addMessage(botResponse, "bot-message");
            chatInput.clear();
        }
    }

    private void addMessage(String message, String styleClass) {
        if (chatMessages == null) {
            System.err.println("chatMessages est null ! Impossible d'ajouter le message: " + message);
            return;
        }

        // Sauvegarder le message dans l'historique
        messageHistory.add(new ChatMessage(message, styleClass));

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add("chat-message");
        messageLabel.getStyleClass().add(styleClass);
        messageLabel.setMinWidth(200);
        messageLabel.setMinHeight(30);

        HBox messageContainer = new HBox();
        messageContainer.getStyleClass().add("message-container");
        messageContainer.setAlignment(styleClass.equals("user-message") ? javafx.geometry.Pos.CENTER_RIGHT : javafx.geometry.Pos.CENTER_LEFT);
        messageContainer.getChildren().add(messageLabel);
        messageContainer.setMinWidth(200);
        messageContainer.setMinHeight(30);

        Platform.runLater(() -> {
            chatMessages.getChildren().add(messageContainer);
            // Forcer la mise à jour de la hauteur de chatMessages
            chatMessages.requestLayout();
            // Forcer le défilement immédiatement après l'ajout
            scrollPane.setVvalue(1.0);
        });
    }

    private void loadPreviousMessages() {
        // Effacer les messages actuels dans l'interface
        chatMessages.getChildren().clear();

        // Recharger tous les messages depuis l'historique
        for (ChatMessage chatMessage : messageHistory) {
            Label messageLabel = new Label(chatMessage.getText());
            messageLabel.setWrapText(true);
            messageLabel.getStyleClass().add("chat-message");
            messageLabel.getStyleClass().add(chatMessage.getStyleClass());
            messageLabel.setMinWidth(200);
            messageLabel.setMinHeight(30);

            HBox messageContainer = new HBox();
            messageContainer.getStyleClass().add("message-container");
            messageContainer.setAlignment(chatMessage.getStyleClass().equals("user-message") ? javafx.geometry.Pos.CENTER_RIGHT : javafx.geometry.Pos.CENTER_LEFT);
            messageContainer.getChildren().add(messageLabel);
            messageContainer.setMinWidth(200);
            messageContainer.setMinHeight(30);

            chatMessages.getChildren().add(messageContainer);
        }

        // Forcer le défilement jusqu'au dernier message
        Platform.runLater(() -> {
            scrollPane.setVvalue(1.0);
        });
    }
}