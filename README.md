 # 💰 Accounting Ledger Application

Welcome to my application! This Java-based Command Line Interface (CLI) program allows users to track financial 
transactions, including deposits and payments, options to view detailed ledger reports. It’s designed to help manage
personal or small business finances directly from the terminal.

This project was created as Capstone Project 1 for the Java Development Fundamentals course.

I’d like to thank:

🙏 My instructor, for guidance and support throughout this course

🤝 My peers, for collaboration and feedback

🌐 Online resources and documentation that helped deepen my understanding of Java concepts and best practices

---
The Home Screen is the main menu of the application. From here, users can:
### 🏠 Home Screen![screenshotshome-screen.png](Screenshots/screenshotshome-screen.png)
- Add a new deposit (`1`)
- Make a payment or debit (`2`)
- View the transaction ledger (`3`)
- Exit the application (`4`)

You can type a number (like 1) and press Enter to choose an option.
The app continues running until the user chooses to exit.

---
💵 How to Make a Deposit (Option 1)
Enter the date in YYYY-MM-DD format

Enter the time in HH:MM:SS format

Add a short description and vendor name

Type the amount (positive number)

Your deposit will be saved in a file called transactions.csv.

---
💳 How to Make a Payment (Option 2)
Same steps as deposit, but your amount will automatically be saved as negative (expense).
---
## 📌 Features


### 📒 Ledger View

 ![LedgerView.png](Screenshots/LedgerView.png)

The Ledger View displays all financial transactions, with the most recent ones shown first. Users can filter the ledger to:
- View all transactions (`A`)
- Show deposits only (`D`)
- Show payments/debits only (`P`)
- Access the Reports screen (`R`)
- Return to the Home screen (`H`)

---
- 📊 Reports:
- ![ReportsMenu.png](Screenshots/ReportsMenu.png)

This gives you access to powerful filters:
- Month-to-Date
- Previous Month
- Year-to-Date
- Previous Year
- Search by Vendor
- 🔍 Custom Search (by date, vendor, description, amount)

- 💾 Saves all data to `transactions.csv`

---

## 🛠️ Technologies Used

- Java 17+
- Java IO & NIO for File Handling
- Java Time API for date filtering
- Git + GitHub for version control

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/LesleyCloud1/AccountingLedger.git