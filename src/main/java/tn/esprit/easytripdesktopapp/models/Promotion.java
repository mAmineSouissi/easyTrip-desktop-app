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

    public Promotion() {
    }

    public Promotion(int id, String title, String description, float discount_percentage, Date valid_until) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.discount_percentage = discount_percentage;
        this.valid_until = valid_until;
        this.offerTravels = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getDiscount_percentage() {
        return discount_percentage;
    }

    public Date getValid_until() {
        return valid_until;
    }

    public List<OfferTravel> getOfferTravels() {
        return offerTravels;
    }

    public void setOfferTravels(List<OfferTravel> offerTravels) {
        this.offerTravels = offerTravels;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDiscount_percentage(float discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public void setValid_until(Date valid_until) {
        this.valid_until = valid_until;
    }

    public void addOffer(OfferTravel offer) {
        this.offerTravels.add(offer);
    }

    public void removeOffer(OfferTravel offer) {
        this.offerTravels.remove(offer);
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", discount_percentage=" + discount_percentage + '\'' +
                ", valid_until=" + valid_until + '\'' +
                ", offerTravels=" + offerTravels +
                '}';
    }
}
