package manager;

import interfaces.AdvancedSearch;
import models.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseManager implements AdvancedSearch {
    private final List<Expense> expenses = new ArrayList<>();

    public void loadExpenses(List<Expense> loadedExpenses) {
        if (loadedExpenses != null) {
            expenses.clear();
            expenses.addAll(loadedExpenses);
        }
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        System.out.println("Expense added successfully.");
    }

    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        System.out.println("\n===== Expense List =====");
        for (Expense expense : expenses) {
            System.out.println(expense);
            System.out.println("----------------------------");
        }
    }

    public void searchExpense(String title) {
        boolean found = false;
        for (Expense expense : expenses) {
            if (expense.getTitle().equalsIgnoreCase(title)) {
                System.out.println(expense);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Expense not found.");
        }
    }

    @Override
    public void searchByCategory(String category) {
        boolean found = false;
        System.out.println("\nCategory : " + category);
        for (Expense expense : expenses) {
            if (expense.getCategory().equalsIgnoreCase(category)) {
                System.out.println(expense);
                System.out.println("----------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No expenses found.");
        }
    }

    public void updateExpense(int id, String newTitle, String newCategory, double newAmount, String newDate) {
        boolean found = false;
        for (Expense expense : expenses) {
            if (expense.getExpenseId() == id) {
                expense.setTitle(newTitle);
                expense.setCategory(newCategory);
                expense.setAmount(newAmount);
                expense.setDate(newDate);
                found = true;
                System.out.println("Expense Updated Successfully.");
                break;
            }
        }
        if (!found) {
            System.out.println("Expense ID not found.");
        }
    }

    public void deleteExpense(int id) {
        Expense target = null;
        for (Expense expense : expenses) {
            if (expense.getExpenseId() == id) {
                target = expense;
                break;
            }
        }
        if (target != null) {
            expenses.remove(target);
            System.out.println("Expense Deleted Successfully.");
        } else {
            System.out.println("Expense ID Not Found.");
        }
    }

    public double calculateTotalExpense() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    public void displayTotalAmount() {
        System.out.println("Total Expense Amount : INR " + calculateTotalExpense());
    }

    public void categoryWiseExpense(String category) {
        searchByCategory(category);
    }

    public void highestExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses available.");
            return;
        }
        Expense highest = expenses.get(0);
        for (Expense expense : expenses) {
            if (expense.getAmount() > highest.getAmount()) {
                highest = expense;
            }
        }
        System.out.println("\nHighest Expense");
        System.out.println(highest);
    }

    public void lowestExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses available.");
            return;
        }
        Expense lowest = expenses.get(0);
        for (Expense expense : expenses) {
            if (expense.getAmount() < lowest.getAmount()) {
                lowest = expense;
            }
        }
        System.out.println("\nLowest Expense");
        System.out.println(lowest);
    }

    public void generateReport() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses available to generate report.");
            return;
        }
        StringBuilder report = new StringBuilder();
        report.append("\n========== Expense Report ==========");
        for (Expense expense : expenses) {
            report.append("\n")
                  .append(expense.getExpenseId())
                  .append(" | ")
                  .append(expense.getTitle())
                  .append(" | ")
                  .append(expense.getCategory())
                  .append(" | INR ")
                  .append(expense.getAmount());
        }
        report.append("\n\nTotal Expense : INR " + calculateTotalExpense());
        System.out.println(report);
    }

    public List<Expense> getExpenses() {
        return new ArrayList<>(expenses);
    }
}
