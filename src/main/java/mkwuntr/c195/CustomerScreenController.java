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
import model.Division;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

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

    @FXML
    private ObservableList<Division> divisionList;

    @FXML
    public void initialize() throws SQLException {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        divisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        //State and Country need to be populated based on the First Level Division ID
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


    @FXML
    private void handleAddClick(){
        try {
            // Load the FXML file for the Add Customer page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addCustomer.fxml"));
            Parent addCustomerRoot = loader.load();

            // Create a new stage for the Add Customer page
            Stage addCustomerStage = new Stage();
            addCustomerStage.setTitle("Add Customer");
            addCustomerStage.setScene(new Scene(addCustomerRoot));
            addCustomerStage.show();

            // Get the current stage and close it
            Stage currentStage = (Stage) addButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

            // Load the FXML file for the Modify Customer page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyCustomer.fxml"));
            Parent modifyCustomerRoot = loader.load();

            // Get the controller and pass the selected appointment
            ModifyCustomerController controller = loader.getController();
            controller.setCustomer(selectedCustomer);

            // Create a new stage for the Modify Appointments page
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

    @FXML
    private void handleDeleteClick(){
        // Get the selected customer from the TableView
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


    @FXML
    private void handleExitClick(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
