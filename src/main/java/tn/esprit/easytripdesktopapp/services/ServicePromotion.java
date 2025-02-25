package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;
import tn.esprit.easytripdesktopapp.models.Promotion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicePromotion implements CRUDService<Promotion> {

    private final Connection cnx;

    public ServicePromotion() {
        cnx = MyDataBase.getInstance().getCnx();
    }


    @Override
    public void add(Promotion promotion) {
        String qry = "INSERT INTO `promotion`(`title`, `description`, `discount_percentage`, `valid_until`) VALUES (?,?,?,?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, promotion.getTitle());
            pstm.setString(2, promotion.getDescription());
            pstm.setFloat(3, promotion.getDiscount_percentage());
            pstm.setDate(4, new java.sql.Date(promotion.getValid_until().getTime()));

            pstm.executeUpdate();
            System.out.println("Promotion ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Promotion> getAll() {
        List<Promotion> promotions = new ArrayList<>();
        String qry = "SELECT * FROM `promotion`";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                Promotion promotion = new Promotion();
                promotion.setId(rs.getInt("id"));
                promotion.setTitle(rs.getString("title"));
                promotion.setDescription(rs.getString("description"));
                promotion.setDiscount_percentage(rs.getFloat("discount_percentage"));
                promotion.setValid_until(rs.getDate("valid_until"));

                promotions.add(promotion);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return promotions;
    }

    @Override
    public void update(Promotion promotion) {
        String qry = "UPDATE `promotion` SET `title`=?, `description`=?, `discount_percentage`=?, `valid_until`=? WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, promotion.getTitle());
            pstm.setString(2, promotion.getDescription());
            pstm.setFloat(3, promotion.getDiscount_percentage());
            pstm.setDate(4, new java.sql.Date(promotion.getValid_until().getTime()));
            pstm.setInt(5, promotion.getId());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Promotion mise à jour avec succès !");
            } else {
                System.out.println("Aucune promotion trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Promotion promotion) {
        String qry = "DELETE FROM `promotion` WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, promotion.getId());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Promotion supprimée avec succès !");
            } else {
                System.out.println("Aucune promotion trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Promotion getById(int id) {
        String qry = "SELECT * FROM `promotion` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                Promotion p = new Promotion();
                p.setId(rs.getInt("id"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString("description"));
                p.setDiscount_percentage(rs.getFloat("discount_percentage"));
                p.setValid_until(rs.getDate("valid_until"));
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la promotion par ID : " + e.getMessage());
        }
        return null;
    }


    public Optional<Promotion> getByid(int id) {
        String qry = "SELECT * FROM `promotion` WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Promotion promotion = new Promotion();
                promotion.setId(rs.getInt("id"));
                promotion.setTitle(rs.getString("title"));
                promotion.setDescription(rs.getString("description"));
                promotion.setDiscount_percentage(rs.getFloat("discount_percentage"));
                promotion.setValid_until(rs.getDate("valid_until"));

                ServiceOfferTravel serviceOffer = new ServiceOfferTravel();
                List<OfferTravel> offers = serviceOffer.getOffresByPromotionId(id);

                promotion.setOfferTravels(offers);
                return Optional.of(promotion);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Promotion> search(String keyword) {
        List<Promotion> promotions = new ArrayList<>();
        String qry = "SELECT * FROM `promotion` WHERE `title` LIKE ? OR `description` LIKE ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            String likeKeyword = "%" + keyword + "%";
            pstm.setString(1, likeKeyword);
            pstm.setString(2, likeKeyword);

            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Promotion promotion = new Promotion();
                promotion.setId(rs.getInt("id"));
                promotion.setTitle(rs.getString("title"));
                promotion.setDescription(rs.getString("description"));
                promotion.setDiscount_percentage(rs.getFloat("discount_percentage"));
                promotion.setValid_until(rs.getDate("valid_until"));

                promotions.add(promotion);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return promotions;
    }

    @Override
    public boolean exists(int id) {
        String qry = "SELECT COUNT(*) FROM `promotion` WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public long count() {
        String qry = "SELECT COUNT(*) FROM `promotion`";
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }


    public Promotion getByTitle(String title) {
        String qry = "SELECT * FROM `promotion` WHERE `title`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, title);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                Promotion p = new Promotion();
                p.setId(rs.getInt("id"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString("description"));
                p.setDiscount_percentage(rs.getFloat("discount_percentage"));
                p.setValid_until(rs.getDate("valid_until"));
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la promotion par titre : " + e.getMessage());
        }
        return null;
    }


}