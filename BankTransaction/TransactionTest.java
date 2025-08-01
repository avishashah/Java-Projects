package com.aurionpro.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.aurionpro.model.Admin;
import com.aurionpro.model.Customer;

public class TransactionTest {
    public static void main(String[] args) {
        Connection conn = null;
        Scanner scanner = new Scanner(System.in);

        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");

           
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/jdbc_transaction", "root", "Avisha@2606");

            int option = -1;

            while (option != 3) {
                System.out.println("\n--- BANK SYSTEM ---");
                System.out.println("1. Login as Admin");
                System.out.println("2. Login as Customer");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                if (!scanner.hasNextLine()) {
                    System.out.println("No input detected. Exiting...");
                    break;
                }

                String input = scanner.nextLine().trim();

                try {
                    option = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number (1-3).");
                    continue;
                }

                switch (option) {
                    case 1 -> {
                        System.out.print("Enter admin password: ");
                        String inputPassword = scanner.nextLine();
                        if ("admin123".equals(inputPassword)) {
                            Admin admin = new Admin(conn);
                            admin.showAdminMenu(scanner);
                        } else {
                            System.out.println("Incorrect password!");
                        }
                    }

                    case 2 -> {
                        Customer customer = Customer.login(conn, scanner);
                        if (customer != null) {
                            customer.showMenu(conn, scanner);
                        }
                    }

                    case 3 -> System.out.println("Thank you. Goodbye!");

                    default -> System.out.println("Invalid option. Please try again.");
                }
            }

        } catch (NoSuchElementException e) {
            System.out.println("Input stream closed. Exiting...");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                scanner.close();
            } catch (Exception e) {
                System.out.println("Failed to close resources: " + e.getMessage());
            }
        }
    }
}
