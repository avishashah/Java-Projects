package com.musafir.model;

public class Driver {
    private int id;
    private int userId;
    private String vehicleType;
    private String numberPlate;
    private double rating;

    // Getters and Setters
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

    public String getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getNumberPlate() {
        return numberPlate;
    }
    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Driver [id=" + id + ", userId=" + userId + ", vehicleType=" + vehicleType +
               ", numberPlate=" + numberPlate + ", rating=" + rating + "]";
    }
}
