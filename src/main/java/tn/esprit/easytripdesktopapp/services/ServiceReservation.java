package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceReservation implements CRUDService<Reservation> {
    private final Connection cnx;

    public ServiceReservation() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Reservation reservation) {
        String qry = "INSERT INTO `reservation`(`ordre_date`, `nom`, `prenom`, `phone`, `email`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDate(1, new java.sql.Date(reservation.getOrdreDate().getTime()));
            pstm.setString(2, reservation.getNom());
            pstm.setString(3, reservation.getPrenom());
            pstm.setInt(4, reservation.getPhone());
            pstm.setString(5, reservation.getEmail());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String qry = "SELECT * FROM `reservation`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setIdReservation(rs.getInt("id_reservation"));
                r.setOrdreDate(rs.getDate("ordre_date"));
                r.setNom(rs.getString("nom"));
                r.setPrenom(rs.getString("prenom"));
                r.setPhone(rs.getInt("phone"));
                r.setEmail(rs.getString("email"));
                reservations.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }

    @Override
    public void update(Reservation reservation) {
        String qry = "UPDATE `reservation` SET `ordre_date`=?, `nom`=?, `prenom`=?, `phone`=?, `email`=? WHERE id_reservation=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDate(1, new java.sql.Date(reservation.getOrdreDate().getTime()));
            pstm.setString(2, reservation.getNom());
            pstm.setString(3, reservation.getPrenom());
            pstm.setInt(4, reservation.getPhone());
            pstm.setString(5, reservation.getEmail());
            pstm.setInt(6, reservation.getIdReservation());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Reservation reservation) {
        String qry = "DELETE FROM `reservation` WHERE id_reservation = ?";
        try {
            PreparedStatement pstmt = cnx.prepareStatement(qry);
            pstmt.setInt(1, reservation.getIdReservation());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<Reservation> getById(int id) {
        String qry = "SELECT * FROM `reservation` WHERE id_reservation = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setIdReservation(rs.getInt("id_reservation"));
                reservation.setOrdreDate(rs.getDate("ordre_date"));
                reservation.setNom(rs.getString("nom"));
                reservation.setPrenom(rs.getString("prenom"));
                reservation.setPhone(rs.getInt("phone"));
                reservation.setEmail(rs.getString("email"));
                return Optional.of(reservation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> search(String keyword) {
        List<Reservation> reservations = new ArrayList<>();
        String qry = "SELECT * FROM `reservation` WHERE `nom` LIKE ? OR `prenom` LIKE ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            String searchPattern = "%" + keyword + "%";
            pstm.setString(1, searchPattern);
            pstm.setString(2, searchPattern);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setIdReservation(rs.getInt("id_reservation"));
                r.setOrdreDate(rs.getDate("ordre_date"));
                r.setNom(rs.getString("nom"));
                r.setPrenom(rs.getString("prenom"));
                r.setPhone(rs.getInt("phone"));
                r.setEmail(rs.getString("email"));
                reservations.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }

    @Override
    public boolean exists(int id) {
        String qry = "SELECT 1 FROM `reservation` WHERE id_reservation = ?";
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
        String qry = "SELECT COUNT(*) FROM `reservation`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.out.println("Error counting reservations: " + e.getMessage());
        }
        return 0;
    }
}