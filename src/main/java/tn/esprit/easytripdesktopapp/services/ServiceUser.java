package tn.esprit.easytripdesktopapp.services;


import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements CRUDService<User> {
    private final Connection cnx;

    public ServiceUser() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(User user) {
        String qry = "INSERT INTO `user`(`name`, `surname`, `password`, `email`, `phone`, `addresse`, `profilePhoto`, `role`) VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getSurname());
            pstm.setString(3, user.getPassword());
            pstm.setString(4, user.getEmail());
            pstm.setString(5, user.getPhone());
            pstm.setString(6, user.getAddress());
            pstm.setString(7, user.getProfilePhoto());
            pstm.setString(8, user.getRole());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String qry = "SELECT * FROM `user`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("addresse"));
                user.setProfilePhoto(rs.getString("profilePhoto"));
                user.setRole(rs.getString("role"));

                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

    @Override
    public void update(User user) {
        String qry = "UPDATE `user` SET `name`=?, `surname`=?, `password`=?, `email`=?, `phone`=?, `addresse`=?, `profilePhoto`=?, `role`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getSurname());
            pstm.setString(3, user.getPassword());
            pstm.setString(4, user.getEmail());
            pstm.setString(5, user.getPhone());
            pstm.setString(6, user.getAddress());
            pstm.setString(7, user.getProfilePhoto());
            pstm.setString(8, user.getRole());
            pstm.setInt(9, user.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(User user) {
        String qry = "DELETE FROM `user` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, user.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
