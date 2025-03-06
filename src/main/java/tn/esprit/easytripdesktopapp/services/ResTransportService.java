package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.utils.MyDataBase;
import tn.esprit.easytripdesktopapp.models.Res_Transport;


import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResTransportService {
    Connection cnx = MyDataBase.getInstance().getCnx();
    // Method to add a new reservation
    public void addResTransport(int userId, int carId, Date startDate, Date endDate, Res_Transport.Status status) {
        String req = "INSERT INTO `res_transport`(`user_id`, `car_id`, `start_date`, `end_date`, `status`) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            // Set the parameters for the SQL query
            pst.setInt(1, userId);
            pst.setInt(2, carId);
            pst.setDate(3, (java.sql.Date) startDate);
            pst.setDate(4, (java.sql.Date) endDate);
            pst.setString(5, status.toString()); // Convert enum to string

            // Execute the query
            pst.executeUpdate();
            System.out.println("Reservation added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding reservation: " + e.getMessage());
        }
    }

    public List<Res_Transport> getAllResTransport() throws SQLException{

        String req = "SELECT * FROM `res_transport`";
        List<Res_Transport> resTransportList = new ArrayList<>();

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Res_Transport resTransport = new Res_Transport();
                resTransport.setId(rs.getInt("id"));
                resTransport.setUserId(rs.getInt("user_id"));
                resTransport.setCarId(rs.getInt("car_id"));
                resTransport.setStartDate(rs.getDate("start_date"));
                resTransport.setEndDate(rs.getDate("end_date"));

                // Convert the status string to the enum
                String statusString = rs.getString("status");
                if (statusString != null) {
                    resTransport.setStatus(Res_Transport.Status.valueOf(statusString));
                }

                resTransportList.add(resTransport);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reservations: " + e.getMessage());
            throw e; // Re-throw the exception for handling in the calling method
        }

        return resTransportList;
    }
    public void DeleteResTransport(int id){
        String req = "DELETE FROM `res_transport` WHERE `id` = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id); // Set the reservation ID
            pst.executeUpdate();
            System.out.println("Reservation deleted successfully!");
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
        }
    }
    public void UpdateResTransport (Res_Transport resTransport){
        String req = "UPDATE `res_transport` SET `start_date` = ?, `end_date` = ?, `status` = ? WHERE `id` = ? AND `user_id` = ? AND `car_id` = ?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setDate(1, (java.sql.Date) resTransport.getStartDate());
            pst.setDate(2, (java.sql.Date) resTransport.getEndDate());
            pst.setString(3, resTransport.getStatus().toString()); // Convert enum to string
            pst.setInt(4, resTransport.getId()); // ✅ Set ID
            pst.setInt(5, resTransport.getUserId()); // ✅ Set User ID
            pst.setInt(6, resTransport.getCarId()); // ✅ Set Car ID

            pst.executeUpdate();
            System.out.println("Reservation updated successfully!");
        } catch (SQLException e) {
            System.err.println("Error updating reservation: " + e.getMessage());
        }
    }


    }




