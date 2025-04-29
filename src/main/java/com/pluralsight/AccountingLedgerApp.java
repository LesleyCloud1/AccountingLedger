package com.pluralsight;
import java.io.File;
import java.util.Scanner;
import java.io.FileWriter:
import java.io.IOException:

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

            if (choice.equals("1")) {
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
                try (FileWriter = new FileWriter("transactions.csv", true)) {
                    writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");
                    System.out.println("Deposit saved to transactions.csv");
                } catch (IOException e) {
                    System.out.println("Error saving deposit: " + e.getMessage());
                }

            } else if (choice.equals("2")) {
                System.out.println("\n---- Make Payment ----"));
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
                try (FileWriter = new FileWriter("transactions.csv", true)) {
                    writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");
                    System.out.println("Payment saved to transactions.csv");
                } catch (IOException e) {
                    System.out.println("Error saving deposit: " + e.getMessage());


            } else if (choice.equals("3")) {
                System.out.println("View Account Transactions Selected");
            } else if (choice.equals("4")) {
                System.out.println("Have a nice day!");
                running = false; //properly exit the loop
            } else {
                System.out.println("Invalid, select options #1-4");
            }
        }
    }
}
