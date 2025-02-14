package tn.esprit.easytripdesktopapp.services;

import tn.esprit.Interfaces.IService;
import tn.esprit.Models.OfferTravel;
import tn.esprit.Utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffer implements IService<OfferTravel> {

    private Connection cnx ;

    public ServiceOffer(){
        cnx = DB.getInstance().getCnx();
    }

    @Override
    public void add(OfferTravel offer) {
        String qry = "INSERT INTO `offer`(`departure`, `destination`, `departure_date`, `arrival_date`, `hotelName`, `flightName`, `discription`, `price`, `agency_id`, `promotion_id`) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, offer.getDeparture());
            pstm.setString(2, offer.getDestination());
            pstm.setDate(3, new Date(offer.getDeparture_date().getTime()));
            pstm.setDate(4, new Date(offer.getArrival_date().getTime()));
            pstm.setString(5, offer.getHotelName());
            pstm.setString(6, offer.getFlightName());
            pstm.setString(7, offer.getDiscription());
            pstm.setFloat(8, offer.getPrice());
            pstm.setInt(9, offer.getAgency_id());
            pstm.setInt(10, offer.getPromotion_id());

            pstm.executeUpdate();
            System.out.println("Offre ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<OfferTravel> getAll() {
        List<OfferTravel> offers = new ArrayList<>();
        String qry = "SELECT * FROM `offer`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                OfferTravel offer = new OfferTravel();
                offer.setId(rs.getInt("id"));
                offer.setDeparture(rs.getString("departure"));
                offer.setDestination(rs.getString("destination"));
                offer.setDeparture_date(rs.getDate("departure_date"));
                offer.setArrival_date(rs.getDate("arrival_date"));
                offer.setHotelName(rs.getString("hotelName"));
                offer.setFlightName(rs.getString("flightName"));
                offer.setDiscription(rs.getString("discription"));
                offer.setPrice(rs.getFloat("price"));
                offer.setAgency_id(rs.getInt("agency_id"));
                offer.setPromotion_id(rs.getInt("promotion_id"));

                offers.add(offer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return offers;
    }

    @Override
    public void update(OfferTravel offer) {
        String qry = "UPDATE `offer` SET `departure`=?, `destination`=?, `departure_date`=?, `arrival_date`=?, `hotelName`=?, `flightName`=?, `discription`=?, `price`=?, `agency_id`=?, `promotion_id`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, offer.getDeparture());
            pstm.setString(2, offer.getDestination());
            pstm.setDate(3, new Date(offer.getDeparture_date().getTime()));
            pstm.setDate(4, new Date(offer.getArrival_date().getTime()));
            pstm.setString(5, offer.getHotelName());
            pstm.setString(6, offer.getFlightName());
            pstm.setString(7, offer.getDiscription());
            pstm.setFloat(8, offer.getPrice());
            pstm.setInt(9, offer.getAgency_id());
            pstm.setInt(10, offer.getPromotion_id());
            pstm.setInt(11, offer.getId());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Offre mise à jour avec succès !");
            } else {
                System.out.println("Aucune offre trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(OfferTravel offer) {
        String qry = "DELETE FROM `offer` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, offer.getId());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Offre supprimée avec succès !");
            } else {
                System.out.println("Aucune offre trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public List<OfferTravel> getByDestination(String s) {
        return List.of();
    }
}
