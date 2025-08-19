package com.musafir.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String city;
    private String role;
    private double balance;

    // Getters & Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User [id=" + id 
            + ", name=" + name 
            + ", email=" + email 
            + ", phone=" + phone 
            + ", city=" + city 
            + ", role=" + role 
            + ", balance=" + balance + "]";
    }
}
