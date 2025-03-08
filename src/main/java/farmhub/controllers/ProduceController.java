package farmhub.controllers;

import farmhub.dao.ProduceDAO;
import farmhub.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import farmhub.models.ProduceModel;

import java.util.List;

public class ProduceController {

    private int farmerId;

    @FXML private TableView<ProduceModel> produceTable;
    @FXML private TableColumn<ProduceModel, String> colName;
    @FXML private TableColumn<ProduceModel, Integer> colQuantity;
    @FXML private TableColumn<ProduceModel, Double> colPrice;
    @FXML private TableColumn<ProduceModel, String> colUnit;

    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> unitComboBox;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;

    private ProduceModel selectedProduce;

    public void initialize() {
        // Get the logged-in farmer's ID
        if (SessionManager.getInstance().isUserLoggedIn()) {
            farmerId = SessionManager.getInstance().getCurrentUser().getId();
            loadProduceData();
        }

        // Initialize unit ComboBox
        unitComboBox.getItems().addAll("kg", "liters", "pieces");

        // Handle row selection for editing
        produceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProduce = newSelection;
                populateFields(newSelection);
            }
        });
    }

    private void loadProduceData() {
        List<ProduceModel> produceList = ProduceDAO.getProduce(farmerId);

        for (ProduceModel produce : produceList) {
            int inventoryStock = ProduceDAO.getInventoryStock(produce.getId());
            if (produce.getQuantity() != inventoryStock) {
                System.out.println("Discrepancy detected for Produce ID: " + produce.getId());
            }
        }

        // Set table column values
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));

        // Load data into table
        produceTable.getItems().setAll(produceList);
    }


    private void populateFields(ProduceModel produce) {
        nameField.setText(produce.getName());
        quantityField.setText(String.valueOf(produce.getQuantity()));
        priceField.setText(String.valueOf(produce.getPrice()));
        unitComboBox.setValue(produce.getUnit());
    }

    @FXML
    private void handleSave() {
        String name = nameField.getText();
        String quantityText = quantityField.getText();
        String priceText = priceField.getText();
        String unit = unitComboBox.getValue();

        if (name.isEmpty() || quantityText.isEmpty() || priceText.isEmpty() || unit == null) {
            showAlert("Validation Error", "Please fill all fields.");
            return;
        }

        int quantity;
        double price;
        try {
            quantity = Integer.parseInt(quantityText);
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Quantity and Price must be numbers.");
            return;
        }

        if (selectedProduce == null) {
            ProduceDAO.addProduce(farmerId, name, quantity, price, unit);
        } else {
            ProduceDAO.updateProduce(selectedProduce.getId(), name, quantity, price, unit);
            selectedProduce = null; // Reset selection after update
        }

        clearFields();
        loadProduceData();
    }

    @FXML
    private void handleDelete() {
        if (selectedProduce != null) {
            ProduceDAO.deleteProduce(selectedProduce.getId());
            selectedProduce = null;
            clearFields();
            loadProduceData();
        } else {
            showAlert("Error", "No produce selected!");
        }
    }

    private void clearFields() {
        nameField.clear();
        quantityField.clear();
        priceField.clear();
        unitComboBox.setValue(null);
        selectedProduce = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
