package tn.esprit.easytripdesktopapp.models;

import java.sql.Date;

public class Feedback {
    private int id;
    private int user_id; // Changed to match database column name
    private int offerId;
    private int rating;
    private String message;
    private Date date;

    // Default constructor
    public Feedback() {}

    // Constructor with parameters
    public Feedback(int user_id, int offerId, int rating, String message, Date date) {
        this.user_id = user_id; // Use user_id here
        this.offerId = offerId;
        this.rating = rating;
        this.message = message;
        this.date = date;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUser_id() { return user_id; } // Use user_id here
    public void setUser_id(int user_id) { this.user_id = user_id; }

    public int getOfferId() { return offerId; }
    public void setOfferId(int offerId) { this.offerId = offerId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", user_id=" + user_id + // Use user_id here
                ", offerId=" + offerId +
                ", rating=" + rating +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
