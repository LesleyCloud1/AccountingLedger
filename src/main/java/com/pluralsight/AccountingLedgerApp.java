package com.pluralsight;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class AccountingLedgerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); //read user input
        boolean running = true; // Loop will run until the user says "exit"

        while (running) {
            //HomeScreen
            System.out.println("\n---------Welcome to Accounting Ledger Application-----");
            System.out.println("1: Make Deposit");
            System.out.println("2: Make Payment");
            System.out.println("3: View Account Transactions");
            System.out.println("4: Exit");
            System.out.println("Select Option #1-4: ");

            String choice = scanner.nextLine().trim();//trim removes extra spaces on user input

            if (choice.equals("1") || choice.equals("D")) {
                System.out.println("\n---- Make Deposit ----");
                //Ask user for deposit details this info will be saved to transactions csv file
                System.out.print("Enter date (YYYY-MM-DD): ");
                String date = scanner.nextLine().trim();
                System.out.print("Enter time (HH:MM:SS): ");
                String time = scanner.nextLine().trim();
                System.out.print("Enter Description: ");
                String description = scanner.nextLine().trim();
                System.out.print("Enter Vendor: ");
                String vendor = scanner.nextLine().trim();
                System.out.print("Enter Amount: ");
                String amount = scanner.nextLine().trim();

                //Confirm user data
                System.out.println("\n Deposit Confirmation:");
                System.out.println(date + "|" + time + "|" + description + "|" + vendor + "|" + amount);

                //Save deposit data to transactions.csv
                try (FileWriter writer = new FileWriter("transactions.csv", true)) {
                    writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");
                    System.out.println("Deposit saved to transactions.csv");
                } catch (IOException e) {
                    System.out.println("Error saving deposit: " + e.getMessage());
                }

            } else if (choice.equals("2") || choice.equals("P")) {
                System.out.println("\n---- Make Payment ----");
                //Ask user for payment details this info will be saved to transactions csv file
                System.out.print("Enter date (YYYY-MM-DD): ");
                String date = scanner.nextLine().trim();
                System.out.print("Enter time (HH:MM:SS): ");
                String time = scanner.nextLine().trim();
                System.out.print("Enter description: ");
                String description = scanner.nextLine().trim();
                System.out.print("Enter vendor: ");
                String vendor = scanner.nextLine().trim();
                System.out.print("Enter Amount: ");
                String amount = scanner.nextLine().trim();
                //Create minus sign for negative number
                if (!amount.startsWith("-")) {
                    amount = "-" + amount;
                }
                //Confirm user data
                System.out.println("\n Payment Confirmation:");
                System.out.println(date + "|" + time + "|" + description + "|" + vendor + "|" + amount);

                //Save payment data to transactions.csv
                try (FileWriter writer = new FileWriter("transactions.csv", true)) {
                    writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");
                    System.out.println("Payment saved to transactions.csv");
                } catch (IOException e) {
                    System.out.println("Error saving deposit: " + e.getMessage());
                }


            } else if (choice.equals("3") || choice.equals("L")) {
                boolean inLedgerMenu = true;

                while (inLedgerMenu) {
                    System.out.println("\n---- View Account Transactions ----");
                    System.out.println("A: View All Transactions");
                    System.out.println("D: View Deposits Only");
                    System.out.println("P: View Payments Only");
                    System.out.println("R: Reports");
                    System.out.println("H: Return to Home");

                    String ledgerChoice = scanner.nextLine().trim().toUpperCase();

                    switch (ledgerChoice) {
                        case "A":
                            displayTransactions("ALL");
                            break;
                        case "D":
                            displayTransactions("DEPOSITS");
                            break;
                        case "P":
                            displayTransactions("PAYMENTS");
                            break;
                        case "R":
                            showReportsMenu(scanner);
                            break;
                        case "H":
                            inLedgerMenu = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter A, D, P, R, or H.");
                    }
                }

            } else if (choice.equals("4") || choice.equalsIgnoreCase("X")) {
                System.out.println("Have a nice day!");
                running = false;

            } else {
                System.out.println("Invalid input. Please select options #1-4.");
            }
        }

        scanner.close();
    }

    public static void displayTransactions(String type) {
        try {
            File file = new File("transactions.csv");
            Scanner fileScanner = new Scanner(file);

            boolean hasData = false;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                String amountStr = parts[4].trim();

                switch (type) {
                    case "ALL":
                        System.out.println(line);
                        hasData = true;
                        break;
                    case "DEPOSITS":
                        if (!amountStr.startsWith("-")) {
                            System.out.println(line);
                            hasData = true;
                        }
                        break;
                    case "PAYMENTS":
                        if (amountStr.startsWith("-")) {
                            System.out.println(line);
                            hasData = true;
                        }
                        break;
                }
            }
            fileScanner.close();
            if (!hasData) {
                System.out.println("No " + type.toLowerCase() + " transactions found.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Transaction file not found.");
        }
    }

    public static void showReportsMenu(Scanner scanner) {
        boolean inReportsMenu = true;
        while (inReportsMenu) {
            System.out.println("\n---- Reports Menu ----");
            System.out.println("1: Month To Date");
            System.out.println("2: Previous Month");
            System.out.println("3: Year To Date");
            System.out.println("4: Previous Year");
            System.out.println("5: Search by Vendor");
            System.out.println("0: Back to Ledger");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("Month To Date report not implemented yet.");
                    break;
                case "2":
                    System.out.println("Previous Month report not implemented yet.");
                    break;
                case "3":
                    System.out.println("Year To Date report not implemented yet.");
                    break;
                case "4":
                    System.out.println("Previous Year report not implemented yet.");
                    break;
                case "5":
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine().trim().toLowerCase();
                    searchByVendor(vendor);
                    break;
                case "0":
                    inReportsMenu = false;
                    break;
                default:
                    System.out.println("Invalid input. Please select 0-5.");
            }
        }
    }

    public static void searchByVendor(String vendorName) {
        try {
            File file = new File("transactions.csv");
            Scanner fileScanner = new Scanner(file);
            boolean found = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length >= 4 && parts[3].toLowerCase().contains(vendorName)) {
                    System.out.println(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found for vendor: " + vendorName);
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Transaction file not found.");
        }
    }
}
