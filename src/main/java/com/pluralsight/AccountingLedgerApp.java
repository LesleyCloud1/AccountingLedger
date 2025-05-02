package com.pluralsight;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AccountingLedgerApp {

    //Transactions.csv file, all transactions will be stored here
    private static final String TRANSACTION_FILE = "transactions.csv";

    //List to keep all transaction records in memory
    private static List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        //Check if the file exists or create it
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

        //Main Menu Loop
        while (running) {
            System.out.println("\n===== Welcome to your Account =====");
            System.out.println("1: Make Deposit");
            System.out.println("2: Make Payment");
            System.out.println("3: View Account Transactions");
            System.out.println("4: Exit");
            System.out.print("Choose option #1-4: ");
            String choice = scanner.nextLine().trim();

            //Make deposit
            if (choice.equals("1")) {
                System.out.println("\n--- Make Deposit ---");
                String date = InputValidator.getValidDate(scanner);
                String time = InputValidator.getValidTime(scanner);
                String description = InputValidator.getNonEmptyInput(scanner, "Description");
                String vendor = InputValidator.getNonEmptyInput(scanner, "Vendor");
                double amount = InputValidator.getValidAmount(scanner);

                //Save the deposit to file
                saveTransaction(date, time, description, vendor, String.valueOf(amount));
                System.out.println("Deposit saved!");
            }
            //Make payment
            else if (choice.equals("2")) {
                System.out.println("\n--- Make Payment ---");
                String date = InputValidator.getValidDate(scanner);
                String time = InputValidator.getValidTime(scanner);
                String description = InputValidator.getNonEmptyInput(scanner, "Description");
                String vendor = InputValidator.getNonEmptyInput(scanner, "Vendor");
                double amount = InputValidator.getValidAmount(scanner);

                //Payments are stored as negative values
                if (amount > 0) {
                    amount = -amount; //Convert to negative for payments
                }

                //Save the payment to file
                saveTransaction(date, time, description, vendor, String.valueOf(amount));
                System.out.println("Payment saved!");
            }
            //View Transactions
            else if (choice.equals("3")) {
                showLedgerMenu(scanner);
            }
            //Exit
            else if (choice.equals("4")) {
                System.out.println("Have a blessed day!");
                running = false;
            } else {
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

    public static void displayTransactions(String type) {
        try {
            File file = new File(TRANSACTION_FILE);
            Scanner fileScanner = new Scanner(file);

            List<String> lines = new ArrayList<>();

            while (fileScanner.hasNextLine()) {
                lines.add(fileScanner.nextLine());
            }
            Collections.reverse(lines); //Show most recent first

            boolean found = false;
            //Print a table header
            System.out.printf("\n%-12s %-8s %-25s %-20s %12s\n",
                    "Date", "Time", "Description", "Vendor", "Amount");
            System.out.println("----------------------------------------------------------------------------");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                try {
                    Transaction transaction = Transaction.fromCSV(line);
                    double amount = transaction.getAmount();

                    if (type.equals("ALL") ||
                            (type.equals("DEPOSITS") && amount >= 0) ||
                            (type.equals("PAYMENTS") && amount < 0)) {

                        //Format amount with commas and two decimal places
                        String formattedAmount = String.format("%,.2f", amount);

                        System.out.printf("%s | %s | %s | %s | $%s\n",
                                transaction.getDate(),
                                transaction.getTime(),
                                transaction.getDescription(),
                                transaction.getVendor(),
                                formattedAmount);
                        found = true;
                    }
                } catch (Exception e) {
                }
            }
            if (!found) {
                System.out.println("No " + type.toLowerCase() + " transactions found.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not read transactions file.");
        }
    }
    //Menu for reports
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

                //Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                //Ensure the line has at least 5 parts and the date is valid
                if (parts.length < 5) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;  //Skip malformed lines, I added this cod before implementing Input validator but want to keep
                }

                try {
                    LocalDate date = LocalDate.parse(parts[0].trim(), formatter);

                    //Check if the transaction is within the month-to-date range
                    if ((date.isEqual(startOfMonth) || date.isAfter(startOfMonth)) && date.isBefore(today.plusDays(1))) {
                        results.add(line);
                    }
                } catch (Exception e) {
                    //Catch parsing errors and skip the line if it's not a valid date
                    System.out.println("Skipping invalid date in line: " + line);
                    continue;
                }
            }

            System.out.println("\n--- Month To Date Transactions ---");
            System.out.printf("\n%-12s %-8s %-25s %-20s %12s\n",
                    "Date", "Time", "Description", "Vendor", "Amount");
            System.out.println("----------------------------------------------------------------------------");

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

                //Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                //Ensure the line has at least 5 parts and the date is valid
                if (parts.length < 5) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;  // Skip malformed lines
                }

                try {
                    LocalDate date = LocalDate.parse(parts[0].trim(), formatter);

                    //Check if the transaction is within the previous month range
                    if (!date.isBefore(startOfPreviousMonth) && !date.isAfter(endOfPreviousMonth)) {
                        results.add(line);
                    }
                } catch (Exception e) {
                    //Catch parsing errors and skip the line if it's not a valid date
                    System.out.println("Skipping invalid date in line: " + line);
                    continue;
                }
            }

            System.out.println("\n--- Previous Month Transactions ---");
            System.out.printf("\n%-12s %-8s %-25s %-20s %12s\n",
                    "Date", "Time", "Description", "Vendor", "Amount");
            System.out.println("----------------------------------------------------------------------------");

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

                //Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                //Ensure the line has at least 5 parts and the date is valid
                if (parts.length < 5) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;  // Skip malformed lines
                }

                try {
                    LocalDate date = LocalDate.parse(parts[0].trim(), formatter);

                    //Check if the transaction is within the year-to-date range
                    if (!date.isBefore(startOfYear) && !date.isAfter(today)) {
                        results.add(line);
                    }
                } catch (Exception e) {
                    //Catch parsing errors and skip the line if it's not a valid date
                    System.out.println("Skipping invalid date in line: " + line);
                    continue;
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

                //Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                //Ensure the line has at least 5 parts and the date is valid
                if (parts.length < 5) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;  // Skip malformed lines
                }

                try {
                    LocalDate date = LocalDate.parse(parts[0].trim(), formatter);

                    //Check if the transaction is within the previous year range
                    if (!date.isBefore(startOfPreviousYear) && !date.isAfter(endOfPreviousYear)) {
                        results.add(line);
                    }
                } catch (Exception e) {
                    //Catch parsing errors and skip the line if it's not a valid date
                    System.out.println("Skipping invalid date in line: " + line);
                    continue;
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

                //Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                //Ensure the line has at least 5 parts (date, time, description, vendor, amount)
                if (parts.length < 5) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;  // Skip malformed lines
                }

                //Check if the vendor name is part of the transaction
                if (parts[3].toLowerCase().contains(vendorName.toLowerCase())) {
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