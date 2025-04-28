package com.pluralsight;
import java.util.Scanner;

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

            String choice = scanner.nextLine().trim();
            //trim removes extra spaces on user input
        if (choice.equals("1")) {
            System.out.println("Make Deposit Selected");
        } else if (choice.equals("2")) {
            System.out.println("Make Payment Selected");
        } else if (choice.equals("3")) {
            System.out.println("View Account Transactions Selected");
        } else if (choice.equals("4")) {
            System.out.println("Have a nice day!");
        }else {
            System.out.println("Invalid, select options #1-4");

        }

        }

    }

}
