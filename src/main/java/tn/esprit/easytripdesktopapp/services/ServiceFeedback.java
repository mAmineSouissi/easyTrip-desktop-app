package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.models.Feedback;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceFeedback {

    private Connection cnx;

    public ServiceFeedback() {
        // Récupérer la connexion à la base de données
        cnx = MyDataBase.getInstance().getCnx();
    }

    // Méthode pour récupérer tous les feedbacks
    public List<Feedback> getAll() {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT * FROM feedback";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Feedback feedback = new Feedback(
                        resultSet.getInt("id"),
                        resultSet.getInt("userId"),  // Utilisez "userId" au lieu de "user_id"
                        resultSet.getInt("offerId"), // Utilisez "offerId" au lieu de "offer_id"
                        resultSet.getInt("rating"),
                        resultSet.getString("message"),
                        resultSet.getDate("date")
                );
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    // Méthode pour récupérer les feedbacks par offerId (ID de l'hôtel ou de l'offre)
    public List<Feedback> getFeedbacksByOfferId(int offerId) {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT * FROM feedback WHERE offerId = ?"; // Utilisez "offerId" au lieu de "offer_id"

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {

            preparedStatement.setInt(1, offerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Feedback feedback = new Feedback(
                        resultSet.getInt("id"),
                        resultSet.getInt("userId"),  // Utilisez "userId" au lieu de "user_id"
                        resultSet.getInt("offerId"), // Utilisez "offerId" au lieu de "offer_id"
                        resultSet.getInt("rating"),
                        resultSet.getString("message"),
                        resultSet.getDate("date")
                );
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    // Méthode pour ajouter un feedback
    public void add(Feedback feedback) {
        String query = "INSERT INTO feedback (userId, offerId, rating, message, date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {

            preparedStatement.setInt(1, feedback.getUserId());
            preparedStatement.setInt(2, feedback.getOfferId());
            preparedStatement.setInt(3, feedback.getRating());
            preparedStatement.setString(4, feedback.getMessage());
            preparedStatement.setDate(5, feedback.getDate());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour mettre à jour un feedback
    public void update(Feedback feedback) {
        String query = "UPDATE feedback SET userId = ?, offerId = ?, rating = ?, message = ?, date = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {

            preparedStatement.setInt(1, feedback.getUserId());
            preparedStatement.setInt(2, feedback.getOfferId());
            preparedStatement.setInt(3, feedback.getRating());
            preparedStatement.setString(4, feedback.getMessage());
            preparedStatement.setDate(5, feedback.getDate());
            preparedStatement.setInt(6, feedback.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un feedback
    public void delete(Feedback feedback) {
        String query = "DELETE FROM feedback WHERE id = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {

            preparedStatement.setInt(1, feedback.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}