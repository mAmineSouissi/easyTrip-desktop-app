package tn.esprit.models;

import java.util.Date;

public class Res_Transport {
    private int id;
    private int userId;
    private int carId; // Foreign key for the car
    private Date startDate;
    private Date endDate;
    private Status status; // Using the Status enumeration





    // Enumeration for the status
    public enum Status {
        IN_PROGRESS,
        CANCELED,
        DONE
    }


    public Res_Transport(int id, int userId, int carId, Date startDate, Date endDate, Status status) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Res_Transport() {
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getCarId() {
        return carId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Res_Transport{" +
                "id=" + id +
                ", userId=" + userId +
                ", carId=" + carId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}
