package tn.esprit.easytripdesktopapp.services;


import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.*;
import tn.esprit.easytripdesktopapp.utils.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ServiceReclamation implements CRUDService<Reclamation> {

    private final Connection cnx;

    public ServiceReclamation() {
        cnx = MyDataBase.getInstance().getCnx();
    }


    @Override
    public void add(Reclamation reclamation) {
        String qry = "INSERT INTO reclamation (userId, status, date, issue, category) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, reclamation.getUserId());
            pstm.setString(2, reclamation.getStatus());
            pstm.setDate(3, reclamation.getDate());
            pstm.setString(4, reclamation.getIssue());
            pstm.setString(5, reclamation.getCategory());

            pstm.executeUpdate();
            System.out.println("Reclamation ajoutée avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

    }

    @Override
    public List<Reclamation> getAll() {
        List<Reclamation> reclamations = new ArrayList<>();
        String qry = "SELECT * FROM reclamation";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setUserId(rs.getInt("userId"));
                reclamation.setStatus(rs.getString("status"));
                reclamation.setDate(rs.getDate("date"));
                reclamation.setIssue(rs.getString("issue"));
                reclamation.setCategory(rs.getString("category"));

                reclamations.add(reclamation);
            }

        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        return reclamations;
    }

    @Override
    public void update(Reclamation reclamation) {
        String qry = "UPDATE `reclamation` SET `userId`=?, `status`=?, `date`=?, `issue`=?, `category`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, reclamation.getUserId());
            pstm.setString(2, reclamation.getStatus());
            pstm.setDate(3, reclamation.getDate());
            pstm.setString(4, reclamation.getIssue());
            pstm.setString(5, reclamation.getCategory());
            pstm.setInt(6, reclamation.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void delete(Reclamation reclamation) {
        String qry = "DELETE FROM `reclamation` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, reclamation.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public Optional<Reclamation> getById(int id) {
        String qry = "SELECT * FROM `reclamation` WHERE `id` = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setUserId(rs.getInt("userId"));
                reclamation.setStatus(rs.getString("status"));
                reclamation.setDate(rs.getDate("date"));
                reclamation.setIssue(rs.getString("issue"));
                reclamation.setCategory(rs.getString("category"));

                return Optional.of(reclamation);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Reclamation> search(String keyword) {
        List<Reclamation> reclamations = new ArrayList<>();
        String qry = "SELECT * FROM `reclamation` WHERE `issue` LIKE ? OR `category` LIKE ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            String searchPattern = "%" + keyword + "%";
            pstm.setString(1, searchPattern);
            pstm.setString(2, searchPattern);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setUserId(rs.getInt("userId"));
                reclamation.setStatus(rs.getString("status"));
                reclamation.setDate(rs.getDate("date"));
                reclamation.setIssue(rs.getString("issue"));
                reclamation.setCategory(rs.getString("category"));

                reclamations.add(reclamation);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return reclamations;
    }

    @Override
    public boolean exists(int id) {
        String qry = "SELECT 1 FROM `reclamation` WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public long count() {
        String countQuery = "SELECT COUNT(*) FROM `reclamation`";
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
