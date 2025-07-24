// --- test/Main.java ---
package com.aurionpro.test;

import java.util.List;
import java.util.Scanner;

import com.aurionpro.model.Builder;
import com.aurionpro.model.Guitar;
import com.aurionpro.model.GuitarSpec;
import com.aurionpro.model.Inventory;
import com.aurionpro.model.Type;
import com.aurionpro.model.Wood;

public class GuitarTest {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Inventory inventory = new Inventory();

    public static void main(String[] args) {
        preloadInventory(); 

        while (true) {
            System.out.println("\n--- Guitar Inventory System ---");
            System.out.println("1. Owner - Add Guitar");
            System.out.println("2. Customer - Search Guitar");
            System.out.println("3. View All Guitars");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1": addGuitar(); break;
                case "2": searchGuitar(); break;
                case "3": viewAllGuitars(); break;
                case "4": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid option. Please choose 1 to 4.");
            }
        }
    }

    private static void preloadInventory() {
        inventory.addGuitar("001", 14999.0, new GuitarSpec(Builder.FENDER, Type.ELECTRIC, Wood.ALDER, Wood.ALDER, "Stratocaster"));
        inventory.addGuitar("002", 13999.0, new GuitarSpec(Builder.GIBSON, Type.ELECTRIC, Wood.MAHOGANY, Wood.MAPLE, "Les Paul"));
        inventory.addGuitar("003", 11999.0, new GuitarSpec(Builder.MARTIN, Type.ACOUSTIC, Wood.INDAIN_ROSEWOOD, Wood.SITKA, "D-28"));
        inventory.addGuitar("004", 12999.0, new GuitarSpec(Builder.COLLINGS, Type.ACOUSTIC, Wood.MAPLE, Wood.MAPLE, "OM2H"));
        inventory.addGuitar("005", 15999.0, new GuitarSpec(Builder.OLSON, Type.ACOUSTIC, Wood.CEDAR, Wood.MAPLE, "SJ"));
    }

    private static void viewAllGuitars() {
        List<Guitar> all = inventory.search(new GuitarSpec(null, null, null, null, null));
        if (all.isEmpty()) {
            System.out.println("No guitars in inventory.");
        } else {
            System.out.println("\n--- All Guitars ---");
            for (Guitar g : all) {
                System.out.println(g);
            }
        }
    }

    private static void addGuitar() {
        try {
            System.out.print("Serial Number: ");
            String serial = scanner.nextLine().trim();
            if (serial.isEmpty()) throw new IllegalArgumentException("Serial number cannot be empty.");

            System.out.print("Price: ");
            String priceInput = scanner.nextLine().trim();
            if (priceInput.isEmpty()) throw new IllegalArgumentException("Price cannot be empty.");
            double price = Double.parseDouble(priceInput);

            Builder builder = chooseEnum(Builder.class, "Builder");

            System.out.print("Model: ");
            String model = scanner.nextLine().trim();
            if (model.isEmpty()) throw new IllegalArgumentException("Model cannot be empty.");

            Type type = chooseEnum(Type.class, "Type");
            Wood backWood = chooseEnum(Wood.class, "Back Wood");
            Wood topWood = chooseEnum(Wood.class, "Top Wood");

            GuitarSpec spec = new GuitarSpec(builder, type, backWood, topWood, model);
            inventory.addGuitar(serial, price, spec);
            System.out.println("Guitar added.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format. Please enter a valid price.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private static void searchGuitar() {
        try {
            System.out.println("\nEnter Search Criteria (press Enter to skip any):");

            System.out.print("Model: ");
            String modelInput = scanner.nextLine().trim();
            String model = modelInput.isEmpty() ? null : modelInput;

            Builder builder = null;
            if (askYesNo("Do you want to enter Builder? (y/n): ")) {
                builder = chooseEnum(Builder.class, "Builder");
            }

            Type type = null;
            if (askYesNo("Do you want to enter Type? (y/n): ")) {
                type = chooseEnum(Type.class, "Type");
            }

            Wood backWood = null;
            if (askYesNo("Do you want to enter Back Wood? (y/n): ")) {
                backWood = chooseEnum(Wood.class, "Back Wood");
            }

            Wood topWood = null;
            if (askYesNo("Do you want to enter Top Wood? (y/n): ")) {
                topWood = chooseEnum(Wood.class, "Top Wood");
            }

            GuitarSpec searchSpec = new GuitarSpec(builder, type, backWood, topWood, model);
            List<Guitar> results = inventory.search(searchSpec);

            if (results.isEmpty()) {
                System.out.println("No matching guitars found.");
            } else {
                System.out.println("\nMatching Guitars:");
                for (Guitar g : results) {
                    System.out.println(g);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static boolean askYesNo(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
        }
    }

    private static <T extends Enum<T>> T chooseEnum(Class<T> enumClass, String prompt) {
        T[] values = enumClass.getEnumConstants();
        while (true) {
            System.out.println("Choose " + prompt + ":");
            for (int i = 0; i < values.length; i++) {
                System.out.println((i + 1) + ". " + values[i]);
            }
            System.out.print("Enter number: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > values.length) {
                    System.out.println("Invalid choice. Enter number between 1 and " + values.length);
                    continue;
                }
                return values[choice - 1];
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid number.");
            }
        }
    }
}
