package tn.esprit.easytripdesktopapp.models;

import tn.esprit.easytripdesktopapp.models.Ticket;

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

    public Promotion() {
        this.tickets = new ArrayList<>(); // Initialisation de la liste des tickets
    }

    public Promotion(int id, String title, String description, float discount_percentage, Date validUntil) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.discount_percentage = discount_percentage;
        this.validUntil = validUntil;
        this.tickets = new ArrayList<>(); // Initialisation de la liste des tickets
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

    public List<Ticket> getTickets() { return tickets; } // Getter pour la liste des tickets
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; } // Setter pour la liste des tickets

    // Méthode pour ajouter un ticket à la promotion
    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    // Méthode pour supprimer un ticket de la promotion
    public void removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", discount_percentage=" + discount_percentage +
                ", validUntil=" + validUntil +
                ", tickets=" + tickets.size() + // Affichage du nombre de tickets associés
                '}';
    }
}