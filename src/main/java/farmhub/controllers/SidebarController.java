package farmhub.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.List;

public class SidebarController {
    @FXML
    private VBox navItems;
    @FXML
    private Label dashboardLabel;
    @FXML
    private Label inventoryLabel;
    @FXML
    private Label produceLabel;
    @FXML
    private Label salesLabel;
    @FXML
    private Label weatherLabel;

    @FXML
    public void initialize() {
        // List of all labels for easier management
        List<Label> labels = List.of(dashboardLabel, inventoryLabel, produceLabel, salesLabel, weatherLabel);

        // Apply default style and hover effect to all labels
        labels.forEach(label -> {
            setDefaultStyle(label);

            label.setOnMouseEntered(event -> label.setStyle(
                    "-fx-background-color: #388E3C; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 5 10;"
            ));

            label.setOnMouseExited(event -> {
                if (!label.getStyle().contains("white")) { // Not active
                    setDefaultStyle(label);
                }
            });
        });
    }

    private void setDefaultStyle(Label label) {
        label.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: normal; " +
                        "-fx-padding: 5 10;" +
                        "-fx-cursor: hand;"
        );
    }

    private void setActive(Label activeLabel) {
        List<Label> labels = List.of(dashboardLabel, inventoryLabel, produceLabel, salesLabel, weatherLabel);

        // Reset all labels to default
        labels.forEach(this::setDefaultStyle);

        // Apply active style to clicked label
        activeLabel.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #2E7D32; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 5 10;" +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
        );
    }

    @FXML
    public void handleDashboardClick() {
        setActive(dashboardLabel);
        loadView("Dashboard", "/views/dashboard/dashboard.fxml");
    }

    @FXML
    public void handleInventoryClick() {
        setActive(inventoryLabel);
        loadView("Inventory", "/views/dashboard/inventory.fxml");
    }

    @FXML
    public void handleProduceClick() {
        setActive(produceLabel);
        loadView("Produce", "/views/dashboard/produce.fxml");
    }

    @FXML
    public void handleSalesClick() {
        setActive(salesLabel);
        loadView("Sales", "/views/dashboard/sales.fxml");
    }

    @FXML
    public void handleWeatherClick() {
        setActive(weatherLabel);
        loadView("Weather", "/views/dashboard/weather.fxml");
    }

    private void loadView(String title, String fxmlPath) {

        MainController.getInstance().loadView(fxmlPath, title);
    }
}
