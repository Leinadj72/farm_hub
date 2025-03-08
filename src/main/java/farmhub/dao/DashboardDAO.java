package farmhub.dao;

import farmhub.session.SessionManager;
import farmhub.models.SalesModel;
import farmhub.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {

    // Get farmer ID from the database using session email
    public static int getFarmerId() {
        int farmerId = -1;
        if (SessionManager.getInstance().isUserLoggedIn()) {
            String userEmail = SessionManager.getInstance().getCurrentUser().getEmail();

            String query = "SELECT id FROM farmers WHERE email = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, userEmail);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    farmerId = rs.getInt("id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return farmerId;
    }

    // Get total sales (Calculate dynamically: SUM(quantity_sold * price))
    public static double getTotalSales(int farmerId) {
        String query = """
                SELECT COALESCE(SUM(s.quantity_sold * p.price), 0) AS total_sales
                FROM sales s\s
                JOIN produce p ON s.produce_id = p.id\s
                WHERE s.farmer_id = ?
               \s""";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, farmerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_sales");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Get total produce count for the logged-in farmer
    public static int getTotalProduceCount(int farmerId) {
        String query = "SELECT COUNT(*) FROM produce WHERE farmer_id = ?";
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

    // Get low stock count (produce with current stock < 10)
    public static int getLowStockCount(int farmerId) {
        String query = """
                SELECT COUNT(*) FROM inventory\s
                WHERE produce_id IN (SELECT id FROM produce WHERE farmer_id = ?)\s
                AND current_stock < 10
               \s""";
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

    // Get recent sales (Calculate total amount dynamically)
    public static List<SalesModel> getRecentSales(int farmerId) {
        List<SalesModel> sales = new ArrayList<>();
        String query = """
                SELECT s.sale_date, p.name, s.quantity_sold, (s.quantity_sold * p.price) AS total_amount
                FROM sales s\s
                JOIN produce p ON s.produce_id = p.id\s
                WHERE s.farmer_id = ?\s
                ORDER BY s.sale_date DESC\s
                LIMIT 10
               \s""";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, farmerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(new SalesModel(rs.getString("sale_date"), rs.getString("name"),
                        rs.getInt("quantity_sold"), rs.getDouble("total_amount")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sales;
    }

    public static List<SalesModel> getMonthlySales(int farmerId) {
        List<SalesModel> sales = new ArrayList<>();
        String query = """
            SELECT DATE_FORMAT(s.sale_date, '%Y-%m') AS month,\s
                   p.name AS produce,\s
                   SUM(s.quantity_sold * p.price) AS total_amount
            FROM sales s
            JOIN produce p ON s.produce_id = p.id
            WHERE s.farmer_id = ?
            GROUP BY month, produce
            ORDER BY month ASC;
           \s""";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, farmerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(new SalesModel(rs.getString("month"), rs.getString("produce"),
                        0, rs.getDouble("total_amount")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sales;
    }
}
