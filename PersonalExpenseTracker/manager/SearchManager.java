package manager;

import interfaces.AdvancedSearch;
import models.Expense;

public class SearchManager implements AdvancedSearch {
    private final ExpenseManager manager;

    public SearchManager(ExpenseManager manager) {
        this.manager = manager;
    }

    @Override
    public void searchExpense(String title) {
        boolean found = false;
        for (Expense expense : manager.getExpenses()) {
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
        manager.searchByCategory(category);
    }
}
