package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceHotel implements CRUDService<Hotel> {
    private final Connection cnx;

    public ServiceHotel() {
        cnx = MyDataBase.getInstance().getCnx();
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

            // Gestion des valeurs null pour promotion_id
            if (hotel.getPromotion() != null) {
                pstm.setInt(10, hotel.getPromotion().getId());
            } else {
                pstm.setNull(10, java.sql.Types.INTEGER);
            }

            // Gestion des valeurs null pour agency_id
            if (hotel.getAgence() != null) {
                pstm.setInt(11, hotel.getAgence().getId());
            } else {
                pstm.setNull(11, java.sql.Types.INTEGER);
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
                h.setPromotionId(rs.getInt("promotion_id"));
                h.setUserId(rs.getInt("user_id"));

                if (!rs.wasNull()) {
                    Promotion promotion = new ServicePromotion().getById(rs.getInt("promotion_id"));
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
                pstm.setNull(10, java.sql.Types.INTEGER);
            }

            if (hotel.getAgence() != null) {
                pstm.setInt(11, hotel.getAgence().getId());
            } else {
                pstm.setNull(11, java.sql.Types.INTEGER);
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

    @Override
    public Hotel getById(int id) {
        String qry = "SELECT * FROM `hotels` WHERE `id_hotel` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
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
                hotel.setPromotionId(rs.getInt("promotion_id"));
                hotel.setUserId(rs.getInt("user_id"));

                // Charger la promotion si elle existe
                int promotionId = rs.getInt("promotion_id");
                if (!rs.wasNull()) {
                    Promotion promotion = new ServicePromotion().getById(promotionId);
                    hotel.setPromotion(promotion);
                }

                System.out.println("Hôtel récupéré avec succès : " + hotel.getName() + " (ID: " + hotel.getId() + ")");
                return hotel;
            } else {
                System.out.println("Aucun hôtel trouvé avec l'ID : " + id);
                return null; // Retourner null si l'hôtel n'existe pas
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'hôtel avec l'ID " + id + " : " + e.getMessage());
            return null;
        }
    }

    public List<Hotel> getByUserId(int user_id) {
        String qry = "SELECT * FROM `hotels` WHERE `user_id` = ?";
        List<Hotel> hotels = new ArrayList<>();

        try {
            PreparedStatement pst = cnx.prepareStatement(qry);
            pst.setInt(1, user_id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
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
                hotel.setPromotionId(rs.getInt("promotion_id"));
                hotel.setUserId(rs.getInt("user_id"));

                // Load promotion if applicable
                if (!rs.wasNull()) {
                    Promotion promotion = new ServicePromotion().getById(rs.getInt("promotion_id"));
                    hotel.setPromotion(promotion);
                }

                hotels.add(hotel); // Add hotel to the list
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des hôtels par user_id : " + e.getMessage());
        }

        return hotels;
    }

    @Override
    public List<Hotel> search(String keyword) {
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
}