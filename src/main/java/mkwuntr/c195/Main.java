package mkwuntr.c195;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the application.
 * This class launches the JavaFX application and loads the login form as the initial view.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     * @param stage The first stage for the application is the Login
     * @throws IOException if there's an error loading the FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Client Schedule Management System");
        stage.setScene(scene);
        stage.show();
    }
}
