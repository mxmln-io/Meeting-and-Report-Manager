package mkwuntr.c195;

import dataaccessobjects.UserDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for the Login Form.
 */
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

    /**
     * Initializes the form with locale-based content.
     */
    @FXML
    public void initialize() {
        currentLocale = Locale.getDefault();
        updateView();
    }

    /**
     * Updates the form elements based on the currently selected locale.
     */
    @FXML
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

    /**
     * Handles login logic.
     */
    @FXML
    private void handleLogin() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            String errorMessage = resources.getString("error");
            displayErrorMessage(errorMessage);
        } else {
            try {
                ObservableList<User> users = userDAO.getAllUsersObservable();
                boolean isAuthenticated = users.stream()
                        .anyMatch(user -> user.getName().equals(username) && user.getPassword().equals(password));
                recordLoginAttempt(username, isAuthenticated);

                if (isAuthenticated) {
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
                    String errorMessage = resources.getString("login_error");
                    displayErrorMessage(errorMessage);
                }
            } catch (SQLException e) {
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

    /**
     * Toggles the form's language between English and French.
     */
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
     * Closes the application.
     */
    @FXML
    private void handleExitClick() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an error message in a pop-up alert.
     *
     * @param message The error message to display.
     */
    @FXML
    private void displayErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Records the login attempt, with date, time, and success/failure.
     *
     * @param username The username attempted.
     * @param success  Boolean indicating if the attempt was successful.
     */
    private void recordLoginAttempt(String username, boolean success) {
        String appDir = System.getProperty("user.dir");
        String filePath = appDir + File.separator + "login_activity.txt";

        try (FileWriter writer = new FileWriter(filePath, true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bufferedWriter)) {
                String result = success ? "Successful" : "Failed";
                String entry = String.format("%s - Login %s for user: %s%n", LocalDateTime.now(), result, username);
                out.println(entry);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
