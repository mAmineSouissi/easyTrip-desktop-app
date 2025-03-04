package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Webinaire;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceWebinaire implements CRUDService<Webinaire> {
    private Connection cnx;

    public ServiceWebinaire() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Webinaire webinaire) {
        String qry = "INSERT INTO `webinaire`(`title`, `description`, `debutDateTime`, `finitDateTime`, `link`, `hotel_id`, `room_id`) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, webinaire.getTitle());
            pstm.setString(2, webinaire.getDescription());
            pstm.setTimestamp(3, Timestamp.valueOf(webinaire.getDebutDateTime()));
            pstm.setTimestamp(4, Timestamp.valueOf(webinaire.getFinitDateTime()));
            pstm.setString(5, webinaire.getLink());
            pstm.setInt(6, webinaire.getHotel().getId());
            pstm.setString(7, webinaire.getRoomId()); // Sauvegarder le roomId
            pstm.executeUpdate();
            System.out.println("Webinaire ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du webinaire : " + e.getMessage());
        }
    }

    @Override
    public List<Webinaire> getAll() {
        List<Webinaire> webinaires = new ArrayList<>();
        String qry = "SELECT * FROM `webinaire`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Webinaire w = new Webinaire();
                w.setId(rs.getInt("id"));
                w.setTitle(rs.getString("title"));
                w.setDescription(rs.getString("description"));
                w.setDebutDateTime(rs.getTimestamp("debutDateTime").toLocalDateTime());
                w.setFinitDateTime(rs.getTimestamp("finitDateTime").toLocalDateTime());
                w.setLink(rs.getString("link"));
                w.setHotel(new ServiceHotel().getById(rs.getInt("hotel_id")));
                w.setRoomId(rs.getString("room_id")); // Charger le roomId
                webinaires.add(w);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des webinaires : " + e.getMessage());
        }
        return webinaires;
    }

    @Override
    public void update(Webinaire webinaire) {
        String qry = "UPDATE `webinaire` SET `title`=?, `description`=?, `debutDateTime`=?, `finitDateTime`=?, `link`=?, `hotel_id`=?, `room_id`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, webinaire.getTitle());
            pstm.setString(2, webinaire.getDescription());
            pstm.setTimestamp(3, Timestamp.valueOf(webinaire.getDebutDateTime()));
            pstm.setTimestamp(4, Timestamp.valueOf(webinaire.getFinitDateTime()));
            pstm.setString(5, webinaire.getLink());
            pstm.setInt(6, webinaire.getHotel().getId());
            pstm.setString(7, webinaire.getRoomId()); // Mettre à jour le roomId
            pstm.setInt(8, webinaire.getId());
            pstm.executeUpdate();
            System.out.println("Webinaire mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du webinaire : " + e.getMessage());
        }
    }

    @Override
    public void delete(Webinaire webinaire) {
        String qry = "DELETE FROM `webinaire` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, webinaire.getId());
            pstm.executeUpdate();
            System.out.println("Webinaire supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du webinaire : " + e.getMessage());
        }
    }

    @Override
    public Webinaire getById(int id) {
        return null;
    }

    @Override
    public List<Webinaire> search(String keyword) {
        return List.of();
    }

    @Override
    public boolean exists(int id) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    /**
     * Supprime les webinaires dont la date de fin est passée.
     */
    public void deleteExpiredWebinaires() {
        String qry = "DELETE FROM `webinaire` WHERE `finitDateTime` < NOW()";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.executeUpdate();
            System.out.println("Webinaires expirés supprimés avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des webinaires expirés : " + e.getMessage());
        }
    }
}