package tn.esprit.interfaces;

import tn.esprit.models.Res_Transport;

import java.sql.SQLException;
import java.util.List;


public interface IRes_transport<T> {
    public void AddResTransport(T t);
    public void UpdateResTransport(T t);
    void DeleteResTransport(int id);
    List<T>getAllResTransport() throws SQLException;

}
