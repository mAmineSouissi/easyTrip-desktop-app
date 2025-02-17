package tn.esprit.easytripdesktopapp.services;


import tn.esprit.easytripdesktopapp.interfaces.IService;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ServiceReservation implements IService<Reservation> {
    private Connection cnx ;

    public ServiceReservation(){
        cnx = MyDataBase.getInstance().getCnx();
    }


    @Override
    public void add(Reservation reservation) {
        String qry ="INSERT INTO `reservation`(`ordreDate`, `nom`, `prenom`, `phone`, `email`) VALUES (?,?,?,?,?)";
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
                r.setOrdreDate(rs.getDate(("ordreDate")));
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
    public void delete(int id) {
        String qry = "DELETE FROM `reservation` WHERE id_reservation =?";
        try (PreparedStatement pstmt = cnx.prepareStatement(qry)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {System.out.println(e.getMessage());}
    }


    @Override
    public void update(Reservation reservation) {
        String qry = "UPDATE `reservation` SET  `ordreDate`=?, `nom`=?, `prenom`=?, `phone`=?,`email`=?  WHERE id_reservation =?";
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

    public Reservation getById(int id) {
        String qry = "SELECT * FROM `reservation` WHERE id_reservation = ?";
        Reservation c = null;
        try (PreparedStatement ps = cnx.prepareStatement(qry)) {
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                Date ordreDate = res.getDate("ordreDate");
                String nom = res.getString("nom");
                String prenom = res.getString("prenom");
                int phone = res.getInt("phone");
                String email = res.getString("email");
                c = new Reservation(id, ordreDate, nom, prenom, phone, email);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return c;
    }


}
