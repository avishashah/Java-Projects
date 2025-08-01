package com.aurionpro.model;

import java.sql.*;
import java.util.*;

public class Admin {
    private final Connection conn;

    public Admin(Connection conn) {
        this.conn = conn;
    }

    public void showAdminMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Add Account");
            System.out.println("2. View All Accounts");
            System.out.println("3. View Transaction History");
            System.out.println("4. Close Account");
            System.out.println("5. Create Additional Account");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1 -> addAccount(sc);
                case 2 -> viewAllAccounts();
                case 3 -> viewTransactionHistory();
                case 4 -> closeAccount(sc);
                case 5 -> createAdditionalAccount(sc);
                case 6 -> System.out.println("Exiting Admin Menu...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    public void addAccount(Scanner sc) {
        try {
            String name;
            do {
                System.out.print("Enter customer name: ");
                name = sc.nextLine().trim();
                if (!isValidName(name)) {
                    System.out.println("Invalid name. Name must contain only alphabets and spaces.");
                }
            } while (!isValidName(name));

            int age;
            do {
                System.out.print("Enter age (18–110): ");
                age = getValidatedInt(sc, 18, 110);
            } while (age == -1);

            double balance;
            do {
                System.out.print("Enter initial deposit (minimum ₹1000): ");
                balance = getValidatedDouble(sc, 1000, Double.MAX_VALUE);
            } while (balance == -1);

            String pin = String.format("%04d", new Random().nextInt(10000));
            int accNum = 100000 + new Random().nextInt(900000);

            int customerId = 1;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM accounts")) {
                if (rs.next() && rs.getInt(1) > 0) {
                    customerId = rs.getInt(1) + 1;
                }
            }

            String sql = "INSERT INTO accounts(id, name, age, pin, account_number, balance) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, customerId);
                ps.setString(2, name);
                ps.setInt(3, age);
                ps.setString(4, pin);
                ps.setInt(5, accNum);
                ps.setDouble(6, balance);
                ps.executeUpdate();
            }

            System.out.println("✅ Account created successfully!");
            System.out.printf("Account ID: %d%nAccount Number: %d%nPIN: %s%n", customerId, accNum, pin);

        } catch (SQLException e) {
            System.out.println("Error adding account: " + e.getMessage());
        }
    }

    public void createAdditionalAccount(Scanner sc) {
        try {
            int id;
            do {
                System.out.print("Enter existing customer ID: ");
                id = getValidatedInt(sc, 1, Integer.MAX_VALUE);
            } while (id == -1);

            String name;
            int age;

            String checkSql = "SELECT name, age FROM accounts WHERE id = ? LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("No customer found with ID: " + id);
                        return;
                    }
                    name = rs.getString("name");
                    age = rs.getInt("age");
                }
            }

            if (!isValidName(name)) {
                System.out.println("Invalid stored customer name. Contains invalid characters.");
                return;
            }

            System.out.println("Found customer: " + name);

            double balance;
            do {
                System.out.print("Enter initial balance for new account: ");
                balance = getValidatedDouble(sc, 1000, Double.MAX_VALUE);
            } while (balance == -1);

            String pin = String.format("%04d", new Random().nextInt(10000));
            int accNum = 100000 + new Random().nextInt(900000);

            String insertSql = "INSERT INTO accounts(id, name, age, pin, account_number, balance) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setInt(3, age);
                ps.setString(4, pin);
                ps.setInt(5, accNum);
                ps.setDouble(6, balance);
                ps.executeUpdate();
            }

            System.out.println("Additional account created for " + name);
            System.out.printf("Account Number: %d%nPIN: %s%n", accNum, pin);

        } catch (SQLException e) {
            System.out.println("Error creating additional account: " + e.getMessage());
        }
    }

    public void viewAllAccounts() {
        String sql = "SELECT id, name, age, account_number, balance FROM accounts";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("ID\tName\tAge\tAccount No\tBalance");
            while (rs.next()) {
                System.out.printf("%d\t%s\t%d\t%d\t%.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getInt("account_number"),
                        rs.getDouble("balance"));
            }

        } catch (SQLException e) {
            System.out.println("Error viewing accounts: " + e.getMessage());
        }
    }

    public void viewTransactionHistory() {
        String sql = "SELECT * FROM transactions ORDER BY timestamp DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("TxnID | AccountID | Type | Amount | Remarks | Timestamp");
            while (rs.next()) {
                System.out.printf("%d | %d | %s | %.2f | %s | %s%n",
                        rs.getInt("id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("remarks"),
                        rs.getTimestamp("timestamp"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching transaction history: " + e.getMessage());
        }
    }

    public void closeAccount(Scanner sc) {
        try {
            int accNum;
            do {
                System.out.print("Enter account number to close: ");
                accNum = getValidatedInt(sc, 100000, 999999);
            } while (accNum == -1);

            String getSql = "SELECT * FROM accounts WHERE account_number = ?";
            try (PreparedStatement getPs = conn.prepareStatement(getSql)) {
                getPs.setInt(1, accNum);
                try (ResultSet rs = getPs.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("No account found with that number.");
                        return;
                    }

                    String insertSql = "INSERT INTO inactive_accounts(account_id, id, name, age, pin, account_number, balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                        insertPs.setInt(1, rs.getInt("account_id"));
                        insertPs.setInt(2, rs.getInt("id"));
                        insertPs.setString(3, rs.getString("name"));
                        insertPs.setInt(4, rs.getInt("age"));
                        insertPs.setString(5, rs.getString("pin"));
                        insertPs.setInt(6, rs.getInt("account_number"));
                        insertPs.setDouble(7, rs.getDouble("balance"));
                        insertPs.executeUpdate();
                    }

                    try (PreparedStatement delPs = conn.prepareStatement("DELETE FROM accounts WHERE account_number = ?")) {
                        delPs.setInt(1, accNum);
                        delPs.executeUpdate();
                    }

                    System.out.println("Account closed and moved to inactive_accounts.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error closing account: " + e.getMessage());
        }
    }

    private boolean isValidName(String name) {
        return name != null && name.matches("[A-Za-z ]+");
    }

    private int getValidatedInt(Scanner sc, int min, int max) {
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input. Must be a number.");
            sc.nextLine(); 
            return -1;
        }
        int val = sc.nextInt();
        sc.nextLine(); // consume newline
        if (val < min || val > max) {
            return -1;
        }
        return val;
    }

    private double getValidatedDouble(Scanner sc, double min, double max) {
        if (!sc.hasNextDouble()) {
            System.out.println("Invalid input. Must be a number.");
            sc.nextLine(); // discard invalid input
            return -1;
        }
        double val = sc.nextDouble();
        sc.nextLine(); // consume newline
        if (val < min || val > max) {
            return -1;
        }
        return val;
    }
}
