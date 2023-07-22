package mkwuntr.c195;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        }
    }

    @FXML
    private void handleLoginClick() {

    }

    @FXML
    private void handleToggleClick() {

    }

    @FXML
    private void handleExitClick() {

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

