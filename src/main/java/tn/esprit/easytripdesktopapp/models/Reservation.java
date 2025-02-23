package tn.esprit.easytripdesktopapp.models;

import java.util.Date;

public class Reservation {

    private int idReservation;
    private Date ordreDate;
    private String nom;
    private String prenom;
    private int phone;
    private String email;
    private int places;


    public Reservation() {}


    public Reservation(int idReservation, Date ordreDate, String nom, String prenom, int phone, String email, int places) {
        this.idReservation = idReservation;
        this.ordreDate = ordreDate;
        this.nom = nom;
        this.prenom = prenom;
        this.phone = phone;
        this.email = email;
        this.places = places;
    }



    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public Date getOrdreDate() {
        return ordreDate;
    }

    public void setOrdreDate(Date ordreDate) {
        this.ordreDate = ordreDate;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", ordreDate=" + ordreDate +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", places=" + places +
                '}';
    }
}
