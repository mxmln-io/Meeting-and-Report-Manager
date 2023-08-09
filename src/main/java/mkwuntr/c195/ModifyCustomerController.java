package mkwuntr.c195;

import dataaccessobjects.CustomerDAO;
import dataaccessobjects.DivisionDAO;
import dataaccessobjects.UserDAO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Controller for the ModifyCustomer view.
 * Provides functionalities to modify customer details.
 */
public class ModifyCustomerController {

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField postalTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private MenuButton countryIdMenuButton;

    @FXML
    private MenuButton stateMenuButton;

    @FXML
    private TextField countryNameField;

    @FXML
    private TextField divisionIdField;

    @FXML
    private DivisionDAO divisionDAO = new DivisionDAO();

    @FXML
    private CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    private Customer customer;

    /**
     * Initializes the controller after its root element has been processed.
     */
    @FXML
    public void initialize() throws SQLException {
        try {
            idTextField.setText(String.valueOf(CustomerDAO.getNextCustomerId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        MenuItem usMenuItem = new MenuItem("1");
        MenuItem ukMenuItem = new MenuItem("2");
        MenuItem canMenuItem = new MenuItem("3");

        // Add event listeners to the menu items
        usMenuItem.setOnAction(event -> {
            countryNameField.setText("USA");
            updateStateMenuButton(1);
            countryIdMenuButton.setText(usMenuItem.getText());
            stateMenuButton.setText("");
            divisionIdField.clear();
        });
        ukMenuItem.setOnAction(event -> {
            countryNameField.setText("UK");
            updateStateMenuButton(2);
            countryIdMenuButton.setText(ukMenuItem.getText());
            stateMenuButton.setText("");
            divisionIdField.clear();
        });
        canMenuItem.setOnAction(event -> {
            countryNameField.setText("CAN");
            updateStateMenuButton(3);
            countryIdMenuButton.setText(canMenuItem.getText());
            stateMenuButton.setText("");
            divisionIdField.clear();
        });

        countryIdMenuButton.getItems().addAll(usMenuItem, ukMenuItem, canMenuItem);
    }

    /**
     * Updates the state menu button based on the countryId.
     * @param countryId the ID of the selected country.
     */
    @FXML
    private void updateStateMenuButton(int countryId) {
        stateMenuButton.getItems().clear();
        divisionIdField.clear();

        try {
            ObservableList<Division> divisions = divisionDAO.getDivisionsByCountryId(countryId);

            for (Division division : divisions) {
                MenuItem menuItem = new MenuItem(String.valueOf(division.getName()));

                menuItem.setOnAction(event -> {
                    stateMenuButton.setText(menuItem.getText());
                    try {
                        int divisionId = divisionDAO.getDivisionIdByDivisionName(division.getName());
                        divisionIdField.setText(String.valueOf(divisionId));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                stateMenuButton.getItems().add(menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Authenticates the user.
     * @return boolean indicating if authentication was successful.
     * @throws SQLException if there's any issue accessing the database.
     */
    @FXML
    private boolean authenticate() throws SQLException {
        // Create a custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Authentication");

        // Set up the login button.
        ButtonType loginButtonType = new ButtonType("Confirm change", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> usernameField.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(usernameField.getText(), passwordField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if (result.isPresent()) {
            String username = result.get().getKey();
            String password = result.get().getValue();

            // Use your UserDAO to verify the username and password.
            UserDAO userDAO = new UserDAO();
            if (userDAO.verifyUser(username, password)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the modified customer details.
     * @throws SQLException if there's any issue accessing the database.
     * @throws IOException if there's an issue with loading FXML files.
     */
    @FXML
    public void saveCustomer() throws SQLException, IOException {
        // Validate fields before proceeding
        if (!validateFields()) {
            return;
        }

        // First, authenticate the user
        if (!authenticate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Authentication Error");
            alert.setContentText("Unable to authenticate user.");
            alert.showAndWait();
            return;
        }

        // If authenticated, update the customer object
        int id = Integer.parseInt(idTextField.getText());
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalTextField.getText();
        String phone = phoneTextField.getText();
        int divisionId = Integer.parseInt(divisionIdField.getText());
        Customer updatedCustomer = new Customer(id, name, address, postalCode, phone, divisionId);

        // Update the customer in the database
        customerDAO.updateCustomer(updatedCustomer);

        Stage currentStage = (Stage) saveButton.getScene().getWindow();
        currentStage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerScreen.fxml"));
        Parent customerScreenRoot = loader.load();

        Stage customerScreenStage = new Stage();
        customerScreenStage.setTitle("Customer Screen");
        customerScreenStage.setScene(new Scene(customerScreenRoot));
        customerScreenStage.show();
    }

    /**
     * Validates input fields.
     * @return boolean indicating if fields are valid.
     */
    @FXML
    private boolean validateFields() {
        // Check if any field is empty
        if (idTextField.getText().trim().isEmpty() ||
                nameTextField.getText().trim().isEmpty() ||
                addressTextField.getText().trim().isEmpty() ||
                postalTextField.getText().trim().isEmpty() ||
                phoneTextField.getText().trim().isEmpty() ||
                divisionIdField.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Incomplete Data");
            alert.setContentText("Please ensure all fields are filled out.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Sets the customer for the view.
     * @param customer Customer object to set.
     * @throws SQLException if there's any issue accessing the database.
     */
    @FXML
    public void setCustomer(Customer customer) throws SQLException {
        this.customer = customer;

        // Set customer details
        idTextField.setText(Integer.toString(customer.getId()));
        nameTextField.setText(customer.getName());
        addressTextField.setText(customer.getAddress());
        postalTextField.setText(customer.getPostalCode());
        phoneTextField.setText(customer.getPhoneNumber());
        divisionIdField.setText(String.valueOf(customer.getDivisionId()));
        Division division = divisionDAO.getDivisionById(customer.getDivisionId());

        if (division != null) {
            stateMenuButton.setText(division.getName());
            switch (division.getCountryId()) {
                case 1:
                    countryNameField.setText("USA");
                    countryIdMenuButton.setText("1");
                    break;
                case 2:
                    countryNameField.setText("UK");
                    countryIdMenuButton.setText("2");
                    break;
                case 3:
                    countryNameField.setText("CAN");
                    countryIdMenuButton.setText("3");
                    break;
            }

        }
    }

    /**
     * Handles save button click event.
     * @throws SQLException if there's any issue accessing the database.
     * @throws IOException if there's an issue with loading FXML files.
     */
    @FXML
    private void handleSaveClick() throws SQLException, IOException {
        saveCustomer();
    }

    /**
     * Handles cancel button click event.
     * @throws IOException if there's an issue with loading FXML files.
     */
    @FXML
    private void handleCancelClick() throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerScreen.fxml"));
        Parent root = loader.load();

        Stage stage2 = new Stage();
        stage2.setTitle("Customer Screen");
        stage2.setScene(new Scene(root));
        stage2.show();
    }
}
