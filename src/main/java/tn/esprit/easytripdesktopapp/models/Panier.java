package tn.esprit.easytripdesktopapp.models;

import java.util.ArrayList;
import java.util.List;

public class Panier {

    private int id_panier;
    private double totalprix;
    private List<Reservation> reservations;


    public Panier() {
        this.reservations = new ArrayList<>();
    }

    public Panier(int id_panier, double totalprix) {
        this.id_panier = id_panier;
        this.totalprix = totalprix;
        this.reservations = new ArrayList<>();
    }

    public Panier(double totalprix) {
        this.totalprix = totalprix;
        this.reservations = new ArrayList<>();
    }


    public int getId_panier() {
        return id_panier;
    }

    public void setId_panier(int id_panier) {
        this.id_panier = id_panier;
    }

    public double getTotalprix() {
        return totalprix;
    }

    public void setTotalprix(double totalprix) {
        this.totalprix = totalprix;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "Panier{" +
                "id_panier=" + id_panier +
                ", totalprix=" + totalprix +
                ", reservations=" + reservations +
                '}';
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
    }


}
