package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Survey;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class ServiceSurvey implements CRUDService<Survey> {
    private final Connection cnx;

    public ServiceSurvey() {
        cnx = MyDataBase.getInstance().getCnx();
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
