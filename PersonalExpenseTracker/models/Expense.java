package models;

public class Expense implements Cloneable {
    private int expenseId;
    private String title;
    private String category;
    private double amount;
    private String date;

    public Expense(int expenseId, String title, String category, double amount, String date) {
        this.expenseId = expenseId;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Expense ID : " + expenseId +
                "\nTitle      : " + title +
                "\nCategory   : " + category +
                "\nAmount     : INR " + amount +
                "\nDate       : " + date;
    }

    @Override
    public Expense clone() {
        try {
            return (Expense) super.clone();
        } catch (CloneNotSupportedException ex) {
            return new Expense(expenseId, title, category, amount, date);
        }
    }
}
