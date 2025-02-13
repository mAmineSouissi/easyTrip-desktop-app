package tn.esprit.easytripdesktopapp.services;


import tn.esprit.easytripdesktopapp.models.*;
import tn.esprit.easytripdesktopapp.utils.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServiceReclamation {
    public void create(Reclamation r) {
        String sql = "INSERT INTO reclamations (user_id, issue, category, status, date) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = MyDataBase.getInstance().getCnx();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, r.getUserId());
            ps.setString(2, r.getIssue());
            ps.setString(3, r.getCategory());
            ps.setString(4, r.getStatus());
            ps.setDate(5, r.getDate());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reclamation> readAll() {
        List<Reclamation> list = new ArrayList<>();
        String sql = "SELECT * FROM reclamations";
        try {
            Connection conn = MyDataBase.getInstance().getCnx();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Reclamation r = new Reclamation(
                        rs.getInt("id_reclamation"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getDate("date"),
                        rs.getString("issue"),
                        rs.getString("category"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(Reclamation updated) {
        String sql = "UPDATE reclamations SET user_id=?, issue=?, category=?, status=?, date=? WHERE id_Reclamation=?";
        try {
            Connection conn = MyDataBase.getInstance().getCnx();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, updated.getUserId());
            ps.setString(2, updated.getIssue());
            ps.setString(3, updated.getCategory());
            ps.setString(4, updated.getStatus());
            ps.setDate(5, updated.getDate());
            ps.setInt(6, updated.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM reclamations WHERE id_Reclamation=?";
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
