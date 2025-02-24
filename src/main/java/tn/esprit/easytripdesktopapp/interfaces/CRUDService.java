package tn.esprit.easytripdesktopapp.interfaces;

import java.util.List;

public interface CRUDService<T> {

    void add(T t);

    List<T> getAll();

    void update(T t);

    void delete(T t);

}
