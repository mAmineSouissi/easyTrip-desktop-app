package tn.esprit.easytripdesktopapp.services;


import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Feedback;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceFeedback implements CRUDService<Feedback> {

    private final Connection cnx;

    public ServiceFeedback() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Feedback feedback) {
        String qry = "INSERT INTO feedback (userId, offerId, rating, message, date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, feedback.getUserId());
            pstm.setInt(2, feedback.getOfferId());
            pstm.setInt(3, feedback.getRating());
            pstm.setString(4, feedback.getMessage());
            pstm.setDate(5, feedback.getDate());

            pstm.executeUpdate();
            System.out.println("Feedback ajouté avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur" + e.getMessage());
        }
    }

    @Override
    public List<Feedback> getAll() {
        List<Feedback> feedbackList = new ArrayList<>();
        String qry = "SELECT * FROM feedback";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setId(rs.getInt("id"));
                feedback.setUserId(rs.getInt("userId"));
                feedback.setOfferId(rs.getInt("offerId"));
                feedback.setRating(rs.getInt("rating"));
                feedback.setMessage(rs.getString("message"));
                feedback.setDate(rs.getDate("date"));

                feedbackList.add(feedback);
            }

        } catch (SQLException e) {
            System.out.println("Erreur" + e.getMessage());
        }

        return feedbackList;
    }

    @Override
    public void update(Feedback feedback) {
        String qry = "UPDATE `feedback` SET `userId`=?, `offerId`=?, `rating`=?, `message`=?, `date`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, feedback.getUserId());
            pstm.setInt(2, feedback.getOfferId());
            pstm.setInt(3, feedback.getRating());
            pstm.setString(4, feedback.getMessage());
            pstm.setDate(5, feedback.getDate());
            pstm.setInt(6, feedback.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void delete(Feedback feedback) {
        String qry = "DELETE FROM `feedback` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, feedback.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public Optional<Feedback> getById(int id) {
        List<Feedback> feedbacks = new ArrayList<>();
        String qry = "SELECT * FROM `feedback` WHERE `id` = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setId(rs.getInt("id"));
                feedback.setUserId(rs.getInt("user_id"));
                feedback.setOfferId(rs.getInt("offer_id"));
                feedback.setRating(rs.getInt("rating"));
                feedback.setMessage(rs.getString("message"));
                feedback.setDate(rs.getDate("date"));

                feedbacks.add(feedback);

                return Optional.of(feedback);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Feedback> search(String keyword) {
        List<Feedback> feedbacks = new ArrayList<>();
        String qry = "SELECT * FROM `feedback` WHERE `message` LIKE ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            String searchPattern = "%" + keyword + "%";
            pstm.setString(1, searchPattern);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setId(rs.getInt("id"));
                feedback.setUserId(rs.getInt("user_id"));
                feedback.setOfferId(rs.getInt("offer_id"));
                feedback.setRating(rs.getInt("rating"));
                feedback.setMessage(rs.getString("message"));
                feedback.setDate(rs.getDate("date"));

                feedbacks.add(feedback);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return feedbacks;
    }

    @Override
    public boolean exists(int id) {
        String qry = "SELECT * FROM `feedback` WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            pstm.execute();
            if (pstm.getResultSet().next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public long count() {
        String countQuery = "SELECT COUNT(*) FROM `feedback`";
        try {
            PreparedStatement pstm = cnx.prepareStatement(countQuery);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}





