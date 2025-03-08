package farmhub.dao;

import farmhub.models.InventoryModel;
import farmhub.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    // Get all inventory items for a farmer
    public static List<InventoryModel> getInventory(int farmerId) {
        List<InventoryModel> inventoryList = new ArrayList<>();
        String query = """
                SELECT i.id, i.produce_id, p.name, i.current_stock
                FROM inventory i
                JOIN produce p ON i.produce_id = p.id
                WHERE p.farmer_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, farmerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                inventoryList.add(new InventoryModel(
                        rs.getInt("id"),
                        rs.getInt("produce_id"),
                        rs.getString("name"),
                        rs.getInt("current_stock")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inventoryList;
    }


    // Get count of low stock items
    public static int getLowStockCount(int farmerId) {
        String query = """
                SELECT COUNT(*) FROM inventory
                WHERE produce_id IN (SELECT id FROM produce WHERE farmer_id = ?)
                AND current_stock < 10
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, farmerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean addNewProduceToInventory(int produceId) {
        String query = "INSERT INTO inventory (produce_id, current_stock) VALUES (?, 0)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, produceId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
