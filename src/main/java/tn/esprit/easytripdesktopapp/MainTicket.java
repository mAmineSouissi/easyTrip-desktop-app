package tn.esprit.easytripdesktopapp;

import tn.esprit.easytripdesktopapp.models.Ticket;
import tn.esprit.easytripdesktopapp.services.ServiceTicket;

import java.util.List;
import java.util.Scanner;

public class MainTicket {
    public static void main(String[] args) {
        ServiceTicket serviceTicket = new ServiceTicket();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Gestion des Tickets ===");
            System.out.println("1. Ajouter un ticket");
            System.out.println("2. Afficher tous les tickets");
            System.out.println("3. Mettre à jour un ticket");
            System.out.println("4. Supprimer un ticket");
            System.out.println("5. Quitter");
            System.out.print("Choisissez une option : ");
            int choix = scanner.nextInt();
            scanner.nextLine(); // Pour consommer la nouvelle ligne

            switch (choix) {
                case 1:
                    ajouterTicket(serviceTicket, scanner);
                    break;
                case 2:
                    afficherTousLesTickets(serviceTicket);
                    break;
                case 3:
                    mettreAJourTicket(serviceTicket, scanner);
                    break;
                case 4:
                    supprimerTicket(serviceTicket, scanner);
                    break;
                case 5:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private static void ajouterTicket(ServiceTicket serviceTicket, Scanner scanner) {
        System.out.println("\n=== Ajouter un Ticket ===");
        System.out.print("Numéro de vol : ");
        int flightNumber = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne
        System.out.print("Compagnie aérienne : ");
        String airline = scanner.nextLine();
        System.out.print("Ville de départ : ");
        String departureCity = scanner.nextLine();
        System.out.print("Ville d'arrivée : ");
        String arrivalCity = scanner.nextLine();
        System.out.print("Date de départ (YYYY-MM-DD) : ");
        String departureDate = scanner.nextLine();
        System.out.print("Heure de départ (HH:MM) : ");
        String departureTime = scanner.nextLine();
        System.out.print("Date d'arrivée (YYYY-MM-DD) : ");
        String arrivalDate = scanner.nextLine();
        System.out.print("Heure d'arrivée (HH:MM) : ");
        String arrivalTime = scanner.nextLine();
        System.out.print("Classe (Economy, Business, First) : ");
        String ticketClass = scanner.nextLine();
        System.out.print("Prix : ");
        float price = scanner.nextFloat();
        scanner.nextLine();
        System.out.print("Type de ticket (one-way, round-trip) : ");
        String ticketType = scanner.nextLine();

        Ticket ticket = new Ticket(0, flightNumber, airline, departureCity, arrivalCity, departureDate, departureTime, arrivalDate, arrivalTime, ticketClass, price, ticketType);
        serviceTicket.add(ticket);
    }

    private static void afficherTousLesTickets(ServiceTicket serviceTicket) {
        System.out.println("\n=== Liste des Tickets ===");
        List<Ticket> tickets = serviceTicket.getAll();
        if (tickets.isEmpty()) {
            System.out.println("Aucun ticket trouvé.");
        } else {
            for (Ticket t : tickets) {
                System.out.println(t);
            }
        }
    }

    private static void mettreAJourTicket(ServiceTicket serviceTicket, Scanner scanner) {
        System.out.println("\n=== Mettre à jour un Ticket ===");
        System.out.print("ID du ticket à mettre à jour : ");
        int id = scanner.nextInt();
        scanner.nextLine();


        List<Ticket> tickets = serviceTicket.getAll();
        Ticket ticketToUpdate = null;
        for (Ticket t : tickets) {
            if (t.getIdTicket() == id) {
                ticketToUpdate = t;
                break;
            }
        }

        if (ticketToUpdate == null) {
            System.out.println("Ticket non trouvé.");
            return;
        }

        System.out.print("Nouveau numéro de vol : ");
        int flightNumber = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne
        System.out.print("Nouvelle compagnie aérienne : ");
        String airline = scanner.nextLine();
        System.out.print("Nouvelle ville de départ : ");
        String departureCity = scanner.nextLine();
        System.out.print("Nouvelle ville d'arrivée : ");
        String arrivalCity = scanner.nextLine();
        System.out.print("Nouvelle date de départ (YYYY-MM-DD) : ");
        String departureDate = scanner.nextLine();
        System.out.print("Nouvelle heure de départ (HH:MM) : ");
        String departureTime = scanner.nextLine();
        System.out.print("Nouvelle date d'arrivée (YYYY-MM-DD) : ");
        String arrivalDate = scanner.nextLine();
        System.out.print("Nouvelle heure d'arrivée (HH:MM) : ");
        String arrivalTime = scanner.nextLine();
        System.out.print("Nouvelle classe (Economy, Business, First) : ");
        String ticketClass = scanner.nextLine();
        System.out.print("Nouveau prix : ");
        float price = scanner.nextFloat();
        scanner.nextLine();
        System.out.print("Nouveau type de ticket (one-way, round-trip) : ");
        String ticketType = scanner.nextLine();


        ticketToUpdate.setFlightNumber(flightNumber);
        ticketToUpdate.setAirline(airline);
        ticketToUpdate.setDepartureCity(departureCity);
        ticketToUpdate.setArrivalCity(arrivalCity);
        ticketToUpdate.setDepartureDate(departureDate);
        ticketToUpdate.setDepartureTime(departureTime);
        ticketToUpdate.setArrivalDate(arrivalDate);
        ticketToUpdate.setArrivalTime(arrivalTime);
        ticketToUpdate.setTicketClass(ticketClass);
        ticketToUpdate.setPrice(price);
        ticketToUpdate.setTicketType(ticketType);

        serviceTicket.update(ticketToUpdate);
    }

    private static void supprimerTicket(ServiceTicket serviceTicket, Scanner scanner) {
        System.out.println("\n=== Supprimer un Ticket ===");
        System.out.print("ID du ticket à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();


        List<Ticket> tickets = serviceTicket.getAll();
        Ticket ticketToDelete = null;
        for (Ticket t : tickets) {
            if (t.getIdTicket() == id) {
                ticketToDelete = t;
                break;
            }
        }

        if (ticketToDelete == null) {
            System.out.println("Ticket non trouvé.");
            return;
        }

        serviceTicket.delete(ticketToDelete);
    }
}