package tn.esprit.easytripdesktopapp;

import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;

import java.util.List;
import java.util.Scanner;

public class MainHotel {
    public static void main(String[] args) {
        ServiceHotel serviceHotel = new ServiceHotel();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Gestion des Hôtels ===");
            System.out.println("1. Ajouter un hôtel");
            System.out.println("2. Afficher tous les hôtels");
            System.out.println("3. Mettre à jour un hôtel");
            System.out.println("4. Supprimer un hôtel");
            System.out.println("5. Quitter");
            System.out.print("Choisissez une option : ");
            int choix = scanner.nextInt();
            scanner.nextLine(); // Pour consommer la nouvelle ligne

            switch (choix) {
                case 1:
                    ajouterHotel(serviceHotel, scanner);
                    break;
                case 2:
                    afficherTousLesHotels(serviceHotel);
                    break;
                case 3:
                    mettreAJourHotel(serviceHotel, scanner);
                    break;
                case 4:
                    supprimerHotel(serviceHotel, scanner);
                    break;
                case 5:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private static void ajouterHotel(ServiceHotel serviceHotel, Scanner scanner) {
        System.out.println("\n=== Ajouter un Hôtel ===");
        System.out.print("Nom de l'hôtel : ");
        String name = scanner.nextLine();
        System.out.print("Adresse : ");
        String adresse = scanner.nextLine();
        System.out.print("Ville : ");
        String city = scanner.nextLine();
        System.out.print("Note (rating) : ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne
        System.out.print("Description : ");
        String description = scanner.nextLine();
        System.out.print("Prix : ");
        float price = scanner.nextFloat();
        scanner.nextLine(); // Pour consommer la nouvelle ligne
        System.out.print("Type de chambre (simple, double, suit) : ");
        String typeRoom = scanner.nextLine();
        System.out.print("Nombre de chambres : ");
        int numRoom = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne
        System.out.print("URL de l'image : ");
        String image = scanner.nextLine();

        Hotel hotel = new Hotel(0, name, adresse, city, rating, description, price, typeRoom, numRoom, image);
        serviceHotel.add(hotel);
    }

    private static void afficherTousLesHotels(ServiceHotel serviceHotel) {
        System.out.println("\n=== Liste des Hôtels ===");
        List<Hotel> hotels = serviceHotel.getAll();
        if (hotels.isEmpty()) {
            System.out.println("Aucun hôtel trouvé.");
        } else {
            for (Hotel h : hotels) {
                System.out.println(h);
            }
        }
    }

    private static void mettreAJourHotel(ServiceHotel serviceHotel, Scanner scanner) {
        System.out.println("\n=== Mettre à jour un Hôtel ===");
        System.out.print("ID de l'hôtel à mettre à jour : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne

        // Récupérer l'hôtel existant
        List<Hotel> hotels = serviceHotel.getAll();
        Hotel hotelToUpdate = null;
        for (Hotel h : hotels) {
            if (h.getId() == id) {
                hotelToUpdate = h;
                break;
            }
        }

        if (hotelToUpdate == null) {
            System.out.println("Hôtel non trouvé.");
            return;
        }

        System.out.print("Nouveau nom de l'hôtel : ");
        String name = scanner.nextLine();
        System.out.print("Nouvelle adresse : ");
        String adresse = scanner.nextLine();
        System.out.print("Nouvelle ville : ");
        String city = scanner.nextLine();
        System.out.print("Nouvelle note (rating) : ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne
        System.out.print("Nouvelle description : ");
        String description = scanner.nextLine();
        System.out.print("Nouveau prix : ");
        float price = scanner.nextFloat();
        scanner.nextLine(); // Pour consommer la nouvelle ligne
        System.out.print("Nouveau type de chambre (simple, double, suit) : ");
        String typeRoom = scanner.nextLine();
        System.out.print("Nouveau nombre de chambres : ");
        int numRoom = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne
        System.out.print("Nouvelle URL de l'image : ");
        String image = scanner.nextLine();

        // Mettre à jour les informations de l'hôtel
        hotelToUpdate.setName(name);
        hotelToUpdate.setAdresse(adresse);
        hotelToUpdate.setCity(city);
        hotelToUpdate.setRating(rating);
        hotelToUpdate.setDescription(description);
        hotelToUpdate.setPrice(price);
        hotelToUpdate.setTypeRoom(typeRoom);
        hotelToUpdate.setNumRoom(numRoom);
        hotelToUpdate.setImage(image);

        serviceHotel.update(hotelToUpdate);
    }

    private static void supprimerHotel(ServiceHotel serviceHotel, Scanner scanner) {
        System.out.println("\n=== Supprimer un Hôtel ===");
        System.out.print("ID de l'hôtel à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne

        // Récupérer l'hôtel existant
        List<Hotel> hotels = serviceHotel.getAll();
        Hotel hotelToDelete = null;
        for (Hotel h : hotels) {
            if (h.getId() == id) {
                hotelToDelete = h;
                break;
            }
        }

        if (hotelToDelete == null) {
            System.out.println("Hôtel non trouvé.");
            return;
        }

        serviceHotel.delete(hotelToDelete);
    }
}