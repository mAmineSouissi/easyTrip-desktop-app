package tn.esprit.easytripdesktopapp.interfaces;

import tn.esprit.easytripdesktopapp.models.cars;

import java.sql.SQLException;
import java.util.List;

public interface ICarsService<T> {
    public void addCars(T t);
    public void updateCars(T t);
    void deleteCars(int id);
    List<T>getAllCars() throws SQLException;
    public cars getCarById(int id);


}
