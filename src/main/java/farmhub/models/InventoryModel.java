package farmhub.models;

public class InventoryModel {
    private int id;
    private int produceId;
    private String name;  // Changed to match schema
    private int currentStock;

    public InventoryModel(int id, int produceId, String name, int currentStock) {
        this.id = id;
        this.produceId = produceId;
        this.name = name;
        this.currentStock = currentStock;
    }

    public int getId() {
        return id;
    }

    public int getProduceId() {
        return produceId;
    }

    public String getName() {
        return name;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    @Override
    public String toString() {
        return name + " - Stock: " + currentStock;
    }
}
