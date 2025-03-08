package farmhub.dao;

import farmhub.models.SalesModel;
import farmhub.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {

    // Get all sales for a farmer
    public static List<SalesModel> getSales(int farmerId) {
        List<SalesModel> salesList = new ArrayList<>();
        String query = """
        SELECT s.id, s.sale_date, s.produce_id, p.name AS produce, s.quantity_sold, (s.quantity_sold * p.price) AS total_amount
        FROM sales s
        JOIN produce p ON s.produce_id = p.id
        WHERE s.farmer_id = ?
        ORDER BY s.sale_date DESC
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, farmerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                salesList.add(new SalesModel(
                        rs.getString("sale_date"),
                        rs.getString("produce"), // Use produce name instead of produce_id
                        rs.getInt("quantity_sold"),
                        rs.getDouble("total_amount")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salesList;
    }


    // Record a new sale and update inventory
    public static boolean recordSale(int farmerId, int produceId, int quantitySold) {
        String insertSale = """
            INSERT INTO sales (farmer_id, produce_id, quantity_sold, total_amount, sale_date)
            SELECT ?, ?, ?, (p.price * ?), NOW()
            FROM produce p WHERE p.id = ?
        """;

        String checkInventory = "SELECT current_stock FROM inventory WHERE produce_id = ?";
        String checkProduceQuantity = "SELECT quantity FROM produce WHERE id = ?";
        String addToInventory = "INSERT INTO inventory (produce_id, current_stock) VALUES (?, ?)";
        String updateInventory = "UPDATE inventory SET current_stock = current_stock - ? WHERE produce_id = ?";
        String updateProduce = "UPDATE produce SET quantity = quantity - ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement saleStmt = conn.prepareStatement(insertSale);
             PreparedStatement checkInventoryStmt = conn.prepareStatement(checkInventory);
             PreparedStatement checkProduceStmt = conn.prepareStatement(checkProduceQuantity);
             PreparedStatement addStmt = conn.prepareStatement(addToInventory);
             PreparedStatement inventoryStmt = conn.prepareStatement(updateInventory);
             PreparedStatement produceStmt = conn.prepareStatement(updateProduce)) {

            conn.setAutoCommit(false); // Start transaction

            // 1. Check inventory stock first
            checkInventoryStmt.setInt(1, produceId);
            ResultSet inventoryResult = checkInventoryStmt.executeQuery();
            boolean isInInventory = false;
            int currentStock = 0;

            if (inventoryResult.next()) {
                currentStock = inventoryResult.getInt("current_stock");
                isInInventory = true;
            }

            // 2. If not in inventory, check produce quantity and add to inventory
            if (!isInInventory) {
                checkProduceStmt.setInt(1, produceId);
                ResultSet produceResult = checkProduceStmt.executeQuery();
                if (produceResult.next()) {
                    currentStock = produceResult.getInt("quantity");
                    if (currentStock < quantitySold) {
                        System.out.println("Insufficient stock in produce table.");
                        return false;
                    }

                    // Add to inventory if not present
                    addStmt.setInt(1, produceId);
                    addStmt.setInt(2, currentStock);
                    addStmt.executeUpdate();
                } else {
                    System.out.println("No record found in produce table.");
                    return false;
                }
            }

            // 3. Check if stock is sufficient
            if (currentStock < quantitySold) {
                System.out.println("Insufficient stock for produce ID: " + produceId);
                return false; // Prevent the sale
            }

            // 4. Record the sale
            saleStmt.setInt(1, farmerId);
            saleStmt.setInt(2, produceId);
            saleStmt.setInt(3, quantitySold);
            saleStmt.setInt(4, quantitySold); // Quantity again for total_amount calculation
            saleStmt.setInt(5, produceId);    // Produce ID for price lookup
            saleStmt.executeUpdate();

            // 5. Deduct sold quantity from inventory
            inventoryStmt.setInt(1, quantitySold);
            inventoryStmt.setInt(2, produceId);
            inventoryStmt.executeUpdate();

            // 6. Deduct sold quantity from produce quantity
            produceStmt.setInt(1, quantitySold);
            produceStmt.setInt(2, produceId);
            produceStmt.executeUpdate();

            conn.commit(); // Commit transaction
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
