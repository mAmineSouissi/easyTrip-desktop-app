package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceUser implements CRUDService<User> {
    private final Connection cnx;

    public ServiceUser() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(User user) {
        String qry = "INSERT INTO `User`(`name`, `surname`, `password`, `email`, `phone`, `addresse`, `profilePhoto`, `role`) VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getSurname());
            pstm.setString(3, user.getPassword()); // Need of hashing the password before storing
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

    public User authenticate(String email, String password) {
        String qry = "SELECT * FROM `User` WHERE `email`=? AND `password`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, email);
            pstm.setString(2, password); // Hash password before comparing

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("addresse"));
                user.setProfilePhoto(rs.getString("profilePhoto"));
                user.setRole(rs.getString("role"));

                return user;
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }


    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String qry = "SELECT * FROM `User`";

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
                System.out.println(" Users Added...");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

    @Override
    public void update(User user) {
        String qry = "UPDATE `User` SET `name`=?, `surname`=?, `password`=?, `email`=?, `phone`=?, `addresse`=?, `profilePhoto`=?, `role`=? WHERE `id`=?";
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
        String qry = "DELETE FROM `User` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, user.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<User> getById(int id) {
        String qry = "SELECT * FROM `User` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("addresse"));
                user.setProfilePhoto(rs.getString("profilePhoto"));
                user.setRole(rs.getString("role"));

                return Optional.of(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<User> search(String keyword) {
        List<User> users = new ArrayList<>();
        String qry = "SELECT * FROM `User` WHERE `name` LIKE ? OR `surname` LIKE ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            String searchPattern = "%" + keyword + "%";
            pstm.setString(1, searchPattern);
            pstm.setString(2, searchPattern);

            ResultSet rs = pstm.executeQuery();

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
    public boolean exists(int id) {
        String qry = "SELECT * FROM `User` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public long count() {
        String countQuery = "SELECT COUNT(*) FROM `User`";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(countQuery)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.out.println("Error counting users: " + e.getMessage());
        }
        return 0;
    }
}
