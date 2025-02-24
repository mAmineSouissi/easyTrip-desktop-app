package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePromotion {

    private Connection cnx;

    public ServicePromotion() {
        cnx = MyDataBase.getInstance().getCnx(); // Utilisation de la connexion partagée
    }

    /**
     * Ajoute une promotion à la base de données.
     *
     * @param promotion La promotion à ajouter.
     */
    public void add(Promotion promotion) {
        String qry = "INSERT INTO `promotion`(`title`, `description`, `discount_percentage`, `valid_until`) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, promotion.getTitle());
            pstm.setString(2, promotion.getDescription());
            pstm.setFloat(3, promotion.getDiscount_percentage());
            pstm.setDate(4, new java.sql.Date(promotion.getValidUntil().getTime())); // Conversion de java.util.Date en java.sql.Date
            pstm.executeUpdate();
            System.out.println("Promotion ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la promotion : " + e.getMessage());
        }
    }

    /**
     * Récupère toutes les promotions depuis la base de données.
     *
     * @return Une liste de toutes les promotions.
     */
    public List<Promotion> getAll() {
        List<Promotion> promotions = new ArrayList<>();
        String qry = "SELECT * FROM `promotion`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Promotion p = new Promotion();
                p.setId(rs.getInt("id"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString("description"));
                p.setDiscount_percentage(rs.getFloat("discount_percentage"));
                p.setValidUntil(rs.getDate("valid_until")); // Récupération de la date de validité
                promotions.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des promotions : " + e.getMessage());
        }
        return promotions;
    }

    /**
     * Met à jour une promotion dans la base de données.
     *
     * @param promotion La promotion à mettre à jour.
     */
    public void update(Promotion promotion) {
        String qry = "UPDATE `promotion` SET `title`=?, `description`=?, `discount_percentage`=?, `valid_until`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, promotion.getTitle());
            pstm.setString(2, promotion.getDescription());
            pstm.setFloat(3, promotion.getDiscount_percentage());
            pstm.setDate(4, new java.sql.Date(promotion.getValidUntil().getTime())); // Conversion de java.util.Date en java.sql.Date
            pstm.setInt(5, promotion.getId());
            pstm.executeUpdate();
            System.out.println("Promotion mise à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la promotion : " + e.getMessage());
        }
    }

    /**
     * Supprime une promotion de la base de données.
     *
     * @param promotion La promotion à supprimer.
     */
    public void delete(Promotion promotion) {
        String qry = "DELETE FROM `promotion` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, promotion.getId());
            pstm.executeUpdate();
            System.out.println("Promotion supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la promotion : " + e.getMessage());
        }
    }

    /**
     * Récupère une promotion par son ID.
     *
     * @param id L'ID de la promotion à récupérer.
     * @return La promotion correspondante, ou null si non trouvée.
     */
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
                p.setValidUntil(rs.getDate("valid_until"));
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la promotion par ID : " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère une promotion par son titre.
     *
     * @param title Le titre de la promotion à récupérer.
     * @return La promotion correspondante, ou null si non trouvée.
     */
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
                p.setValidUntil(rs.getDate("valid_until"));
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la promotion par titre : " + e.getMessage());
        }
        return null;
    }
}