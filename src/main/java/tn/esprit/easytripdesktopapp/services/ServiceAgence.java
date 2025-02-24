package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceAgence implements CRUDService<Agence> {

    private Connection cnx ;

    public ServiceAgence(){
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Agence agence) {
        String qry = "INSERT INTO `Agency`(`name`, `address`, `phone`, `email`) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, agence.getNom());
            pstm.setString(2, agence.getAddress());
            pstm.setString(3, agence.getPhone());
            pstm.setString(4, agence.getEmail());

            pstm.executeUpdate();
            System.out.println("Agence ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Agence> getAll() {
        List<Agence> agences = new ArrayList<>();
        String qry = "SELECT * FROM `Agency`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Agence agence = new Agence();
                agence.setId(rs.getInt("id"));
                agence.setNom(rs.getString("name"));
                agence.setAddress(rs.getString("address"));
                agence.setPhone(rs.getString("phone"));
                agence.setEmail(rs.getString("email"));

                agences.add(agence);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return agences;
    }

    @Override
    public void update(Agence agence) {
        String qry = "UPDATE `Agency` SET `name`=?, `address`=?, `phone`=?, `email`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, agence.getNom());
            pstm.setString(2, agence.getAddress());
            pstm.setString(3, agence.getPhone());
            pstm.setString(4, agence.getEmail());
            pstm.setInt(5, agence.getId());

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
        String qry = "DELETE FROM `Agency` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
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









}
