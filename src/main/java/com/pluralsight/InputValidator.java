package com.pluralsight;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InputValidator {

        //Validate Date Input (YYYY-MM-DD)
        public static String getValidDate(Scanner scanner) {
            String date = "";
            boolean valid = false;

            while (!valid) {
                System.out.print("Date (YYYY-MM-DD): ");
                date = scanner.nextLine().trim();
                try {
                    LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    valid = true; //Correct date format
                } catch (Exception e) {
                    System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                }
            }
            return date;
        }

        //Validate Time Input (HH:MM:SS)
        public static String getValidTime(Scanner scanner) {
            String time = "";
            boolean valid = false;

            while (!valid) {
                System.out.print("Time (HH:MM:SS): ");
                time = scanner.nextLine().trim();
                try {
                    //Trying to parse the time to check if it's in valid format
                    LocalTime.parse(time);
                    valid = true; // Time is valid
                } catch (Exception e) {
                    System.out.println("Invalid time format. Please enter the time in HH:MM:SS format.");
                }
            }
            return time;
        }

        //Validate Amount Input (numeric value)
        public static double getValidAmount(Scanner scanner) {
            double amount = 0.0;
            boolean valid = false;

            while (!valid) {
                System.out.print("Amount: ");
                String input = scanner.nextLine().trim();
                try {
                    amount = Double.parseDouble(input);
                    valid = true; // Amount is valid
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount. Please enter a numeric value.");
                }
            }
            return amount;
        }

        // Validate Non-Empty Input (for fields like Description and Vendor)
        public static String getNonEmptyInput(Scanner scanner, String fieldName) {
            String input = "";
            while (input.trim().isEmpty()) {
                System.out.print(fieldName + ": ");
                input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println(fieldName + " cannot be empty.");
                }
            }
            return input;
        }
    }
