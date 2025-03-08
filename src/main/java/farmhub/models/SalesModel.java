package farmhub.models;

public class SalesModel {
    private String date;
    private String produce;
    private int quantitySold;
    private double totalAmount;

    public SalesModel(String date, String produce, int quantitySold, double totalAmount) {
        this.date = date;
        this.produce = produce;
        this.quantitySold = quantitySold;
        this.totalAmount = totalAmount;
    }

    public String getDate() {
        return date;
    }

    public String getProduce() {
        return produce;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        return produce + " - " + quantitySold + " sold on " + date;
    }
}
