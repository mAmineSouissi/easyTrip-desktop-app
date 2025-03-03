package tn.esprit.easytripdesktopapp.models;
import java.util.Date;

public class Reservation {
    private int id; // Changed from idReservation to match database column name
    private int user_id;
    private int travel_id;
    private String status; // Added status field from database
    private Date orderDate; // Changed from ordreDate to match database column name
    private int ticket_id;
    private int hotel_id;
    private String nom;
    private String prenom;
    private int phone;
    private String email;
    private int places;
    private int offerId;


    public Reservation() {}

    public Reservation(int id, int user_id, int travel_id, String status, Date orderDate,
                       int ticket_id, int hotel_id, String nom, String prenom,
                       int phone, String email, int places) {
        this.id = id;
        this.user_id = user_id;
        this.travel_id = travel_id;
        this.status = status;
        this.orderDate = orderDate;
        this.ticket_id = ticket_id;
        this.hotel_id = hotel_id;
        this.nom = nom;
        this.prenom = prenom;
        this.phone = phone;
        this.email = email;
        this.places = places;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTravel_id() {
        return travel_id;
    }

    public void setTravel_id(int travel_id) {
        this.travel_id = travel_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
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

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", travel_id=" + travel_id +
                ", status='" + status + '\'' +
                ", orderDate=" + orderDate +
                ", ticket_id=" + ticket_id +
                ", hotel_id=" + hotel_id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", places=" + places +
                '}';
    }
}