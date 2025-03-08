package farmhub.controllers;

import farmhub.dao.ProduceDAO;
import farmhub.dao.SalesDAO;
import farmhub.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import farmhub.models.ProduceModel;
import farmhub.models.SalesModel;

import java.util.List;
import java.util.Optional;

public class SalesController {

    private int farmerId;

    @FXML private TableView<SalesModel> salesTable;
    @FXML private TableColumn<SalesModel, String> colDate;
    @FXML private TableColumn<SalesModel, String> colProduce;
    @FXML private TableColumn<SalesModel, Integer> colQuantity;
    @FXML private TableColumn<SalesModel, Double> colAmount;

    @FXML private ComboBox<ProduceModel> produceComboBox;
    @FXML private TextField quantityField;
    @FXML private Label totalAmountLabel;
    @FXML private Button recordSaleButton;

    public void initialize() {
        if (SessionManager.getInstance().isUserLoggedIn()) {
            farmerId = farmhub.dao.DashboardDAO.getFarmerId(); // Get logged-in farmer's ID
            loadSalesData();
            loadProduceList();
        }

        // Ensure the quantity field accepts only valid numbers
        quantityField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Allow only numbers
                quantityField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            calculateTotalAmount();
        });

        produceComboBox.setOnAction(event -> calculateTotalAmount());
    }

    private void loadSalesData() {
        List<SalesModel> salesList = SalesDAO.getSales(farmerId);

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colProduce.setCellValueFactory(new PropertyValueFactory<>("produce"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        salesTable.getItems().setAll(salesList);
    }

    private void loadProduceList() {
        List<ProduceModel> produceList = ProduceDAO.getProduce(farmerId);
        produceComboBox.getItems().setAll(produceList);
    }

    private void calculateTotalAmount() {
        try {
            ProduceModel selectedProduce = produceComboBox.getValue();
            String quantityText = quantityField.getText().trim();
            int quantity = quantityText.isEmpty() ? 0 : Integer.parseInt(quantityText);

            if (selectedProduce != null && quantity > 0) {
                double total = selectedProduce.getPrice() * quantity;
                totalAmountLabel.setText("₵" + String.format("%.2f", total));
            } else {
                totalAmountLabel.setText("₵0.00");
            }
        } catch (NumberFormatException ignored) {
            totalAmountLabel.setText("₵0.00");
        }
    }

    @FXML
    private void handleRecordSale() {
        ProduceModel selectedProduce = produceComboBox.getValue();
        if (selectedProduce == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a produce item.");
            return;
        }

        try {
            String quantityText = quantityField.getText().trim();
            if (quantityText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please enter a quantity.");
                return;
            }

            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be greater than zero.");
                return;
            }

            // Show a confirmation dialog before proceeding
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Sale");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Are you sure you want to record this sale?");
            
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = SalesDAO.recordSale(farmerId, selectedProduce.getId(), quantity);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Sale recorded successfully.");
                    loadSalesData();  // Refresh table
                    loadProduceList(); // Refresh stock levels
                    quantityField.clear();
                    totalAmountLabel.setText("₵0.00");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Insufficient stock for this sale.");
                }
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Enter a valid number.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

