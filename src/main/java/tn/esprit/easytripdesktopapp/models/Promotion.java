package tn.esprit.easytripdesktopapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Promotion {
    private int id;
    private String title;
    private String description;
    private float discount_percentage;
    private Date valid_until;
    private List<OfferTravel> offerTravels;
    private List<Ticket> tickets; // Liste des tickets associés à cette promotion
    private List<Hotel> hotels; // Liste des hôtels associés à cette promotion

    public Promotion() {
        this.offerTravels = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.hotels = new ArrayList<>();
    }

    public Promotion(int id, String title, String description, float discount_percentage, Date valid_until) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.discount_percentage = discount_percentage;
        this.valid_until = valid_until;
        this.offerTravels = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.hotels = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(float discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public Date getValid_until() {
        return valid_until;
    }

    public void setValid_until(Date valid_until) {
        this.valid_until = valid_until;
    }

    public List<OfferTravel> getOfferTravels() {
        return offerTravels;
    }

    public void setOfferTravels(List<OfferTravel> offerTravels) {
        this.offerTravels = offerTravels;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    public void addHotel(Hotel hotel) {
        this.hotels.add(hotel);
        hotel.setPromotion(this);
    }

    public void removeHotel(Hotel hotel) {
        this.hotels.remove(hotel);
        hotel.setPromotion(null);
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", discount_percentage=" + discount_percentage +
                ", valid_until=" + valid_until +
                ", offerTravels=" + offerTravels +
                '}';
    }
}