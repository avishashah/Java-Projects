package com.aurionpro.model;

public class NetBanking implements IPaymentGateway {

    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Net Banking.");
    }

    @Override
    public void refund(double amount) {
        System.out.println("Refunded " + amount + " to Net Banking.");
    }
}

