package com.aurionpro.test;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.model.CreditCard;
import com.aurionpro.model.IPaymentGateway;
import com.aurionpro.model.NetBanking;
import com.aurionpro.model.UPI;

public class Checkout {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IPaymentGateway payment = null;

        
        while (true) {
            try {
                System.out.println("Choose Payment Method:");
                System.out.println("1. Credit Card");
                System.out.println("2. UPI");
                System.out.println("3. Net Banking");
                System.out.print("Enter your choice (1-3): ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        payment = new CreditCard();
                        break;
                    case 2:
                        payment = new UPI();
                        break;
                    case 3:
                        payment = new NetBanking();
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                        continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); 
            }
        }

       
        double amount = 0;
        while (true) {
            try {
                System.out.print("Enter amount to pay: ");
                amount = scanner.nextDouble();
                if (amount <= 0) {
                    System.out.println("Amount must be greater than 0.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid amount! Please enter a valid number.");
                scanner.nextLine(); 
            }
        }

        payment.pay(amount);

       
        scanner.nextLine(); 
        while (true) {
            System.out.print("Do you want a refund? (yes/no): ");
            String refundChoice = scanner.nextLine().trim();

            if (refundChoice.equalsIgnoreCase("yes")) {
                payment.refund(amount);
                break;
            } else if (refundChoice.equalsIgnoreCase("no")) {
                System.out.println("No refund processed.");
                break;
            } else {
                System.out.println("Invalid input. Please type 'yes' or 'no'.");
            }
        }

        scanner.close();
    }
}
