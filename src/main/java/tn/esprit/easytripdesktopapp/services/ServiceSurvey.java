package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Survey;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceSurvey implements CRUDService<Survey> {
    private final Connection cnx;

    public ServiceSurvey() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public Survey getById(int id) {
        List<Survey> surveys = new ArrayList<>();
        String qry = "SELECT * FROM `survey` WHERE `id` = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery(qry);

            if (rs.next()) {
                Survey survey = new Survey();
                survey.setId(rs.getInt("id"));
                survey.setTitle(rs.getString("title"));
                survey.setDescription(rs.getString("description"));
                survey.setCategory(rs.getString("category"));

                surveys.add(survey);

                return survey;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public List<Survey> search(String keyword) {
        List<Survey> surveys = new ArrayList<>();
        String qry="SELECT * FROM `survey` WHERE `title` LIKE ? OR `category` LIKE ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            String searchPattern = "%" + keyword + "%";
            pstm.setString(1, searchPattern);
            pstm.setString(2, searchPattern);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Survey survey = new Survey();
                survey.setId(rs.getInt("id"));
                survey.setTitle(rs.getString("title"));
                survey.setDescription(rs.getString("description"));
                survey.setCategory(rs.getString("category"));
                surveys.add(survey);

                return surveys;
            }

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return List.of();
    }

    @Override
    public boolean exists(int id) {
        String qry = "SELECT * FROM `survey` WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            pstm.execute();
            if (pstm.getResultSet().next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public long count() {
        String countQuery = "SELECT COUNT(*) FROM `survey`";
        try {
            PreparedStatement pstm = cnx.prepareStatement(countQuery);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public void add(Survey survey) {
        String qry = "INSERT INTO `survey`(`title`, `description`, `category`) VALUES (?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, survey.getTitle());
            pstm.setString(2, survey.getDescription());
            pstm.setString(3, survey.getCategory());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Survey> getAll() {
        List<Survey> surveys = new ArrayList<>();
        String qry = "SELECT * FROM `survey`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Survey survey = new Survey();
                survey.setId(rs.getInt("id"));
                survey.setTitle(rs.getString("title"));
                survey.setDescription(rs.getString("description"));
                survey.setCategory(rs.getString("category"));

                surveys.add(survey);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return surveys;
    }

    @Override
    public void update(Survey survey) {
        String qry = "UPDATE `survey` SET `title`=?, `description`=?, `category`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, survey.getTitle());
            pstm.setString(2, survey.getDescription());
            pstm.setString(3, survey.getCategory());
            pstm.setInt(4, survey.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Survey survey) {
        String qry = "DELETE FROM `survey` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, survey.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
