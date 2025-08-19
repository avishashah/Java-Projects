package com.musafir.model;

public class Vehicle {
    private int id;
    private String type;        // car_economy, car_premium, car_6seater, auto, bike
    private String numberPlate;
    private int driverId;

    public Vehicle() {}

    public Vehicle(int id, String type, String numberPlate, int driverId) {
        this.id = id;
        this.type = type;
        this.numberPlate = numberPlate;
        this.driverId = driverId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getNumberPlate() {
        return numberPlate;
    }
    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public int getDriverId() {
        return driverId;
    }
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return "Vehicle [id=" + id + ", type=" + type + ", numberPlate=" + numberPlate + ", driverId=" + driverId + "]";
    }
}
