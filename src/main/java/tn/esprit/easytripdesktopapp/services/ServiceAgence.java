package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServiceAgence implements CRUDService<Agence> {

    private Connection cnx ;

    public ServiceAgence(){
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Agence agence) {
        String qry = "INSERT INTO `agency`(`name`, `address`, `phone`, `email`, `image`) VALUES (?,?,?,?,?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, agence.getNom());
            pstm.setString(2, agence.getAddress());
            pstm.setString(3, agence.getPhone());
            pstm.setString(4, agence.getEmail());
            pstm.setString(5, agence.getImage());
            pstm.executeUpdate();
            System.out.println("Agence ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Agence> getAll() {
        List<Agence> agences = new ArrayList<>();
        String qry = "SELECT * FROM `agency`";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                Agence agence = new Agence();
                agence.setId(rs.getInt("id"));
                agence.setNom(rs.getString("name"));
                agence.setAddress(rs.getString("address"));
                agence.setPhone(rs.getString("phone"));
                agence.setEmail(rs.getString("email"));
                agence.setImage(rs.getString("image"));

                ServiceOfferTravel serviceOffer = new ServiceOfferTravel();
                List<OfferTravel> offres = serviceOffer.getOffresByAgenceId(agence.getId());

                agence.setOfferTravels(offres);

                agences.add(agence);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return agences;
    }

    @Override
    public void update(Agence agence) {
        String qry = "UPDATE `agency` SET `name`=?, `address`=?, `phone`=?, `email`=?, `image`=? WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, agence.getNom());
            pstm.setString(2, agence.getAddress());
            pstm.setString(3, agence.getPhone());
            pstm.setString(4, agence.getEmail());
            pstm.setString(5, agence.getImage());
            pstm.setInt(6, agence.getId());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Agence mise à jour avec succès !");
            } else {
                System.out.println("Aucune agence trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Agence agence) {
        String qry = "DELETE FROM `agency` WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, agence.getId());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Agence supprimée avec succès !");
            } else {
                System.out.println("Aucune agence trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public Optional<Agence> getById(int id) {
        String qry = "SELECT * FROM `agency` WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Agence agence = new Agence();
                agence.setId(rs.getInt("id"));
                agence.setNom(rs.getString("name"));
                agence.setAddress(rs.getString("address"));
                agence.setPhone(rs.getString("phone"));
                agence.setEmail(rs.getString("email"));
                agence.setImage(rs.getString("image"));

                ServiceOfferTravel serviceOffer = new ServiceOfferTravel();
                List<OfferTravel> offres = serviceOffer.getOffresByAgenceId(id);

                agence.setOfferTravels(offres);
                return Optional.of(agence);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Agence> search(String keyword) {
        List<Agence> allAgences = getAll(); // Récupérer toutes les agences
        String lowerCaseKeyword = keyword.toLowerCase(); // Convertir le mot-clé en minuscules pour une recherche insensible à la casse

        return allAgences.stream()
                .filter(agence ->
                        agence.getNom().toLowerCase().contains(lowerCaseKeyword) ||
                                agence.getAddress().toLowerCase().contains(lowerCaseKeyword) ||
                                agence.getEmail().toLowerCase().contains(lowerCaseKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public boolean exists(int id) {
        String qry = "SELECT COUNT(*) FROM `agency` WHERE `id`=?";
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
        String qry = "SELECT COUNT(*) FROM `agency`";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}
