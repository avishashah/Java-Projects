package com.aurionpro.model;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Customer {
    private int accountId;
    private int id;
    private String name;
    private int age;
    private String pin;
    private int accountNumber;
    private double balance;

    public Customer(int accountId, int id, String name, int age, String pin, int accountNumber, double balance) {
        this.accountId = accountId;
        this.id = id;
        this.name = name;
        this.age = age;
        this.pin = pin;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public static Customer login(Connection conn, Scanner scanner) {
        System.out.print("Enter your 4-digit PIN: ");
        String inputPin = scanner.nextLine();

        try {
            String sql = "SELECT * FROM accounts WHERE pin = ? LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, inputPin);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer(
                    rs.getInt("account_id"),
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("pin"),
                    rs.getInt("account_number"),
                    rs.getDouble("balance")
                );
                System.out.println("Welcome " + customer.name + "!");
                return customer;
            } else {
                System.out.println("Login error: Incorrect PIN.");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return null;
        }
    }

    public void showMenu(Connection conn, Scanner scanner) {
        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. Check Balance");
            System.out.println("5. Self Transfer");
            System.out.println("6. Transaction Statement");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int option = getValidatedInt(scanner);
            switch (option) {
                case 1 -> deposit(conn, scanner);
                case 2 -> withdraw(conn, scanner);
                case 3 -> transfer(conn, scanner);
                case 4 -> checkBalance();
                case 5 -> selfTransfer(conn, scanner);
                case 6 -> showTransactions(conn);
                case 7 -> {
                    System.out.println("Goodbye " + name + "!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void deposit(Connection conn, Scanner scanner) {
        double amount;
        while (true) {
            System.out.print("Enter deposit amount: ");
            amount = getValidatedDouble(scanner);
            if (amount > 0) break;
            System.out.println("Amount must be greater than 0.");
        }

        balance += amount;
        updateBalance(conn);
        logTransaction(conn, "Deposit", amount, "Deposited by customer");
        System.out.println("Deposit successful.");
    }

    private void withdraw(Connection conn, Scanner scanner) {
        double amount;
        while (true) {
            System.out.print("Enter withdrawal amount: ");
            amount = getValidatedDouble(scanner);
            if (amount <= 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > balance) {
                System.out.println("Insufficient balance.");
            } else {
                break;
            }
        }

        balance -= amount;
        updateBalance(conn);
        logTransaction(conn, "Withdraw", amount, "Withdrawn by customer");
        System.out.println("Withdrawal successful.");
    }

    private void transfer(Connection conn, Scanner scanner) {
        int toAccNo;
        double amount;

        System.out.print("Enter recipient account number: ");
        toAccNo = getValidatedInt(scanner);

        while (true) {
            System.out.print("Enter amount to transfer: ");
            amount = getValidatedDouble(scanner);
            if (amount <= 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > balance) {
                System.out.println("Insufficient balance.");
            } else {
                break;
            }
        }

        try {
            String sql = "SELECT * FROM accounts WHERE account_number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, toAccNo);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Recipient account not found.");
                return;
            }

            int toAccId = rs.getInt("account_id");
            double toBalance = rs.getDouble("balance");

            balance -= amount;
            updateBalance(conn);

            PreparedStatement updateStmt = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE account_id = ?");
            updateStmt.setDouble(1, toBalance + amount);
            updateStmt.setInt(2, toAccId);
            updateStmt.executeUpdate();

            logTransaction(conn, "Transfer", amount, "Transferred to account: " + toAccNo);
            System.out.println("Transfer successful.");

        } catch (SQLException e) {
            System.out.println("Transfer error: " + e.getMessage());
        }
    }

    private void selfTransfer(Connection conn, Scanner scanner) {
        int toAccountNumber;
        double amount;

        System.out.print("Enter your account number to transfer TO: ");
        toAccountNumber = getValidatedInt(scanner);

        if (toAccountNumber == this.accountNumber) {
            System.out.println("You cannot self-transfer to the same account.");
            return;
        }

        while (true) {
            System.out.print("Enter amount to transfer: ");
            amount = getValidatedDouble(scanner);
            if (amount <= 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > balance) {
                System.out.println("Insufficient balance.");
            } else {
                break;
            }
        }

        try {
            String sql = "SELECT * FROM accounts WHERE account_number = ? AND id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, toAccountNumber);
            stmt.setInt(2, this.id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Target account not found or not yours.");
                return;
            }

            int toAccountId = rs.getInt("account_id");
            double toBalance = rs.getDouble("balance");

            balance -= amount;
            updateBalance(conn);

            PreparedStatement updateStmt = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE account_id = ?");
            updateStmt.setDouble(1, toBalance + amount);
            updateStmt.setInt(2, toAccountId);
            updateStmt.executeUpdate();

            logTransaction(conn, "Self Transfer", amount, "To own account: " + toAccountNumber);
            System.out.println("Self-transfer successful.");

        } catch (SQLException e) {
            System.out.println("Self-transfer error: " + e.getMessage());
        }
    }


    private void checkBalance() {
        System.out.println("Your current balance is: " + balance);
    }

    private void showTransactions(Connection conn) {
        try {
            String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n--- Transaction Statement ---");
            System.out.println("Timestamp               | Type          | Amount     | Remarks");
            System.out.println("---------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-23s| %-13s| ₹%-10.2f| %s%n",
                    rs.getTimestamp("timestamp"),
                    rs.getString("type"),
                    rs.getDouble("amount"),
                    rs.getString("remarks")
                );
            }

        } catch (SQLException e) {
            System.out.println("Transaction history error: " + e.getMessage());
        }
    }

    private void updateBalance(Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE account_id = ?");
            stmt.setDouble(1, balance);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update balance error: " + e.getMessage());
        }
    }

    private void logTransaction(Connection conn, String type, double amount, String remarks) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO transactions (account_id, type, amount, remarks) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, accountId);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.setString(4, remarks);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error logging transaction: " + e.getMessage());
        }
    }

    private int getValidatedInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    private double getValidatedDouble(Scanner sc) {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid amount: ");
            }
        }
    }
}
