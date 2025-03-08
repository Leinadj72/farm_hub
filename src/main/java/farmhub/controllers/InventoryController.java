package farmhub.controllers;

import farmhub.dao.InventoryDAO;
import farmhub.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import farmhub.models.InventoryModel;

import java.util.List;

public class InventoryController {

    private int farmerId;

    @FXML private TableView<InventoryModel> inventoryTable;
    @FXML private TableColumn<InventoryModel, String> colProduceName;
    @FXML private TableColumn<InventoryModel, Integer> colStock;

    @FXML private TextField stockInput;
    @FXML private Button updateStockButton;

    public void initialize() {
        if (SessionManager.getInstance().isUserLoggedIn()) {
            farmerId = SessionManager.getInstance().getCurrentUser().getId();  // Get the logged-in farmer's ID
            loadInventoryData();
        }
    }

    private void loadInventoryData() {
        List<InventoryModel> inventoryList = InventoryDAO.getInventory(farmerId);

        // Set table column values
        colProduceName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("currentStock"));

        // Load data into table
        inventoryTable.getItems().setAll(inventoryList);
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
