package com.pluralsight;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AccountingLedgerApp {

    //Transactions.csv file where all transactions will be stored
    private static final String TRANSACTION_FILE = "transactions.csv";

    //List to keep all transaction records in memory
    private static List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        //Check if the file exists; if not, create it
        File file = new File(TRANSACTION_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created new transactions.csv file.");
            } catch (IOException e) {
                System.out.println("Error creating transactions file: " + e.getMessage());
            }
        }

        //Scanner to read user input from the console
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // ======= Main Menu Loop =======
        while (running) {
            System.out.println("\n===== Welcome to your Account =====");
            System.out.println("1: Make Deposit");
            System.out.println("2: Make Payment");
            System.out.println("3: View Account Transactions");
            System.out.println("4: Exit");
            System.out.print("Choose option #1-4: ");
            String choice = scanner.nextLine().trim();

            // === Option 1: Deposit ===
            if (choice.equals("1")) {
                System.out.println("\n--- Make Deposit ---");
                System.out.print("Date (YYYY-MM-DD): ");
                String date = scanner.nextLine().trim();
                System.out.print("Time (HH:MM:SS): ");
                String time = scanner.nextLine().trim();
                System.out.print("Description: ");
                String description = scanner.nextLine().trim();
                System.out.print("Vendor: ");
                String vendor = scanner.nextLine().trim();
                System.out.print("Amount: ");
                String amount = scanner.nextLine().trim();

                //Save the deposit to file
                saveTransaction(date, time, description, vendor, amount);
                System.out.println("Deposit saved!");

            }
            //
            else if (choice.equals("2")) {
                System.out.println("\n--- Make Payment ---");
                System.out.print("Date (YYYY-MM-DD): ");
                String date = scanner.nextLine().trim();
                System.out.print("Time (HH:MM:SS): ");
                String time = scanner.nextLine().trim();
                System.out.print("Description: ");
                String description = scanner.nextLine().trim();
                System.out.print("Vendor: ");
                String vendor = scanner.nextLine().trim();
                System.out.print("Amount: ");
                String amount = scanner.nextLine().trim();

                //Payments are stored as negative values
                if (!amount.startsWith("-")) {
                    amount = "-" + amount;
                }

                //Save the payment to file
                saveTransaction(date, time, description, vendor, amount);
                System.out.println("Payment saved!");
            }
            //View Transactions
            else if (choice.equals("3")) {
                showLedgerMenu(scanner);
            }
            //Exit ===
            else if (choice.equals("4")) {
                System.out.println("Have a blessed day!");
                running = false;
            }
            else {
                System.out.println("Invalid option. Try again!");
            }
        }

        scanner.close();
    }

    //This method saves a transaction to the CSV file
    public static void saveTransaction(String date, String time, String description, String vendor, String amount) {
        try (FileWriter writer = new FileWriter(TRANSACTION_FILE, true)) {
            writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");
        } catch (IOException e) {
            System.out.println("Error writing transaction: " + e.getMessage());
        }
    }

    //Ledger Menu
    public static void showLedgerMenu(Scanner scanner) {
        boolean inLedgerMenu = true;

        while (inLedgerMenu) {
            System.out.println("\n--- Ledger Menu ---");
            System.out.println("A: View All Transactions");
            System.out.println("D: View Deposits Only");
            System.out.println("P: View Payments Only");
            System.out.println("R: Reports");
            System.out.println("H: Return to Home Menu");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
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
                    System.out.println("Invalid option.");
            }
        }
    }

    //Display type of transactions
    public static void displayTransactions(String type) {
        try {
            File file = new File(TRANSACTION_FILE);
            Scanner fileScanner = new Scanner(file);

            List<String> lines = new ArrayList<>();

            while (fileScanner.hasNextLine()) {
                lines.add(fileScanner.nextLine());
            }
            Collections.reverse(lines); // Show most recent first

            boolean found = false;
            for (String line : lines) {
                String[] parts = line.split("\\|");
                String amount = parts[4].trim();

                if (type.equals("ALL") ||
                        (type.equals("DEPOSITS") && !amount.startsWith("-")) ||
                        (type.equals("PAYMENTS") && amount.startsWith("-"))) {
                    System.out.println(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No " + type.toLowerCase() + " transactions found.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not read transactions file.");
        }
    }

    //Menu fo reports
    public static void showReportsMenu(Scanner scanner) {
        boolean inReports = true;

        while (inReports) {
            System.out.println("\n--- Reports Menu ---");
            System.out.println("1: Month To Date");
            System.out.println("2: Previous Month");
            System.out.println("3: Year to Date");
            System.out.println("4: Previous Year");
            System.out.println("5: Search by Vendor");
            System.out.println("0: Back");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    generateMonthToDateReport();
                    break;
                case "2":
                    generatePreviousMonthReport();
                    break;
                case "3":
                    generateYearToDateReport();
                    break;
                case "4":
                    generatePreviousYearReport();
                    break;
                case "5":
                    System.out.print("Enter vendor name to search: ");
                    String vendor = scanner.nextLine().trim().toLowerCase();
                    searchByVendor(vendor);
                    break;
                case "0":
                    inReports = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    //Month to date option
    public static void generateMonthToDateReport() {
        try {
            File file = new File(TRANSACTION_FILE);
            Scanner scanner = new Scanner(file);

            LocalDate today = LocalDate.now();
            LocalDate startOfMonth = today.withDayOfMonth(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            List<String> results = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0].trim(), formatter);

                if ((date.isEqual(startOfMonth) || date.isAfter(startOfMonth)) && date.isBefore(today.plusDays(1))) {
                    results.add(line);
                }
            }

            System.out.println("\n--- Month To Date Transactions ---");
            if (results.isEmpty()) {
                System.out.println("No transactions found this month.");
            } else {
                for (String transaction : results) {
                    System.out.println(transaction);
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
    //Previous Month
    public static void generatePreviousMonthReport() {
        try {
            File file = new File(TRANSACTION_FILE);
            Scanner scanner = new Scanner(file);

            LocalDate today = LocalDate.now();
            LocalDate startOfPreviousMonth = today.minusMonths(1).withDayOfMonth(1);
            LocalDate endOfPreviousMonth = startOfPreviousMonth.withDayOfMonth(startOfPreviousMonth.lengthOfMonth());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List<String> results = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0].trim(), formatter);

                if (!date.isBefore(startOfPreviousMonth) && !date.isAfter(endOfPreviousMonth)) {
                    results.add(line);
                }
            }

            System.out.println("\n--- Previous Month Transactions ---");
            if (results.isEmpty()) {
                System.out.println("No transactions found in previous month.");
            } else {
                for (String transaction : results) {
                    System.out.println(transaction);
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error generating previous month report: " + e.getMessage());
        }
    }
    //Year To Date
    public static void generateYearToDateReport() {
        try {
            File file = new File(TRANSACTION_FILE);
            Scanner scanner = new Scanner(file);

            LocalDate today = LocalDate.now();
            LocalDate startOfYear = today.withDayOfYear(1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List<String> results = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0].trim(), formatter);

                if (!date.isBefore(startOfYear) && !date.isAfter(today)) {
                    results.add(line);
                }
            }

            System.out.println("\n--- Year To Date Transactions ---");
            if (results.isEmpty()) {
                System.out.println("No transactions found year to date.");
            } else {
                for (String transaction : results) {
                    System.out.println(transaction);
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error generating year-to-date report: " + e.getMessage());
        }
    }
    //Previous Year
    public static void generatePreviousYearReport() {
        try {
            File file = new File(TRANSACTION_FILE);
            Scanner scanner = new Scanner(file);

            LocalDate today = LocalDate.now();
            int previousYear = today.getYear() - 1;
            LocalDate startOfPreviousYear = LocalDate.of(previousYear, 1, 1);
            LocalDate endOfPreviousYear = LocalDate.of(previousYear, 12, 31);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List<String> results = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0].trim(), formatter);

                if (!date.isBefore(startOfPreviousYear) && !date.isAfter(endOfPreviousYear)) {
                    results.add(line);
                }
            }

            System.out.println("\n--- Previous Year Transactions ---");
            if (results.isEmpty()) {
                System.out.println("No transactions found in previous year.");
            } else {
                for (String transaction : results) {
                    System.out.println(transaction);
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error generating previous year report: " + e.getMessage());
        }
    }
    //Search by vendor option
    public static void searchByVendor(String vendorName) {
        try {
            File file = new File(TRANSACTION_FILE);
            Scanner scanner = new Scanner(file);
            boolean found = false;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts[3].toLowerCase().contains(vendorName)) {
                    System.out.println(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found for vendor: " + vendorName);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not read transactions file.");
        }
    }
}
