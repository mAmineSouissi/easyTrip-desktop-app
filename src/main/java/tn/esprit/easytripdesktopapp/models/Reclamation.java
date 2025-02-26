package tn.esprit.easytripdesktopapp.models;

import java.sql.Date;

public class Reclamation {
    private int id;
    private int userId;
    private String status;
    private Date date;
    private String issue;
    private String category;

    public Reclamation() {}

    public Reclamation(int userId, String status, Date date, String issue, String category) {
        this.userId = userId;
        this.status = status;
        this.date = date;
        this.issue = issue;
        this.category = category;
    }

    public Reclamation(int id, int userId, String status, Date date, String issue, String category) {
        this(userId, status, date, issue, category);
        this.id = id;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) {
        if (id > 0) this.id = id;
        else throw new IllegalArgumentException("ID invalide");
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) {
        if (userId > 0) this.userId = userId;
        else throw new IllegalArgumentException("UserID invalide");
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        if (status != null && !status.trim().isEmpty()) this.status = status;
        else throw new IllegalArgumentException("Le statut ne peut pas être vide");
    }

    public Date getDate() { return date; }
    public void setDate(Date date) {
        if (date != null) this.date = date;
        else throw new IllegalArgumentException("La date ne peut pas être null");
    }

    public String getIssue() { return issue; }
    public void setIssue(String issue) {
        if (issue != null && !issue.trim().isEmpty()) this.issue = issue;
        else throw new IllegalArgumentException("L'issue ne peut pas être vide");
    }

    public String getCategory() { return category; }
    public void setCategory(String category) {
        if (category != null && !category.trim().isEmpty()) this.category = category;
        else throw new IllegalArgumentException("La catégorie ne peut pas être vide");
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", issue='" + issue + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}