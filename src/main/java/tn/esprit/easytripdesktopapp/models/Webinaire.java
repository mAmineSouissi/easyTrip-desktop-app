package tn.esprit.easytripdesktopapp.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Webinaire {
    private int id;
    private String title;
    private String description;
    private LocalDateTime debutDateTime;
    private LocalDateTime finitDateTime;
    private String link;
    private Hotel hotel;
    private String roomId; // Identifiant unique de la salle

    public Webinaire() {
        this.roomId = UUID.randomUUID().toString(); // Générer un UUID unique
    }

    public Webinaire(String title, String description, LocalDateTime debutDateTime, LocalDateTime finitDateTime, String link, Hotel hotel) {
        this.title = title;
        this.description = description;
        this.debutDateTime = debutDateTime;
        this.finitDateTime = finitDateTime;
        this.link = link;
        this.hotel = hotel;
        this.roomId = UUID.randomUUID().toString(); // Générer un UUID unique
    }

    // Getters et setters
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}