package tn.esprit.easytripdesktopapp.models;

import java.util.ArrayList;
import java.util.List;

public class Agence {
    private int id,user_id;
    private String name, address, phone, email , image;
    private List<OfferTravel> offerTravels;
    private List<Ticket> tickets; // Liste des tickets associés à cette agence
    private List<Hotel> hotels;

    public Agence() {
        this.tickets = new ArrayList<>();
        this.hotels = new ArrayList<>();
    }

    public Agence(int id, String name, String address, String phone, String email , String image, int user_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.image = image;
        this.user_id = user_id;
        this.offerTravels = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.hotels = new ArrayList<>();

    }

    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }

    public List<Hotel> getHotels() { return hotels; }
    public void setHotels(List<Hotel> hotels) { this.hotels = hotels; }

    // Méthode pour ajouter un hôtel à l'agence
    public void addHotel(Hotel hotel) {
        this.hotels.add(hotel);
        hotel.setAgence(this);
    }

    public void removeHotel(Hotel hotel) {
        this.hotels.remove(hotel);
        hotel.setAgence(null);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNom() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public List<OfferTravel> getOfferTravels() {
        return offerTravels;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOfferTravels(List<OfferTravel> offerTravels) {
        this.offerTravels = offerTravels;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void addOffer(OfferTravel offer) {
        this.offerTravels.add(offer);
    }

    public void removeOffer(OfferTravel offer) {
        this.offerTravels.remove(offer);
    }

    @Override
    public String toString() {
        return "Agence{" +
                "id=" + id +
                ", nom='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                "    image='" + image + '\'' +
                ", offerTravels=" + offerTravels +
                ", tickets=" + tickets.size() +
                ", hotels=" + hotels.size() +
                '}' ;
    }
}
