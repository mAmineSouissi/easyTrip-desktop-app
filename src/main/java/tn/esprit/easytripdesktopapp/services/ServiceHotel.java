package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceHotel implements CRUDService<Hotel> {
    private Connection cnx;

    public ServiceHotel() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Hotel hotel) {
        String qry = "INSERT INTO `hotels`(`name`, `adresse`, `city`, `rating`, `description`, `price`, `type_room`, `num_room`, `image`) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, hotel.getName());
            pstm.setString(2, hotel.getAdresse());
            pstm.setString(3, hotel.getCity());
            pstm.setInt(4, hotel.getRating());
            pstm.setString(5, hotel.getDescription());
            pstm.setFloat(6, hotel.getPrice());
            pstm.setString(7, hotel.getTypeRoom());
            pstm.setInt(8, hotel.getNumRoom());
            pstm.setString(9, hotel.getImage());
            pstm.executeUpdate();
            System.out.println("Hôtel ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'hôtel : " + e.getMessage());
        }
    }

    @Override
    public List<Hotel> getAll() {
        List<Hotel> hotels = new ArrayList<>();
        String qry = "SELECT * FROM `hotels`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Hotel h = new Hotel();
                h.setId(rs.getInt("id_hotel"));
                h.setName(rs.getString("name"));
                h.setAdresse(rs.getString("adresse"));
                h.setCity(rs.getString("city"));
                h.setRating(rs.getInt("rating"));
                h.setDescription(rs.getString("description"));
                h.setPrice(rs.getFloat("price"));
                h.setTypeRoom(rs.getString("type_room"));
                h.setNumRoom(rs.getInt("num_room"));
                h.setImage(rs.getString("image"));
                hotels.add(h);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des hôtels : " + e.getMessage());
        }
        return hotels;
    }

    @Override
    public void update(Hotel hotel) {
        String qry = "UPDATE `hotels` SET `name`=?, `adresse`=?, `city`=?, `rating`=?, `description`=?, `price`=?, `type_room`=?, `num_room`=?, `image`=? WHERE `id_hotel`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, hotel.getName());
            pstm.setString(2, hotel.getAdresse());
            pstm.setString(3, hotel.getCity());
            pstm.setInt(4, hotel.getRating());
            pstm.setString(5, hotel.getDescription());
            pstm.setFloat(6, hotel.getPrice());
            pstm.setString(7, hotel.getTypeRoom());
            pstm.setInt(8, hotel.getNumRoom());
            pstm.setString(9, hotel.getImage());
            pstm.setInt(10, hotel.getId()); // ID de l'hôtel à mettre à jour
            pstm.executeUpdate();
            System.out.println("Hôtel mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'hôtel : " + e.getMessage());
        }
    }

    @Override
    public void delete(Hotel hotel) {
        String qry = "DELETE FROM `hotels` WHERE `id_hotel`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, hotel.getId()); // ID de l'hôtel à supprimer
            pstm.executeUpdate();
            System.out.println("Hôtel supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'hôtel : " + e.getMessage());
        }
    }

    @Override
    public Optional<Hotel> getById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public List<Hotel> search(String keyword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public boolean exists(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }
}