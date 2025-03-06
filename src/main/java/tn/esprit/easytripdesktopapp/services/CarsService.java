package tn.esprit.easytripdesktopapp.services;
import javafx.scene.image.Image;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;
import tn.esprit.easytripdesktopapp.interfaces.ICarsService;
import tn.esprit.easytripdesktopapp.models.cars;
//import tn.esprit.models.Image;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CarsService implements ICarsService<cars> {
    Connection cnx = MyDataBase.getInstance().getCnx();

    @Override
    public void addCars(cars cars) {
        String req = "INSERT INTO `cars`(`model`, `seats`, `location`, `price_per_day`, `image`) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, cars.getModel());
            pst.setInt(2, cars.getSeats());
            pst.setString(3, cars.getLocation());
            pst.setFloat(4, cars.getPrice());
            pst.setString(5, cars.getImage()); // Insert the image URL

            pst.executeUpdate();
            System.out.println("Car added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding car: " + e.getMessage());
        }
    }

    @Override
public void updateCars(cars cars) {
    String req = "UPDATE cars SET model = ?, seats = ?, location = ?, price_per_day = ?, image = ?, availability = ?  WHERE id = ?";
    try (PreparedStatement pst = cnx.prepareStatement(req)) {
        pst.setString(1, cars.getModel());
        pst.setInt(2, cars.getSeats());
        pst.setString(3, cars.getLocation());
        pst.setFloat(4, cars.getPrice());
        pst.setString(5, cars.getImage()); // Set the image URL
        pst.setString(6, cars.getStatus().toString());
        pst.setInt(7, cars.getId());

        int rowsUpdated = pst.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Car updated successfully");
        } else {
            System.out.println("No car found with the given ID");
        }
    } catch (SQLException e) {
        System.err.println("Error updating car: " + e.getMessage());
    }
}

@Override
public void deleteCars(int id) {
    String req = "DELETE FROM cars WHERE id = ?";
    try (PreparedStatement pst = cnx.prepareStatement(req)) {
        pst.setInt(1, id); // Pass the car ID to delete

        int rowsDeleted = pst.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Car deleted successfully");
        } else {
            System.out.println("No car found with the given ID");
        }
    } catch (SQLException e) {
        System.err.println("Error deleting car: " + e.getMessage());
    }
}

@Override
public List<cars> getAllCars() throws SQLException {
    String req = "SELECT * FROM `cars`";
    List<cars> carsList = new ArrayList<>();
    try {
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            cars c = new cars();
            c.setId(rs.getInt("id"));
            c.setModel(rs.getString("model"));
            c.setSeats(rs.getInt("seats"));
            c.setLocation(rs.getString("location"));
            c.setPrice(rs.getInt("price_per_day"));
            c.setImage(rs.getString("image"));
            String statusString = rs.getString("availability");  // Get the string from the database
            if (statusString != null) {
                c.setStatus(cars.availability.valueOf(statusString));  // Convert string to enum
            }
            System.out.println(statusString);
            carsList.add(c);

        }
    }catch (SQLException e) {
        System.out.println(e.getErrorCode());
    }




    return carsList;
}
@Override
public cars getCarById(int id) {
    String req = "SELECT * FROM `cars` WHERE `id` = ?";
    cars car = null;

    try (PreparedStatement pst = cnx.prepareStatement(req)) {
        pst.setInt(1, id); // Set the ID parameter
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            car = new cars();
            car.setId(rs.getInt("id"));
            car.setModel(rs.getString("model"));
            car.setSeats(rs.getInt("seats"));
            car.setLocation(rs.getString("location"));
            car.setPrice(rs.getFloat("price_per_day"));
        }
    } catch (SQLException e) {
        System.err.println("Error fetching car by ID: " + e.getMessage());
    }

    return car; // Returns null if no car is found with the given ID
}



}
