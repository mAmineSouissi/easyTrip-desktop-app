package tn.esprit.easytripdesktopapp.interfaces;

import tn.esprit.easytripdesktopapp.models.Res_Transport;

import java.sql.SQLException;
import java.util.List;


public interface IRes_transport<T> {
    public void AddResTransport(T t);
    public void UpdateResTransport(T t);
    void DeleteResTransport(int id);
    List<T>getAllResTransport() throws SQLException;

}
