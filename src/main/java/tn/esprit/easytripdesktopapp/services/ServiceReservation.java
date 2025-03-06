package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.IService;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation implements IService<Reservation> {
    private Connection cnx;

    public ServiceReservation() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Reservation reservation) {
        String qry = "INSERT INTO `Reservation`(`user_id`, `travel_id`, `status`, `orderDate`, `ticket_id`, `hotel_id`, `nom`, `prenom`, `phone`, `email`, `places`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, reservation.getUser_id());
            pstm.setInt(2, reservation.getTravel_id());
            pstm.setString(3, reservation.getStatus());

            if (reservation.getOrderDate() != null) {
                pstm.setDate(4, new java.sql.Date(reservation.getOrderDate().getTime()));
            } else {
                pstm.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            }

            pstm.setInt(5, reservation.getTicket_id());
            pstm.setInt(6, reservation.getHotel_id());
            pstm.setString(7, reservation.getNom());
            pstm.setString(8, reservation.getPrenom());
            pstm.setInt(9, reservation.getPhone());
            pstm.setString(10, reservation.getEmail());
            pstm.setInt(11, reservation.getPlaces());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String qry = "SELECT * FROM `Reservation`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Reservation r = new Reservation();

                r.setId(rs.getInt("id_reservation"));
                r.setUser_id(rs.getInt("user_id"));
                r.setTravel_id(rs.getInt("travel_id"));
                r.setStatus(rs.getString("status"));
                r.setOrderDate(rs.getDate("orderDate"));
                r.setTicket_id(rs.getInt("ticket_id"));
                r.setHotel_id(rs.getInt("hotel_id"));
                r.setNom(rs.getString("nom"));
                r.setPrenom(rs.getString("prenom"));
                r.setPhone(rs.getInt("phone"));
                r.setEmail(rs.getString("email"));
                r.setPlaces(rs.getInt("places"));

                reservations.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }

    public float getOfferPrice(int offerId) {
        String qry = "SELECT price FROM offer_travel WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(qry)) {
            ps.setInt(1, offerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("price");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public float getHotelPrice(int hotelId) {
        String qry = "SELECT price FROM hotels WHERE id_hotel = ?";
        try (PreparedStatement ps = cnx.prepareStatement(qry)) {
            ps.setInt(1, hotelId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("price");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public float getTicketPrice(int ticketId) {
        String qry = "SELECT price FROM tickets WHERE id_ticket = ?";
        try (PreparedStatement ps = cnx.prepareStatement(qry)) {
            ps.setInt(1, ticketId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("price");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }



    @Override
    public void delete(int id) {
        String qry = "DELETE FROM `Reservation` WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(qry)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Reservation reservation) {
        String qry = "UPDATE `Reservation` SET `user_id`=?, `travel_id`=?, `status`=?, `orderDate`=?, `ticket_id`=?, `hotel_id`=?, `nom`=?, `prenom`=?, `phone`=?, `email`=?, `places`=? WHERE id_reservation=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, reservation.getUser_id());
            pstm.setInt(2, reservation.getTravel_id());
            pstm.setString(3, reservation.getStatus());
            pstm.setDate(4, new java.sql.Date(reservation.getOrderDate().getTime()));
            pstm.setInt(5, reservation.getTicket_id());
            pstm.setInt(6, reservation.getHotel_id());
            pstm.setString(7, reservation.getNom());
            pstm.setString(8, reservation.getPrenom());
            pstm.setInt(9, reservation.getPhone());
            pstm.setString(10, reservation.getEmail());
            pstm.setInt(11, reservation.getPlaces());
            pstm.setInt(12, reservation.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Reservation getById(int id) {
        String qry = "SELECT * FROM `Reservation` WHERE id = ?";
        Reservation r = null;
        try (PreparedStatement ps = cnx.prepareStatement(qry)) {
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                r = new Reservation();
                r.setId(res.getInt("id"));
                r.setUser_id(res.getInt("user_id"));
                r.setTravel_id(res.getInt("travel_id"));
                r.setStatus(res.getString("status"));
                r.setOrderDate(res.getDate("orderDate"));
                r.setTicket_id(res.getInt("ticket_id"));
                r.setHotel_id(res.getInt("hotel_id"));
                r.setNom(res.getString("nom"));
                r.setPrenom(res.getString("prenom"));
                r.setPhone(res.getInt("phone"));
                r.setEmail(res.getString("email"));
                r.setPlaces(res.getInt("places"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    public void addWithTicketOnly(Reservation reservation) {
        String qry = "INSERT INTO `Reservation`(`user_id`, `ticket_id`, `nom`, `prenom`, `phone`, `email`, `places`, `status`, `orderDate`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, reservation.getUser_id());
            pstm.setInt(2, reservation.getTicket_id());
            pstm.setString(3, reservation.getNom());
            pstm.setString(4, reservation.getPrenom());
            pstm.setInt(5, reservation.getPhone());
            pstm.setString(6, reservation.getEmail());
            pstm.setInt(7, reservation.getPlaces());

            // Set a default status if none is provided
            if (reservation.getStatus() == null || reservation.getStatus().isEmpty()) {
                pstm.setString(8, "pending");
            } else {
                pstm.setString(8, reservation.getStatus());
            }

            // Set current date if none is provided
            if (reservation.getOrderDate() != null) {
                pstm.setDate(9, new java.sql.Date(reservation.getOrderDate().getTime()));
            } else {
                pstm.setDate(9, new java.sql.Date(System.currentTimeMillis()));
            }

            pstm.executeUpdate();
            System.out.println("Reservation added successfully with ticket only");
        } catch (SQLException e) {
            System.out.println("Error adding reservation: " + e.getMessage());
        }
    }
}