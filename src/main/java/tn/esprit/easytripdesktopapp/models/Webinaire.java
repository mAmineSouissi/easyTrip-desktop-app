package tn.esprit.easytripdesktopapp.models;

import java.time.LocalDateTime;

public class Webinaire {
    private int id;
    private String title;
    private String description;
    private LocalDateTime debutDateTime;
    private LocalDateTime finitDateTime;
    private String link;
    private Hotel hotel;

    // Constructors
    public Webinaire() {}

    public Webinaire(String title, String description, LocalDateTime debutDateTime, LocalDateTime finitDateTime, String link, Hotel hotel) {
        this.title = title;
        this.description = description;
        this.debutDateTime = debutDateTime;
        this.finitDateTime = finitDateTime;
        this.link = link;
        this.hotel = hotel;
    }

    // Getters and Setters
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

    public LocalDateTime getDebutDateTime() {
        return debutDateTime;
    }

    public void setDebutDateTime(LocalDateTime debutDateTime) {
        this.debutDateTime = debutDateTime;
    }

    public LocalDateTime getFinitDateTime() {
        return finitDateTime;
    }

    public void setFinitDateTime(LocalDateTime finitDateTime) {
        this.finitDateTime = finitDateTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}