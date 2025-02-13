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

    public Reclamation(int id, int userId, String status, Date date, String issue, String category) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.date = date;
        this.issue = issue;
        this.category = category;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }


    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

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
