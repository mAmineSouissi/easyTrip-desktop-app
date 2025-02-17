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
        String qry = "INSERT INTO `Reclamation`(`user_id`, `issue`, `category`, `status`, `date`) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, reclamation.getuser_id());
            pstm.setString(2, reclamation.getIssue());
            pstm.setString(3, reclamation.getCategory());
            pstm.setString(4, reclamation.getStatus());
            pstm.setDate(5, reclamation.getDate());

            pstm.executeUpdate();
            System.out.println("Reclamation ajoutée avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }




    @Override
    public List<Reclamation> getAll() {
        List<Reclamation> reclamations = new ArrayList<>();
        String qry = "SELECT * FROM Reclamation";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setuser_id(rs.getInt("user_id"));
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
        String qry = "UPDATE Reclamation SET user_id=?, status=?, date=?, issue=?, category=? WHERE id=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, reclamation.getuser_id());
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
        String qry = "DELETE FROM Reclamation WHERE id=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, reclamation.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<Reclamation> getById(int id) {
        String qry = "SELECT * FROM Reclamation WHERE id = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setuser_id(rs.getInt("user_id"));
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
        String qry = "SELECT * FROM Reclamation WHERE issue LIKE ? OR category LIKE ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            String searchPattern = "%" + keyword + "%";
            pstm.setString(1, searchPattern);
            pstm.setString(2, searchPattern);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setuser_id(rs.getInt("user_id"));
                reclamation.setStatus(rs.getString("status"));
                reclamation.setDate(rs.getDate("date"));
                reclamation.setIssue(rs.getString("issue"));
                reclamation.setCategory(rs.getString("category"));

                reclamations.add(reclamation);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reclamations;
    }

    @Override
    public boolean exists(int id) {
        String qry = "SELECT 1 FROM Reclamation WHERE id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
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
        String countQuery = "SELECT COUNT(*) FROM Reclamation";
        try (PreparedStatement pstm = cnx.prepareStatement(countQuery)) {
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
