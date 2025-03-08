package farmhub.controllers;

import farmhub.session.CurrentUser;
import farmhub.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController extends BaseController {
    @FXML
    private Label loggedInUserLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Label navbarTitle;
    @FXML
    private StackPane mainContent;

    private static MainController instance;

    @FXML
    public void initialize() {
        instance = this;
        CurrentUser currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            loggedInUserLabel.setText("Welcome, " + currentUser.getUsername());
        } else {
            loggedInUserLabel.setText("Welcome, Guest");
        }

        if (logoutButton != null) {
            logoutButton.setOnAction(this::handleLogout);
        }
    }

    public static MainController getInstance() {
        if (instance == null) {
            System.out.println("ERROR: MainController instance is null!");
        }
        return instance;
    }

    public void loadView(String fxmlPath, String title) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            mainContent.getChildren().clear(); // Clear old content
            mainContent.getChildren().add(view); // Add new content

            navbarTitle.setText(title); // Update navbar title

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.getInstance().clearSession();
        showAlert("Logout Successful", "You have been logged out.", Alert.AlertType.INFORMATION);
        navigateToLogin();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/account/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load login screen.", Alert.AlertType.ERROR);
        }
    }
}