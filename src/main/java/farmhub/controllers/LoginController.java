package farmhub.controllers;

import farmhub.session.CurrentUser;
import farmhub.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import farmhub.dao.UserDAO;
import farmhub.utils.NavigationUtil;

public class LoginController extends BaseController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private void handleRegisterRedirect(ActionEvent event) {
        NavigationUtil.navigateTo("/views/account/register.fxml", event);
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");

        String email = emailField.getText();
        String password = passwordField.getText();

        boolean isValidUser = UserDAO.validateUser(email, password);
        if (!isValidUser) {
            showAlert("Login Failed", "Invalid email or password!", Alert.AlertType.ERROR);
            return;
        }

        // Create and save session
        CurrentUser user = UserDAO.getUserByEmail(email);
        if (user != null) {
            SessionManager.getInstance().setCurrentUser(user);
            showAlert("Login Successful", "Welcome, " + user.getUsername() + "!", Alert.AlertType.INFORMATION);
            NavigationUtil.navigateTo("/views/dashboard/main.fxml", event);
        } else {
            showAlert("Error", "User details not found.", Alert.AlertType.ERROR);
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