package com.aurionpro.model;

public class UPI implements IPaymentGateway {

    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using UPI.");
    }

    @Override
    public void refund(double amount) {
        System.out.println("Refunded " + amount + " to UPI.");
    }
}
