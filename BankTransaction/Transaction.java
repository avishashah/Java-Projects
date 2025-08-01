package com.aurionpro.model;

import java.sql.*;
import java.util.Scanner;

public class Transaction {

    public static void deposit(Connection conn, Scanner scanner, int id) throws SQLException {
        System.out.print("Enter amount to deposit: ");
        double amount = Double.parseDouble(scanner.nextLine());

        PreparedStatement stmt = conn.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?");
        stmt.setDouble(1, amount);
        stmt.setInt(2, id);

        int rows = stmt.executeUpdate();
        if (rows > 0) {
            System.out.println("Deposit successful.");
        }
    }

    public static void withdraw(Connection conn, Scanner scanner, int id) throws SQLException {
        System.out.print("Enter amount to withdraw: ");
        double amount = Double.parseDouble(scanner.nextLine());

        PreparedStatement checkStmt = conn.prepareStatement("SELECT balance FROM accounts WHERE id = ?");
        checkStmt.setInt(1, id);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            double balance = rs.getDouble("balance");
            if (balance >= amount) {
                PreparedStatement withdrawStmt = conn.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id = ?");
                withdrawStmt.setDouble(1, amount);
                withdrawStmt.setInt(2, id);
                withdrawStmt.executeUpdate();
                System.out.println("Withdrawal successful.");
            } else {
                System.out.println("Insufficient balance.");
            }
        }
    }

    public static void transfer(Connection conn, Scanner scanner, int fromId) throws SQLException {
        System.out.print("Enter recipient's account number: ");
        String toAccountNumber = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        double amount = Double.parseDouble(scanner.nextLine());

        PreparedStatement fromStmt = conn.prepareStatement("SELECT balance FROM accounts WHERE id = ?");
        fromStmt.setInt(1, fromId);
        ResultSet fromRs = fromStmt.executeQuery();

        if (!fromRs.next()) {
            System.out.println("Sender not found.");
            return;
        }

        double senderBalance = fromRs.getDouble("balance");

        if (senderBalance < amount) {
            System.out.println("Insufficient balance.");
            return;
        }

        PreparedStatement toStmt = conn.prepareStatement("SELECT id FROM accounts WHERE account_number = ?");
        toStmt.setString(1, toAccountNumber);
        ResultSet toRs = toStmt.executeQuery();

        if (!toRs.next()) {
            System.out.println("Recipient account not found.");
            return;
        }

        int toId = toRs.getInt("id");

        PreparedStatement deduct = conn.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id = ?");
        deduct.setDouble(1, amount);
        deduct.setInt(2, fromId);
        deduct.executeUpdate();

        PreparedStatement add = conn.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?");
        add.setDouble(1, amount);
        add.setInt(2, toId);
        add.executeUpdate();

        System.out.println("Transfer successful.");
    }

    public static void selfTransfer(Connection conn, Scanner scanner, int customerId, String currentAccountNumber) throws SQLException {
        System.out.print("Enter your other account number to transfer TO: ");
        String toAccountNumber = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        double amount = Double.parseDouble(scanner.nextLine());

        if (toAccountNumber.equals(currentAccountNumber)) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }

        PreparedStatement findStmt = conn.prepareStatement("SELECT id FROM accounts WHERE id = ? AND account_number = ?");
        findStmt.setInt(1, customerId);
        findStmt.setString(2, toAccountNumber);
        ResultSet rs = findStmt.executeQuery();

        if (!rs.next()) {
            System.out.println("No matching account found for self-transfer.");
            return;
        }

        int toId = rs.getInt("id");

        PreparedStatement fromStmt = conn.prepareStatement("SELECT balance FROM accounts WHERE account_number = ?");
        fromStmt.setString(1, currentAccountNumber);
        ResultSet fromRs = fromStmt.executeQuery();

        if (!fromRs.next()) {
            System.out.println("Current account not found.");
            return;
        }

        double balance = fromRs.getDouble("balance");

        if (balance < amount) {
            System.out.println("Insufficient balance.");
            return;
        }

        PreparedStatement deduct = conn.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE account_number = ?");
        deduct.setDouble(1, amount);
        deduct.setString(2, currentAccountNumber);
        deduct.executeUpdate();

        PreparedStatement add = conn.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?");
        add.setDouble(1, amount);
        add.setInt(2, toId);
        add.executeUpdate();

        System.out.println("Self-transfer successful.");
    }

    public static void printStatement(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions WHERE account_id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        System.out.println("Date\t\tType\tAmount\tDetails");
        while (rs.next()) {
            System.out.printf("%s\t%s\t%.2f\t%s%n",
                    rs.getString("timestamp"),
                    rs.getString("type"),
                    rs.getDouble("amount"),
                    rs.getString("remarks"));
        }
    }

    public static void checkBalance(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM accounts WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            System.out.println("Current Balance: " + rs.getDouble("balance"));
        } else {
            System.out.println("Account not found.");
        }
    }
}
