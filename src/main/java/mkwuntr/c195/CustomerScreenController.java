package mkwuntr.c195;

import dataaccessobjects.AppointmentDAO;
import dataaccessobjects.CustomerDAO;
import dataaccessobjects.DivisionDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Controller for the CustomerScreen.
 * Handles actions associated with the CustomerScreen, including displaying customers,
 * adding, modifying, and deleting customers.
 */
public class CustomerScreenController {

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, String> idColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, String> stateColumn;

    @FXML
    private TableColumn<Customer, String> countryColumn;

    @FXML
    private TableColumn<Customer, String> postalColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    @FXML
    private TableColumn<Customer, Integer> divisionIdColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button modifyButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button exitButton;

    @FXML
    private CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    private ObservableList<Customer> customerList;

    @FXML
    private DivisionDAO divisionDAO = new DivisionDAO();

    /**
     * Initializes the CustomerScreenController.
     * This method sets up the table columns and initial set of customers.
     * It uses lambda expressions to populate the state and country columns based on
     * the division ID from the Customer object.
     *
     * @throws SQLException if database operations fail.
     */
    @FXML
    public void initialize() throws SQLException {
        //Populate columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        divisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        //State and Country populated based on the First Level Division ID using lambda expressions
        stateColumn.setCellValueFactory(cellData -> {
            int divisionId = cellData.getValue().getDivisionId();
            try {
                String divisionName = divisionDAO.getDivisionById(divisionId).getName();
                return new SimpleStringProperty(divisionName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });

        countryColumn.setCellValueFactory(cellData -> {
            int divisionId = cellData.getValue().getDivisionId();
            try {
                String divisionName = divisionDAO.getCountryNameByDivisionId(divisionId);
                return new SimpleStringProperty(divisionName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });

        CustomerDAO customerDAO = new CustomerDAO();
        customerList = customerDAO.getAllCustomersObservable();
        customerTableView.setItems(customerList);
    }

    /**
     * Handles the "Add" button click event.
     * This method opens the "Add Customer" screen.
     */
    @FXML
    private void handleAddClick(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addCustomer.fxml"));
            Parent addCustomerRoot = loader.load();

            Stage addCustomerStage = new Stage();
            addCustomerStage.setTitle("Add Customer");
            addCustomerStage.setScene(new Scene(addCustomerRoot));
            addCustomerStage.show();

            Stage currentStage = (Stage) addButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Modify" button click event.
     * This method loads the selected customer into the "Modify Customer" screen.
     */
    @FXML
    private void handleModifyClick(){
        try {
            // Get the selected customer
            Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

            // If no customer is selected, show an error message and return
            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Selection");
                alert.setHeaderText(null);
                alert.setContentText("Please select an customer to modify.");
                alert.showAndWait();
                return;
            }

            //Load Modify Customer screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyCustomer.fxml"));
            Parent modifyCustomerRoot = loader.load();

            ModifyCustomerController controller = loader.getController();
            controller.setCustomer(selectedCustomer);

            Stage modifyCustomerStage = new Stage();
            modifyCustomerStage.setTitle("Modify Customer");
            modifyCustomerStage.setScene(new Scene(modifyCustomerRoot));
            modifyCustomerStage.show();

            Stage currentStage = (Stage) customerTableView.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the "Delete" button click event.
     * This method deletes the selected customer, and if the customer has any associated
     * appointments, those are deleted as well.
     */
    @FXML
    private void handleDeleteClick(){
        // Get the selected customer
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        // If no customer is selected, display an alert
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
            return;
        }

        try {
            // Check if the customer has appointments
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            ObservableList<Appointment> customerAppointments = appointmentDAO.getAllAppointmentsByCustomer(selectedCustomer.getId());

            // Create an Alert for confirmation
            Alert alert;
            if (customerAppointments.isEmpty()) {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Customer");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this customer?");
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Customer and Appointments");
                alert.setHeaderText(null);
                alert.setContentText("This customer has appointments associated with them. Deleting the customer will also delete these appointments. Do you want to proceed?");
            }

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // If there are appointments, delete them first
                for (Appointment appointment : customerAppointments) {
                    appointmentDAO.deleteAppointment(appointment.getId());
                }
                // Delete the customer
                customerDAO.deleteCustomer(selectedCustomer.getId());
                // If successful, remove the customer from the ObservableList
                customerList.remove(selectedCustomer);

                // Alert to notify the user of successful deletion
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Customer Deleted");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Customer '" + selectedCustomer.getName() + "' with ID: " + selectedCustomer.getId() + " has been deleted.");
                successAlert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // If there's a problem, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("There was a problem deleting the customer.");
            errorAlert.showAndWait();
        }
    }

    /**
     * Handles the "Exit" button click event.
     * This method closes the current screen.
     */
    @FXML
    private void handleExitClick(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
