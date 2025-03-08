package farmhub.dao;

import farmhub.models.ProduceModel;
import farmhub.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduceDAO {

    // Get all produce for a farmer
    public static List<ProduceModel>getProduce(int farmerId) {
        List<ProduceModel>produceList = new ArrayList<>();
        String query = "SELECT id, name, quantity, price, unit FROM produce WHERE farmer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, farmerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                produceList.add(new ProduceModel(
                        rs.getInt("id"),
                        farmerId,
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("unit")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produceList;
    }

    // Add new produce
    public static void addProduce(int farmerId, String name, int quantity, double price, String unit) {
        String query = "INSERT INTO produce (farmer_id, name, quantity, price, unit) VALUES (?, ?, ?, ?, ?)";
        String inventoryQuery = "INSERT INTO inventory (produce_id, current_stock) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);  // Start transaction

            // Insert into produce table
            stmt.setInt(1, farmerId);
            stmt.setString(2, name);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);
            stmt.setString(5, unit);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int produceId = generatedKeys.getInt(1);

                    // Insert into inventory
                    try (PreparedStatement inventoryStmt = conn.prepareStatement(inventoryQuery)) {
                        inventoryStmt.setInt(1, produceId);
                        inventoryStmt.setInt(2, quantity);
                        inventoryStmt.executeUpdate();
                    }
                }
            }

            conn.commit();  // Commit transaction

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Update produce details
    public static void updateProduce(int produceId, String name, int quantity, double price, String unit) {
        String query = "UPDATE produce SET name = ?, quantity = ?, price = ?, unit = ? WHERE id = ?";
        String inventoryQuery = "UPDATE inventory SET current_stock = ? WHERE produce_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);  // Start transaction

            // Update produce table
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setInt(2, quantity);
                stmt.setDouble(3, price);
                stmt.setString(4, unit);
                stmt.setInt(5, produceId);
                stmt.executeUpdate();
            }

            // Update inventory table
            try (PreparedStatement inventoryStmt = conn.prepareStatement(inventoryQuery)) {
                inventoryStmt.setInt(1, quantity);
                inventoryStmt.setInt(2, produceId);
                inventoryStmt.executeUpdate();
            }

            conn.commit();  // Commit transaction

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getInventoryStock(int produceId) {
        String query = "SELECT current_stock FROM inventory WHERE produce_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, produceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("current_stock");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



    // Delete produce
    public static void deleteProduce(int produceId) {
        String produceQuery = "DELETE FROM produce WHERE id = ?";
        String inventoryQuery = "DELETE FROM inventory WHERE produce_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);  // Start transaction

            // Delete from inventory first
            try (PreparedStatement inventoryStmt = conn.prepareStatement(inventoryQuery)) {
                inventoryStmt.setInt(1, produceId);
                inventoryStmt.executeUpdate();
            }

            // Then delete from produce
            try (PreparedStatement stmt = conn.prepareStatement(produceQuery)) {
                stmt.setInt(1, produceId);
                stmt.executeUpdate();
            }

            conn.commit();  // Commit transaction

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
