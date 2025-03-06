package tn.esprit.easytripdesktopapp.models;

public class cars {
    private int id;
    private String model;
    private int seats;
    private String location;
    private float price;
    private String image; // New attribute for the image URL
    private availability status; // Field for the availability status

    public enum availability {
        AVAILABLE,
        NOT_AVAILABLE,
    }
    public cars() {}

    public cars(int id, String model, int seats, String location, float price, availability status, String image) {
        this.id = id;
        this.model = model;
        this.seats = seats;
        this.location = location;
        this.price = price;
        this.status = status;
        this.image = image;
    }

    // Constructor with all attributes including the image
    public cars(String model, int seats, String location, float price, String image) {
        this.model = model;
        this.seats = seats;
        this.location = location;
        this.price = price;
        this.image = image; // Initialize image


    }

    // Constructor with ID for updates (including the image)
    public cars(int id, String model, int seats, String location, float price, String image) {
        this.id = id;
        this.model = model;
        this.seats = seats;
        this.location = location;
        this.price = price;
        this.image = image; // Initialize image

    }

    public cars(int id, String model, int seats, String location, float price, String image, availability status) {
        this.id = id;
        this.model = model;
        this.seats = seats;
        this.location = location;
        this.price = price;
        this.image = image;
        this.status = status;
    }



    // Getters and setters for each field, including the image
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image; // Setter for image URL
    }

    public availability getStatus() {
        return status;
    }

    public void setStatus(availability status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "cars{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", seats=" + seats +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' + // Include image in the string representation
                '}';
    }
}
