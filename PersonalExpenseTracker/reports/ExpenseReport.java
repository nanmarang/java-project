package reports;

import interfaces.Printable;
import manager.ExpenseManager;
import models.Expense;

public class ExpenseReport extends Report implements Printable {
    private final ExpenseManager manager;

    public ExpenseReport(ExpenseManager manager) {
        this.manager = manager;
    }

    @Override
    public void generateReport() {
        reportHeader();
        for (Expense expense : manager.getExpenses()) {
            System.out.printf("%d | %s | %s | INR %.2f\n",
                    expense.getExpenseId(),
                    expense.getTitle(),
                    expense.getCategory(),
                    expense.getAmount());
        }
    }

    @Override
    public void printReport() {
        generateReport();
    }
}
