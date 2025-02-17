package tn.esprit.easytripdesktopapp.services;

import tn.esprit.easytripdesktopapp.interfaces.IService;
import tn.esprit.easytripdesktopapp.models.Panier;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.Connection;
import java.util.List;

public class ServicePanier implements IService<Panier> {

    private Connection cnx ;

    public ServicePanier(){
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Panier panier) {

    }

    @Override
    public List<Panier> getAll() {
        return List.of();
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Panier panier) {

    }

    @Override
    public Panier getById(int id) {
        return null;
    }


}
