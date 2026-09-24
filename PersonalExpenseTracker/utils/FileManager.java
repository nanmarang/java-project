package utils;

import manager.ExpenseManager;
import models.Expense;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String DATA_FILE = "data/expenses.txt";
    private static final String DELIMITER = ",";

    public List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return expenses;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] tokens = line.split(DELIMITER);
                if (tokens.length < 5) {
                    continue;
                }
                try {
                    int id = Integer.parseInt(tokens[0].trim());
                    String title = tokens[1].trim();
                    String category = tokens[2].trim();
                    double amount = Double.parseDouble(tokens[3].trim());
                    String date = tokens[4].trim();
                    expenses.add(new Expense(id, title, category, amount, date));
                } catch (NumberFormatException ex) {
                    // skip invalid records
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading from data file: " + ex.getMessage());
        }
        return expenses;
    }

    public void saveExpenses(ExpenseManager manager) {
        File file = new File(DATA_FILE);
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Expense expense : manager.getExpenses()) {
                writer.write(expense.getExpenseId() + DELIMITER);
                writer.write(expense.getTitle() + DELIMITER);
                writer.write(expense.getCategory() + DELIMITER);
                writer.write(expense.getAmount() + DELIMITER);
                writer.write(expense.getDate());
                writer.newLine();
            }
            System.out.println("Expenses saved to " + DATA_FILE);
        } catch (IOException ex) {
            System.out.println("Error saving expenses: " + ex.getMessage());
        }
    }
}
