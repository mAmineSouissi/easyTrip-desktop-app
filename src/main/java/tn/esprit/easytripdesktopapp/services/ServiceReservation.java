package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ServiceReservation implements CRUDService<Reservation> {
    private Connection cnx ;

    public ServiceReservation(){
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Reservation reservation) {
        //create Qry SQL
        //execute Qry
        String qry ="INSERT INTO `reservation`(`ordre_date`, `nom`, `prenom`, `phone`, `email`) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDate(1, new java.sql.Date(reservation.getOrdreDate().getTime()));
            pstm.setString(2, reservation.getNom());
            pstm.setString(3, reservation.getPrenom());
            pstm.setInt(4, reservation.getPhone());
            pstm.setString(5, reservation.getEmail());


            pstm.executeUpdate();
        } catch (SQLException e) {System.out.println(e.getMessage());}
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String qry ="SELECT * FROM `reservation`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                Reservation r = new Reservation();
                r.setIdReservation(rs.getInt(("id_reservation")));
                r.setNom(rs.getString(("nom")));
                r.setPrenom(rs.getString(("prenom")));
                r.setPhone(rs.getInt(("phone")));
                r.setEmail(rs.getString(("email")));
                reservations.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }

    @Override
    public void delete(Reservation Reservation) {
        String qry = "DELETE FROM `reservation` WHERE id_reservation =?";
        try (PreparedStatement pstmt = cnx.prepareStatement(qry)) {

            pstmt.setInt(1, Reservation.getIdReservation());
            int rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {System.out.println(e.getMessage());}
    }

    @Override
    public Optional<Reservation> getById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Reservation> search(String keyword) {
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


    @Override
    public void update(Reservation reservation) {
        String qry = "UPDATE `reservation` SET  `ordre_date`=?, `nom`=?, `prenom`=?, `phone`=?,`email`=?  WHERE id_reservation =?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setDate(1, new java.sql.Date(reservation.getOrdreDate().getTime()));
            pstm.setString(2, reservation.getNom());
            pstm.setString(3, reservation.getPrenom());
            pstm.setInt(4, reservation.getPhone());
            pstm.setString(5, reservation.getEmail());
            pstm.setInt(6, reservation.getIdReservation());

            int rowsUpdated = pstm.executeUpdate();
        } catch (SQLException e) {System.out.println(e.getMessage());}
    }




}
