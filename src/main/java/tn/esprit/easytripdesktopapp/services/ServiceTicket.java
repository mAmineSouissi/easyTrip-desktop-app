package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Ticket;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceTicket implements CRUDService<Ticket> {
    private Connection cnx;

    public ServiceTicket() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Ticket ticket) {
        String qry = "INSERT INTO `tickets`(`flight_number`, `airline`, `departure_city`, `arrival_city`, `departure_date`, `departure_time`, `arrival_date`, `arrival_time`, `ticket_class`, `price`, `ticket_type`, `city_image`, `agency_id`, `promotion_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, ticket.getFlightNumber());
            pstm.setString(2, ticket.getAirline());
            pstm.setString(3, ticket.getDepartureCity());
            pstm.setString(4, ticket.getArrivalCity());
            pstm.setString(5, ticket.getDepartureDate());
            pstm.setString(6, ticket.getDepartureTime());
            pstm.setString(7, ticket.getArrivalDate());
            pstm.setString(8, ticket.getArrivalTime());
            pstm.setString(9, ticket.getTicketClass());
            pstm.setFloat(10, ticket.getPrice());
            pstm.setString(11, ticket.getTicketType());
            pstm.setString(12, ticket.getCityImage());
            pstm.setInt(13, ticket.getAgencyId()); // Ajout de l'ID de l'agence
            pstm.setInt(14, ticket.getPromotionId()); // Ajout de l'ID de la promotion
            pstm.executeUpdate();
            System.out.println("Ticket ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du ticket : " + e.getMessage());
        }
    }

    @Override
    public List<Ticket> getAll() {
        List<Ticket> tickets = new ArrayList<>();
        String qry = "SELECT * FROM `tickets`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Ticket t = new Ticket();
                t.setIdTicket(rs.getInt("id_ticket"));
                t.setFlightNumber(rs.getInt("flight_number"));
                t.setAirline(rs.getString("airline"));
                t.setDepartureCity(rs.getString("departure_city"));
                t.setArrivalCity(rs.getString("arrival_city"));
                t.setDepartureDate(rs.getString("departure_date"));
                t.setDepartureTime(rs.getString("departure_time"));
                t.setArrivalDate(rs.getString("arrival_date"));
                t.setArrivalTime(rs.getString("arrival_time"));
                t.setTicketClass(rs.getString("ticket_class"));
                t.setPrice(rs.getFloat("price"));
                t.setTicketType(rs.getString("ticket_type"));
                t.setCityImage(rs.getString("city_image"));
                t.setAgencyId(rs.getInt("agency_id")); // Récupération de l'ID de l'agence
                t.setPromotionId(rs.getInt("promotion_id")); // Récupération de l'ID de la promotion
                tickets.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des tickets : " + e.getMessage());
        }
        return tickets;
    }

    @Override
    public void update(Ticket ticket) {
        String qry = "UPDATE `tickets` SET `flight_number`=?, `airline`=?, `departure_city`=?, `arrival_city`=?, `departure_date`=?, `departure_time`=?, `arrival_date`=?, `arrival_time`=?, `ticket_class`=?, `price`=?, `ticket_type`=?, `city_image`=?, `agency_id`=?, `promotion_id`=? WHERE `id_ticket`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, ticket.getFlightNumber());
            pstm.setString(2, ticket.getAirline());
            pstm.setString(3, ticket.getDepartureCity());
            pstm.setString(4, ticket.getArrivalCity());
            pstm.setString(5, ticket.getDepartureDate());
            pstm.setString(6, ticket.getDepartureTime());
            pstm.setString(7, ticket.getArrivalDate());
            pstm.setString(8, ticket.getArrivalTime());
            pstm.setString(9, ticket.getTicketClass());
            pstm.setFloat(10, ticket.getPrice());
            pstm.setString(11, ticket.getTicketType());
            pstm.setString(12, ticket.getCityImage());
            pstm.setInt(13, ticket.getAgencyId()); // Mise à jour de l'ID de l'agence
            pstm.setInt(14, ticket.getPromotionId()); // Mise à jour de l'ID de la promotion
            pstm.setInt(15, ticket.getIdTicket());
            pstm.executeUpdate();
            System.out.println("Ticket mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du ticket : " + e.getMessage());
        }
    }

    @Override
    public void delete(Ticket ticket) {
        String qry = "DELETE FROM `tickets` WHERE `id_ticket`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, ticket.getIdTicket());
            pstm.executeUpdate();
            System.out.println("Ticket supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du ticket : " + e.getMessage());
        }
    }


}