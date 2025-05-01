package com.pluralsight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private LocalDate date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    //Constructor
    public Transaction(LocalDate date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    // Method to parse a CSV line into a Transaction object
    public static Transaction fromCSV(String csvLine) {
        // Split the CSV line
        String[] parts = csvLine.split("\\|");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the date
        LocalDate date = LocalDate.parse(parts[0].trim(), formatter);
        String time = parts[1].trim();
        String description = parts[2].trim();
        String vendor = parts[3].trim();
        double amount = Double.parseDouble(parts[4].trim());

        return new Transaction(date, time, description, vendor, amount);
    }
}
