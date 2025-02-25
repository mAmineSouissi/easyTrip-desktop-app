package tn.esprit.easytripdesktopapp.models;

import java.util.Date;

public class OfferTravel {

    private int id;
    private String departure;
    private String destination;
    private Date departure_date;
    private Date arrival_date;
    private String hotelName;
    private String flightName;
    private String discription;
    private float price;
    private String image;
    private Agence agence;
    private Promotion promotion;
    private Category category;

    public OfferTravel() {

    }

    public OfferTravel(int id, String departure, String destination, Date departure_date, Date arrival_date,
                 String hotelName, String flightName, String discription, float price,
                 String image, Agence agence, Promotion promotion, Category category)
    {
        this.id = id;
        this.departure = departure;
        this.destination = destination;
        this.departure_date = departure_date;
        this.arrival_date = arrival_date;
        this.hotelName = hotelName;
        this.flightName = flightName;
        this.discription = discription;
        this.price = price;
        this.image = image;
        this.agence = agence;
        this.promotion = promotion;
        this.category = category;  //Initialise Category
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public Date getDeparture_date() {
        return departure_date;
    }

    public Date getArrival_date() {
        return arrival_date;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getFlightName() {
        return flightName;
    }

    public String getDiscription() {
        return discription;
    }

    public float getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public Agence getAgence() {
        return agence;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public Category getCategory() {  //getter Category
        return category;
    }


    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDeparture_date(Date departure_date) {
        this.departure_date = departure_date;
    }

    public void setArrival_date(Date arrival_date) {
        this.arrival_date = arrival_date;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public void setDiscription(String description) {
        this.discription = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public void setCategory(Category category) {  //seter category
        this.category = category;
    }



    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", departure_date=" + departure_date +
                ", arrival_date=" + arrival_date +
                ", hotelName='" + hotelName + '\'' +
                ", flightName='" + flightName + '\'' +
                ", description='" + discription + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", agence=" + (agence != null ? agence.getNom() : "null") +
                ", promotion=" + (promotion != null ? promotion.getTitle() : "null") +
                ", category=" + category +   // affichage de la category
                '}';
    }
}