package tn.esprit.easytripdesktopapp.interfaces;

import java.util.List;

public interface IService<T> {
    void add(T t);

    List<T> getAll();

    void delete(int id);

    void update(T t);

    T getById(int id);

}
