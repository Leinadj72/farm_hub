package farmhub.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import farmhub.dao.UserDAO;
import java.io.IOException;

public class RegisterController extends BaseController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private Label phoneErrorLabel;

    @FXML
    private void handleLoginRedirect(ActionEvent event) {
        navigateTo("/views/account/login.fxml", event);
    }

    @FXML
    private void handleRegisterButton(ActionEvent event) {
        emailErrorLabel.setText("");
        phoneErrorLabel.setText("");
        passwordErrorLabel.setText("");

        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if (username.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Registration Failed", "All fields are required!", Alert.AlertType.ERROR);
            return;
    	}
    	
    	if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            emailErrorLabel.setText("Enter a valid email address.");
            return;
    	}

    	if (!phoneNumber.matches("\\d{10,15}")) {
            phoneErrorLabel.setText("Enter a valid phone number (10-15 digits).");
            return;
    	}

        if (!password.equals(confirmPassword)) {
            passwordErrorLabel.setText("Passwords do not match!");
            return;
        }

        if (UserDAO.isEmailTaken(email)) {
            showAlert("Registration Failed", "Email is already taken!", Alert.AlertType.ERROR);
            return;
        }

        boolean isRegistered = UserDAO.registerUser(username, email, phoneNumber, password);
        if (isRegistered) {
            showAlert("Registration Successful", "You have successfully registered!", Alert.AlertType.INFORMATION);
            navigateToLogin(event);
        } else {
            showAlert("Registration Failed", "An error occurred. Please try again later.", Alert.AlertType.ERROR);
        }
    }

    private void navigateToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/account/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the login page.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
