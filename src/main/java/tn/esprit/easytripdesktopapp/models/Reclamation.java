package tn.esprit.easytripdesktopapp.models;

import java.sql.Date;

public class Reclamation {
    private int id;
    private int user_id;
    private String status;
    private Date date;
    private String issue;
    private String category;

    public Reclamation() {}

    // Constructor without id (since it is auto-incremented)
    public Reclamation(int user_id, String status, Date date, String issue, String category) {
        this.user_id = user_id;
        this.status = status;
        this.date = date;
        this.issue = issue;
        this.category = category;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getuser_id() { return user_id; }
    public void setuser_id(int user_id) { this.user_id = user_id; }

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
                ", user_id=" + user_id +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", issue='" + issue + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
