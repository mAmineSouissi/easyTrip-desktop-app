package tn.esprit.easytripdesktopapp.models;

import java.sql.Date;

public class Feedback {
    private int id;
    private int userId;
    private int offerId;
    private int rating;
    private String message;
    private Date date;

    public Feedback() {}

    public Feedback(int id, int userId, int offerId, int rating, String message, Date date) {
        this.id = id;
        this.userId = userId;
        this.offerId = offerId;
        this.rating = rating;
        this.message = message;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getOfferId() { return offerId; }
    public void setOfferId(int offerId) { this.offerId = offerId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}

