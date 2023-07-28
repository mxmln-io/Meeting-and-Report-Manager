package mkwuntr.c195;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginFormController {
    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private ToggleButton languageToggleButton;

    @FXML
    private Button exitButton;

    @FXML
    private Label locationLabel;

    private ResourceBundle resources;

    public void initialize() {
        // Get the default locale to determine the user's computer language setting
        Locale defaultLocale = Locale.getDefault();

        // Load the appropriate resource bundle based on the default locale
        resources = ResourceBundle.getBundle("LoginForm", defaultLocale);

        // Set the location label text based on the user's ZoneId
        ZoneId zoneId = ZoneId.systemDefault();
        locationLabel.setText(resources.getString("location") + ": " + zoneId.getId());

        // Bind the toggle button's text to dynamically update the language setting
        StringBinding languageTextBinding = Bindings.when(languageToggleButton.selectedProperty())
                .then(resources.getString("english"))
                .otherwise(resources.getString("french"));
        languageToggleButton.textProperty().bind(languageTextBinding);
    }

    @FXML
    private void handleLogin() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        // Perform validation logic for username and password
        if (username.isEmpty() || password.isEmpty()) {
            // Display the error message based on the current language setting
            String errorMessage = resources.getString("error");
            // You can show the error message in a dialog or update a label on the form
            System.out.println(errorMessage);
        } else {
            // Proceed with login logic

            // Assume login is successful, then open the main screen
            try {
                // Load the FXML file for the main screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScreen.fxml"));
                Parent mainScreenRoot = loader.load();

                // Create a new stage for the main screen
                Stage mainScreenStage = new Stage();
                mainScreenStage.setTitle("Main Screen");
                mainScreenStage.setScene(new Scene(mainScreenRoot));
                mainScreenStage.show();

                // Close the current login stage
                Stage loginStage = (Stage) usernameTextField.getScene().getWindow();
                loginStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void handleLoginClick() {

    }

    @FXML
    private void handleToggleClick() {

    }

    /**
     * This method handles the action of the Exit button.
     * When clicked, it closes the application.
     */
    @FXML
    private void handleExitClick() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void displayErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

