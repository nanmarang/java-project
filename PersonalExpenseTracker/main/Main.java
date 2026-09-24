package main;

import manager.ExpenseManager;
import manager.SearchManager;
import models.Expense;
import reports.ExpenseReport;
import utils.DateUtil;
import utils.FileManager;
import utils.Validator;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();
        SearchManager searchManager = new SearchManager(manager);
        ExpenseReport report = new ExpenseReport(manager);
        FileManager fileManager = new FileManager();

        List<Expense> loadedExpenses = fileManager.loadExpenses();
        manager.loadExpenses(loadedExpenses);

        int choice;
        do {
            printMenu();
            choice = getIntInput(sc, "Enter Choice : ");
            switch (choice) {
                case 1 -> addExpense(sc, manager);
                case 2 -> manager.viewExpenses();
                case 3 -> searchExpense(sc, searchManager);
                case 4 -> updateExpense(sc, manager);
                case 5 -> deleteExpense(sc, manager);
                case 6 -> manager.displayTotalAmount();
                case 7 -> categoryWiseReport(sc, manager);
                case 8 -> manager.highestExpense();
                case 9 -> manager.lowestExpense();
                case 10 -> report.printReport();
                case 11 -> {
                    fileManager.saveExpenses(manager);
                    System.out.println("Thank You!");
                }
                default -> System.out.println("Invalid Choice.");
            }
        } while (choice != 11);

        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n===================================");
        System.out.println(" PERSONAL EXPENSE TRACKER");
        System.out.println("===================================");
        System.out.println("1. Add Expense");
        System.out.println("2. View Expenses");
        System.out.println("3. Search Expense");
        System.out.println("4. Update Expense");
        System.out.println("5. Delete Expense");
        System.out.println("6. Total Expense");
        System.out.println("7. Category Wise Report");
        System.out.println("8. Highest Expense");
        System.out.println("9. Lowest Expense");
        System.out.println("10. Generate Report");
        System.out.println("11. Exit");
    }

    private static int getIntInput(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double getDoubleInput(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    private static void addExpense(Scanner sc, ExpenseManager manager) {
        int id = getIntInput(sc, "Expense ID : ");
        System.out.print("Title : ");
        String title = sc.nextLine().trim();
        if (!Validator.validTitle(title)) {
            System.out.println("Invalid title.");
            return;
        }
        System.out.print("Category : ");
        String category = sc.nextLine().trim();
        if (category.isEmpty()) {
            category = "OTHERS";
        }
        double amount = getDoubleInput(sc, "Amount : ");
        if (!Validator.validAmount(amount)) {
            System.out.println("Amount must be greater than zero.");
            return;
        }
        System.out.print("Date (dd-MM-yyyy) or leave blank for today: ");
        String dateInput = sc.nextLine().trim();
        String date = dateInput.isEmpty() ? DateUtil.getCurrentDate() : dateInput;
        if (!DateUtil.isValidDate(date)) {
            System.out.println("Invalid date format. Use dd-MM-yyyy.");
            return;
        }
        Expense expense = new Expense(id, title, category, amount, date);
        manager.addExpense(expense);
    }

    private static void searchExpense(Scanner sc, SearchManager searchManager) {
        System.out.print("Search by Title or Category (t/c) : ");
        String searchType = sc.nextLine().trim();
        if (searchType.equalsIgnoreCase("c")) {
            System.out.print("Enter Category : ");
            String categorySearch = sc.nextLine().trim();
            searchManager.searchByCategory(categorySearch);
        } else {
            System.out.print("Enter Title : ");
            String titleSearch = sc.nextLine().trim();
            searchManager.searchExpense(titleSearch);
        }
    }

    private static void updateExpense(Scanner sc, ExpenseManager manager) {
        int updateId = getIntInput(sc, "Expense ID : ");
        System.out.print("New Title : ");
        String newTitle = sc.nextLine().trim();
        System.out.print("New Category : ");
        String newCategory = sc.nextLine().trim();
        double newAmount = getDoubleInput(sc, "New Amount : ");
        System.out.print("New Date (dd-MM-yyyy) : ");
        String newDate = sc.nextLine().trim();
        if (!DateUtil.isValidDate(newDate)) {
            System.out.println("Invalid date format. Use dd-MM-yyyy.");
            return;
        }
        manager.updateExpense(updateId, newTitle, newCategory, newAmount, newDate);
    }

    private static void deleteExpense(Scanner sc, ExpenseManager manager) {
        int deleteId = getIntInput(sc, "Expense ID : ");
        manager.deleteExpense(deleteId);
    }

    private static void categoryWiseReport(Scanner sc, ExpenseManager manager) {
        System.out.print("Enter Category : ");
        String category = sc.nextLine().trim();
        manager.categoryWiseExpense(category);
    }
}
