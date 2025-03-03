package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AccueilController {

    public Button btnBack;
    public Text Discription;
    UserSession session = UserSession.getInstance();
    @FXML
    private Button btnHotels, btnTickets;
    @FXML
    private Text welcomeText;
    private ResourceBundle bundle;

    @FXML
    private void initialize() {
        bundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
//        updateText();
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), welcomeText);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    public void updateText(){
        btnBack.setText(bundle.getString("btnBack"));
        btnHotels.setText(bundle.getString("manage_hotel"));
        btnTickets.setText(bundle.getString("manage_ticket"));
        Discription.setText(bundle.getString("discription_agent"));
        welcomeText.setText(bundle.getString("welcome_agent"));
    }

    @FXML
    private void goToHotels() {
        loadScene("/tn/esprit/easytripdesktopapp/FXML/Agent/AffichageHotel.fxml", "Gestion des Hôtels");
    }

    @FXML
    private void goToTickets() {
        loadScene("/tn/esprit/easytripdesktopapp/FXML/Agent/AffichageTicket.fxml", "Gestion des Tickets");
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();


            ((Stage) btnHotels.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void hoverIn(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #00f2fe; -fx-text-fill: #4facfe; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    @FXML
    private void hoverOut(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #4facfe; -fx-text-fill: white; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    public void goBack(ActionEvent actionEvent) {
        Stage stage;
        Scene scene;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(getClass().getResource(
                    "/tn/esprit/easytripdesktopapp/FXML/Agent/Dashboard.fxml"),
            bundle);
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(bundle.getString(session.getUser().getRole().toLowerCase() + "_dashboard"));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}