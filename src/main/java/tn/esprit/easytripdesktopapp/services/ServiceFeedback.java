package tn.esprit.easytripdesktopapp.services;


import tn.esprit.easytripdesktopapp.models.Feedback;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFeedback {
    public void create(Feedback f) {
        String sql = "INSERT INTO feedbacks (user_id, offer_id, rating, message, date) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = MyDataBase.getInstance().getCnx();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, f.getUserId());
            ps.setInt(2, f.getOfferId());
            ps.setInt(3, f.getRating());
            ps.setString(4, f.getMessage());
            ps.setDate(5, f.getDate());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Feedback> readAll() {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM feedbacks";
        try {
            Connection conn = MyDataBase.getInstance().getCnx();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Feedback f = new Feedback(
                        rs.getInt("id_feedback"),
                        rs.getInt("user_id"),
                        rs.getInt("offer_id"),
                        rs.getInt("rating"),
                        rs.getString("message"),
                        rs.getDate("date"));
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(Feedback f) {
        String sql = "UPDATE feedbacks SET user_id=?, offer_id=?, rating=?, message=?, date=? WHERE id_feedback=?";
        try {
            Connection conn = MyDataBase.getInstance().getCnx();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, f.getUserId());
            ps.setInt(2, f.getOfferId());
            ps.setInt(3, f.getRating());
            ps.setString(4, f.getMessage());
            ps.setDate(5, f.getDate());
            ps.setInt(6, f.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM feedbacks WHERE id_feedback=?";
        try {
            Connection conn = MyDataBase.getInstance().getCnx();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
