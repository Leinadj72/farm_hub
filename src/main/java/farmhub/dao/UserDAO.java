package farmhub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import farmhub.utils.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import farmhub.session.CurrentUser;
import farmhub.session.SessionManager;

public class UserDAO {
    // Method to register a new user
    public static boolean registerUser(String username, String email,String phoneNumber, String password) {
        String insertQuery = "INSERT INTO farmers (username, email, password, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.setString(4, phoneNumber);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to validate user login
    public static boolean validateUser(String email, String password) {
        String query = "SELECT id, username, password FROM farmers WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (BCrypt.checkpw(password, storedPassword)) { // Verify password
                    int id = resultSet.getInt("id"); // Get user id
                    String username = resultSet.getString("username");

                    // Pass id as farmerId in CurrentUser
                    SessionManager.getInstance().setCurrentUser(new CurrentUser(id, username, email, id));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to fetch user details by email
    public static CurrentUser getUserByEmail(String email) {
        String query = "SELECT id, username, email FROM farmers WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id"); // Get user id
                String username = resultSet.getString("username");

                // Pass id as farmerId in CurrentUser
                return new CurrentUser(id, username, email, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Method to check if an email is already taken
    public static boolean isEmailTaken(String email) {
        String query = "SELECT 1 FROM farmers WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // Return true if a result is found

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
