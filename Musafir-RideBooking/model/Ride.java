package com.musafir.model;

import java.util.Date;

public class Ride {
    private int id;
    private int userId;
    private int driverId;
    private String pickup;
    private String drop;
    private Date pickupTime;
    private String status;
    private String paymentMethod;
    private double fare;
    private String cancellationReason;

    // Getters & Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDriverId() {
        return driverId;
    }
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getPickup() {
        return pickup;
    }
    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDrop() {
        return drop;
    }
    public void setDrop(String drop) {
        this.drop = drop;
    }

    public Date getPickupTime() {
        return pickupTime;
    }
    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getFare() {
        return fare;
    }
    public void setFare(double fare) {
        this.fare = fare;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }
    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    @Override
    public String toString() {
        return "Ride [id=" + id 
            + ", userId=" + userId 
            + ", driverId=" + driverId 
            + ", pickup=" + pickup 
            + ", drop=" + drop 
            + ", pickupTime=" + pickupTime 
            + ", status=" + status 
            + ", paymentMethod=" + paymentMethod 
            + ", fare=" + fare 
            + ", cancellationReason=" + cancellationReason + "]";
    }
}
