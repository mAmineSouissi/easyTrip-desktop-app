package tn.esprit.easytripdesktopapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Promotion {
    private int id;
    private String title;
    private String description;
    private float discount_percentage;
    private Date validUntil;
    private List<Ticket> tickets; // Liste des tickets associés à cette promotion
    private List<Hotel> hotels; // Liste des hôtels associés à cette promotion

    public Promotion() {
        this.tickets = new ArrayList<>();
        this.hotels = new ArrayList<>();
    }

    public Promotion(int id, String title, String description, float discount_percentage, Date validUntil) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.discount_percentage = discount_percentage;
        this.validUntil = validUntil;
        this.tickets = new ArrayList<>();
        this.hotels = new ArrayList<>();
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getDiscount_percentage() { return discount_percentage; }
    public void setDiscount_percentage(float discount_percentage) { this.discount_percentage = discount_percentage; }

    public Date getValidUntil() { return validUntil; }
    public void setValidUntil(Date validUntil) { this.validUntil = validUntil; }

    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }

    public List<Hotel> getHotels() { return hotels; }
    public void setHotels(List<Hotel> hotels) { this.hotels = hotels; }

    // Méthode pour ajouter un hôtel à la promotion
    public void addHotel(Hotel hotel) {
        this.hotels.add(hotel);
        hotel.setPromotion(this);
    }

    // Méthode pour supprimer un hôtel de la promotion
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
                ", validUntil=" + validUntil +
                ", tickets=" + tickets.size() +
                ", hotels=" + hotels.size() +
                '}';
    }
}