package farmhub.models;

public class ProduceModel {
    private int id;
    private int farmerId;
    private String name;
    private int quantity;
    private double price;
    private String unit;

    public ProduceModel(int id, int farmerId, String name, int quantity, double price, String unit) {
        this.id = id;
        this.farmerId = farmerId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return name + " (" + quantity + " " + unit + ")";
    }
}
