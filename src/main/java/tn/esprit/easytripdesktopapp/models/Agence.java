package tn.esprit.easytripdesktopapp.models;

import tn.esprit.easytripdesktopapp.models.Ticket;

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

    public Agence() {
        this.tickets = new ArrayList<>(); // Initialisation de la liste des tickets
    }

    public Agence(int id, String name, String address, String phone, String email, String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.image = image;
        this.tickets = new ArrayList<>(); // Initialisation de la liste des tickets
    }

    public Agence(int id, String name) {
        this.id = id;
        this.name = name;
        this.tickets = new ArrayList<>(); // Initialisation de la liste des tickets
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

    public List<Ticket> getTickets() { return tickets; } // Getter pour la liste des tickets
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; } // Setter pour la liste des tickets

    // Méthode pour ajouter un ticket à l'agence
    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    // Méthode pour supprimer un ticket de l'agence
    public void removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
    }

    @Override
    public String toString() {
        return "Agency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", tickets=" + tickets.size() + // Affichage du nombre de tickets associés
                '}';
    }
}