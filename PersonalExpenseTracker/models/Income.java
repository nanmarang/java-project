package models;

public class Income extends Expense {
    private String source;

    public Income(int expenseId, String title, String category, double amount, String date, String source) {
        super(expenseId, title, category, amount, date);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return super.toString() + "\nSource     : " + source;
    }
}
