package tn.esprit.easytripdesktopapp.interfaces;

import java.util.List;
import java.util.Optional;

public interface CRUDService<T> {
    void add(T t);

    List<T> getAll();

    void update(T t);

    void delete(T t);

    T getById(int id); // Optional to handle cases where an entity is not found

    List<T> search(String keyword);

    boolean exists(int id);

    long count();


}
