package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceHotel implements CRUDService<Hotel> {
    private Connection cnx;

    public ServiceHotel() {
        cnx = MyDataBase.getInstance().getCnx();
    }
    public Hotel getById(int id) {
        String qry = "SELECT * FROM `hotels` WHERE `id_hotel` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id); // Définir l'ID de l'hôtel à rechercher
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Hotel hotel = new Hotel();
                hotel.setId(rs.getInt("id_hotel"));
                hotel.setName(rs.getString("name"));
                hotel.setAdresse(rs.getString("adresse"));
                hotel.setCity(rs.getString("city"));
                hotel.setRating(rs.getInt("rating"));
                hotel.setDescription(rs.getString("description"));
                hotel.setPrice(rs.getFloat("price"));
                hotel.setTypeRoom(rs.getString("type_room"));
                hotel.setNumRoom(rs.getInt("num_room"));
                hotel.setImage(rs.getString("image"));

                // Récupérer la promotion associée (si elle existe)
                int promotionId = rs.getInt("promotion_id");
                if (!rs.wasNull()) { // Vérifier si promotion_id n'est pas NULL
                    Promotion promotion = new ServicePromotion().getById(promotionId);
                    hotel.setPromotion(promotion);
                }



                return hotel; // Retourner l'hôtel trouvé
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'hôtel par ID : " + e.getMessage());
        }
        return null; // Retourner null si aucun hôtel n'est trouvé
    }
    @Override
    public void add(Hotel hotel) {
        String qry = "INSERT INTO `hotels`(`name`, `adresse`, `city`, `rating`, `description`, `price`, `type_room`, `num_room`, `image`, `promotion_id`, `agency_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
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

            // Gestion des valeurs null pour promotion_id et agence_id
            if (hotel.getPromotion() != null) {
                pstm.setInt(10, hotel.getPromotion().getId());
            } else {
                pstm.setNull(10, java.sql.Types.INTEGER); // Définir la valeur NULL pour promotion_id
            }

            if (hotel.getAgence() != null) {
                pstm.setInt(11, hotel.getAgence().getId());
            } else {
                pstm.setNull(11, java.sql.Types.INTEGER); // Définir la valeur NULL pour agence_id
            }

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

                // Récupérer la promotion associée
                int promotionId = rs.getInt("promotion_id");
                if (!rs.wasNull()) { // Vérifier si promotion_id n'est pas NULL
                    Promotion promotion = new ServicePromotion().getById(promotionId); // Récupérer la promotion par son ID
                    h.setPromotion(promotion);
                }

                hotels.add(h);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des hôtels : " + e.getMessage());
        }
        return hotels;
    }

    @Override
    public void update(Hotel hotel) {
        String qry = "UPDATE `hotels` SET `name`=?, `adresse`=?, `city`=?, `rating`=?, `description`=?, `price`=?, `type_room`=?, `num_room`=?, `image`=?, `promotion_id`=?, `agency_id`=? WHERE `id_hotel`=?";
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

            // Gestion des valeurs null pour promotion_id et agence_id
            if (hotel.getPromotion() != null) {
                pstm.setInt(10, hotel.getPromotion().getId());
            } else {
                pstm.setNull(10, java.sql.Types.INTEGER); // Définir la valeur NULL pour promotion_id
            }

            if (hotel.getAgence() != null) {
                pstm.setInt(11, hotel.getAgence().getId());
            } else {
                pstm.setNull(11, java.sql.Types.INTEGER); // Définir la valeur NULL pour agence_id
            }

            pstm.setInt(12, hotel.getId());
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
            pstm.setInt(1, hotel.getId());
            pstm.executeUpdate();
            System.out.println("Hôtel supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'hôtel : " + e.getMessage());
        }
    }
}