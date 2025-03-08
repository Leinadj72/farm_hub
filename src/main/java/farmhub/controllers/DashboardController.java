package farmhub.controllers;

import farmhub.dao.DashboardDAO;
import farmhub.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import farmhub.models.SalesModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController {

    private int farmerId;

    @FXML private Label totalSalesLabel;
    @FXML private Label totalProduceLabel;
    @FXML private Label lowStockLabel;

    @FXML private TableView<SalesModel> salesTable;
    @FXML private TableColumn<SalesModel, String> colDate;
    @FXML private TableColumn<SalesModel, String> colProduce;
    @FXML private TableColumn<SalesModel, Integer> colQuantity;
    @FXML private TableColumn<SalesModel, Double> colAmount;

    @FXML private BarChart<String, Number> salesChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    public void initialize() {
        // Check if the user is logged in
        if (SessionManager.getInstance().isUserLoggedIn()) {
            // Get the farmer's ID using the session email
            farmerId = DashboardDAO.getFarmerId();

            // Verify if the farmerId is valid
            if (farmerId > 0) {
                // Bind Sales Table Columns
                colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                colProduce.setCellValueFactory(new PropertyValueFactory<>("produce"));
                colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
                colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

                // Load Dashboard Data
                loadDashboardData();
            }
        }
    }

    private void loadDashboardData() {
        // Load Totals
        totalSalesLabel.setText("₵" + DashboardDAO.getTotalSales(farmerId));
        totalProduceLabel.setText(String.valueOf(DashboardDAO.getTotalProduceCount(farmerId)));
        lowStockLabel.setText(String.valueOf(DashboardDAO.getLowStockCount(farmerId)));

        // Load Sales Data
        List<SalesModel> sales = DashboardDAO.getRecentSales(farmerId);
        salesTable.getItems().setAll(sales);
        salesTable.refresh();  // Refresh Table to display data

        // Load Sales Chart
        salesChart.getData().clear();  // Clear old data

        List<SalesModel> monthlySales = DashboardDAO.getMonthlySales(farmerId);
        Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

        // Group data by produce and add to series
        for (SalesModel sale : monthlySales) {
            String produce = sale.getProduce();
            String month = sale.getDate();  // Month is stored in date field here

            // Check if series for produce exists, otherwise create it
            seriesMap.putIfAbsent(produce, new XYChart.Series<>());
            seriesMap.get(produce).setName(produce);

            // Add data to the series for the corresponding produce
            seriesMap.get(produce).getData().add(new XYChart.Data<>(month, sale.getTotalAmount()));
        }

        // Add all series to the chart
        salesChart.getData().addAll(seriesMap.values());
    }

}
