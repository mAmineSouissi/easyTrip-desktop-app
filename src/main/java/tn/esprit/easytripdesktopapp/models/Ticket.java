package tn.esprit.easytripdesktopapp.models;

public class Ticket {
    private int idTicket;
    private int flightNumber;
    private String airline;
    private String departureCity;
    private String arrivalCity;
    private String departureDate;
    private String departureTime;
    private String arrivalDate;
    private String arrivalTime;
    private String ticketClass;
    private float price;
    private String ticketType;
    private String cityImage;
    private int agencyId;
    private int promotionId;
    private int userId;

    public Ticket() {}

    public Ticket(int idTicket, int flightNumber, String airline, String departureCity, String arrivalCity, String departureDate, String departureTime, String arrivalDate, String arrivalTime, String ticketClass, float price, String ticketType, String cityImage, int agencyId, int promotionId, int userId) {
        this.idTicket = idTicket;
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.ticketClass = ticketClass;
        this.price = price;
        this.ticketType = ticketType;
        this.cityImage = cityImage;
        this.agencyId = agencyId; // Initialisation de l'ID de l'agence
        this.promotionId = promotionId; // Initialisation de l'ID de la promotion
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getters and Setters
    public int getIdTicket() { return idTicket; }
    public void setIdTicket(int idTicket) { this.idTicket = idTicket; }

    public int getFlightNumber() { return flightNumber; }
    public void setFlightNumber(int flightNumber) { this.flightNumber = flightNumber; }

    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }

    public String getDepartureCity() { return departureCity; }
    public void setDepartureCity(String departureCity) { this.departureCity = departureCity; }

    public String getArrivalCity() { return arrivalCity; }
    public void setArrivalCity(String arrivalCity) { this.arrivalCity = arrivalCity; }

    public String getDepartureDate() { return departureDate; }
    public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(String arrivalDate) { this.arrivalDate = arrivalDate; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getTicketClass() { return ticketClass; }
    public void setTicketClass(String ticketClass) { this.ticketClass = ticketClass; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public String getTicketType() { return ticketType; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }

    public String getCityImage() { return cityImage; }
    public void setCityImage(String cityImage) { this.cityImage = cityImage; }

    public int getAgencyId() { return agencyId; } // Getter pour l'ID de l'agence
    public void setAgencyId(int agencyId) { this.agencyId = agencyId; } // Setter pour l'ID de l'agence

    public int getPromotionId() { return promotionId; } // Getter pour l'ID de la promotion
    public void setPromotionId(int promotionId) { this.promotionId = promotionId; } // Setter pour l'ID de la promotion

    @Override
    public String toString() {
        return "Ticket{" +
                "idTicket=" + idTicket +
                ", flightNumber=" + flightNumber +
                ", airline='" + airline + '\'' +
                ", departureCity='" + departureCity + '\'' +
                ", arrivalCity='" + arrivalCity + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", ticketClass='" + ticketClass + '\'' +
                ", price=" + price +
                ", ticketType='" + ticketType + '\'' +
                ", cityImage='" + cityImage + '\'' +
                ", agencyId=" + agencyId + // Affichage de l'ID de l'agence
                ", promotionId=" + promotionId + // Affichage de l'ID de la promotion
                '}';
    }
}