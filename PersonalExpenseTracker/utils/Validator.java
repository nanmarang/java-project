package utils;

public class Validator {
    public static boolean validAmount(double amount) {
        return amount > 0;
    }

    public static boolean validTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }
}
