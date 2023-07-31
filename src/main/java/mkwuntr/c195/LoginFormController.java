package mkwuntr.c195;

import dataaccessobjects.UserDAO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
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

    @FXML
    private Label languageLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private ResourceBundle resources;

    @FXML
    private UserDAO userDAO = new UserDAO();

    @FXML
    private Locale currentLocale;

    @FXML
    public void initialize() {
        // Get the default locale to determine the user's computer language setting
        currentLocale = Locale.getDefault();
        updateView();
    }

    private void updateView() {
        resources = ResourceBundle.getBundle("LoginForm", currentLocale);

        ZoneId zoneId = ZoneId.systemDefault();
        locationLabel.setText(resources.getString("location") + ": " + zoneId.getId());
        languageLabel.setText(resources.getString("language_label"));
        usernameLabel.setText(resources.getString("username_label"));
        passwordLabel.setText(resources.getString("password_label"));

        usernameTextField.setPromptText(resources.getString("username"));
        passwordTextField.setPromptText(resources.getString("password"));
        loginButton.setText(resources.getString("login"));
        exitButton.setText(resources.getString("exit"));
        languageToggleButton.setText(resources.getString("toggle"));
    }

    @FXML
    private void handleLogin() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        // Perform validation logic for username and password
        if (username.isEmpty() || password.isEmpty()) {
            // Display the error message based on the current language setting
            String errorMessage = resources.getString("error");
            displayErrorMessage(errorMessage); // Use the displayErrorMessage method to show an error dialog
        } else {
            // Proceed with login logic
            try {
                ObservableList<User> users = userDAO.getAllUsersObservable();
                boolean isAuthenticated = users.stream()
                        .anyMatch(user -> user.getName().equals(username) && user.getPassword().equals(password));

                if (isAuthenticated) {
                    // Login is successful, continue to next screen or operations
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScreen.fxml"), resources);
                        Parent root = loader.load();
                        Stage loginStage = (Stage) loginButton.getScene().getWindow();
                        Stage mainStage = new Stage();
                        mainStage.setScene(new Scene(root));
                        mainStage.show();
                        loginStage.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        String errorMessage = resources.getString("error_loading_scene");
                        displayErrorMessage(errorMessage);
                    }
                } else {
                    // Invalid credentials, display an error message
                    String errorMessage = resources.getString("login_error");
                    displayErrorMessage(errorMessage);
                }
            } catch (SQLException e) {
                // Handle SQL exception
                e.printStackTrace();
                String errorMessage = resources.getString("database_error");
                displayErrorMessage(errorMessage);
            }
        }
    }



    @FXML
    private void handleLoginClick() {
        handleLogin();
    }

    @FXML
    private void handleToggleClick() {
        if (currentLocale.getLanguage().equals("en")) {
            currentLocale = new Locale("fr");
        } else {
            currentLocale = new Locale("en");
        }
        updateView();
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

