package farmhub;

import farmhub.session.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader;

        // Check if user is logged in
        if (SessionManager.getInstance().isUserLoggedIn()) {
            loader = new FXMLLoader(getClass().getResource("/views/dashboard/main.fxml"));
        } else {
            loader = new FXMLLoader(getClass().getResource("/views/account/login.fxml"));
        }

        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("FarmHub");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
