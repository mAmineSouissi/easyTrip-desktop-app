package tn.esprit.easytripdesktopapp.models;

public class Hotel {

    private int id;
    private String name;
    private String adresse;
    private String city;
    private int rating;
    private String description;
    private float price;
    private String typeRoom; // simple, double, suit
    private int numRoom;
            private String image;

    public Hotel() {}

    public Hotel(int id, String name, String adresse, String city, int rating, String description, float price, String typeRoom, int numRoom, String image) {
        this.id = id;
        this.name = name;
        this.adresse = adresse;
        this.city = city;
        this.rating = rating;
        this.description = description;
        this.price = price;
        this.typeRoom = typeRoom;
        this.numRoom = numRoom;
        this.image = image;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public String getTypeRoom() { return typeRoom; }
    public void setTypeRoom(String typeRoom) { this.typeRoom = typeRoom; }

    public int getNumRoom() { return numRoom; }
    public void setNumRoom(int numRoom) { this.numRoom = numRoom; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", adresse='" + adresse + '\'' +
                ", city='" + city + '\'' +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", typeRoom='" + typeRoom + '\'' +
                ", numRoom=" + numRoom +
                ", image='" + image + '\'' +
                '}';
    }

    public static class hotel {

        private int id;
        private String name;
        private String adresse;
        private String city;
        private int rating;
        private String description;
        private float price;
        private String typeRoom; // simple, double, suit
        private int numRoom;
        private String image;

        public hotel() {}

        public hotel(int id, String name, String adresse, String city, int rating, String description, float price, String typeRoom, int numRoom, String image) {
            this.id = id;
            this.name = name;
            this.adresse = adresse;
            this.city = city;
            this.rating = rating;
            this.description = description;
            this.price = price;
            this.typeRoom = typeRoom;
            this.numRoom = numRoom;
            this.image = image;
        }

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getAdresse() { return adresse; }
        public void setAdresse(String adresse) { this.adresse = adresse; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public float getPrice() { return price; }
        public void setPrice(float price) { this.price = price; }

        public String getTypeRoom() { return typeRoom; }
        public void setTypeRoom(String typeRoom) { this.typeRoom = typeRoom; }

        public int getNumRoom() { return numRoom; }
        public void setNumRoom(int numRoom) { this.numRoom = numRoom; }

        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }

        @Override
        public String toString() {
            return "Hotel{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", adresse='" + adresse + '\'' +
                    ", city='" + city + '\'' +
                    ", rating=" + rating +
                    ", description='" + description + '\'' +
                    ", price=" + price +
                    ", typeRoom='" + typeRoom + '\'' +
                    ", numRoom=" + numRoom +
                    ", image='" + image + '\'' +
                    '}';
        }
    }
}

