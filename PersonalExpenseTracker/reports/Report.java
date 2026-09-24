package reports;

public abstract class Report {
    public abstract void generateReport();

    protected void reportHeader() {
        System.out.println("==============================");
        System.out.println(" PERSONAL EXPENSE REPORT");
        System.out.println("==============================");
    }
}
