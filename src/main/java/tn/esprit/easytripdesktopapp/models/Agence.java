package tn.esprit.easytripdesktopapp.models;

import java.util.ArrayList;
import java.util.List;

public class Agence {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String image;
    private List<Ticket> tickets; // Liste des tickets associés à cette agence
    private List<Hotel> hotels; // Liste des hôtels associés à cette agence

    public Agence() {
        this.tickets = new ArrayList<>();
        this.hotels = new ArrayList<>();
    }

    public Agence(int id, String name, String address, String phone, String email, String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.image = image;
        this.tickets = new ArrayList<>();
        this.hotels = new ArrayList<>();
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return name; }
    public void setNom(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }

    public List<Hotel> getHotels() { return hotels; }
    public void setHotels(List<Hotel> hotels) { this.hotels = hotels; }

    // Méthode pour ajouter un hôtel à l'agence
    public void addHotel(Hotel hotel) {
        this.hotels.add(hotel);
        hotel.setAgence(this);
    }

    // Méthode pour supprimer un hôtel de l'agence
    public void removeHotel(Hotel hotel) {
        this.hotels.remove(hotel);
        hotel.setAgence(null);
    }

    @Override
    public String toString() {
        return "Agence{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", tickets=" + tickets.size() +
                ", hotels=" + hotels.size() +
                '}';
    }
}