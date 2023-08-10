package mkwuntr.c195;

import dataaccessobjects.ContactDAO;
import dataaccessobjects.ReportDAO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Controller for managing and displaying various reports in the application.
 */
public class ReportsController {

    @FXML
    private Button exitButton;

    @FXML
    private MenuButton typeMenuButton;

    @FXML
    private TableView<Pair<String, Integer>> appointmentTypeMonthTable;

    @FXML
    private TableColumn<Pair<String, Integer>, String> monthColumn;

    @FXML
    private TableColumn<Pair<String, Integer>, Integer> totalByMonthColumn;

    @FXML
    private TableView<Pair<String, Integer>> appointmentCustomerTable;

    @FXML
    private TableColumn<Pair<String, Integer>, String> customerColumn;

    @FXML
    private TableColumn<Pair<String, Integer>, Integer> totalByCustomerColumn;

    @FXML
    private TableView<Appointment> contactAppointmentTable;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startTimeColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endTimeColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;

    @FXML
    private MenuButton contactMenuButton;

    @FXML
    private ContactDAO contactDAO = new ContactDAO();

    @FXML
    private ReportDAO reportDAO = new ReportDAO();

    /**
     * Initializes the reports controller by populating tables and other UI elements.
     * @throws SQLException if there is an error fetching data from the database.
     */
    @FXML
    public void initialize() throws SQLException {
        populateTypeMenuButton();
        populateAppointmentCustomerTable();
        populateContactMenuButton();
    }

    /**
     * Populates the type menu button with appointment types from the database.
     * @throws SQLException if there is an error fetching data from the database.
     */
    @FXML
    private void populateTypeMenuButton() throws SQLException {
        ObservableList<String> types = reportDAO.getAllTypes();

        for (String type : types) {
            MenuItem menuItem = new MenuItem(type);
            menuItem.setOnAction(event -> {
                typeMenuButton.setText(type);
                handleTypeMenuItemAction(event);
            });
            typeMenuButton.getItems().add(menuItem);
        }
    }

    /**
     * Handles the action when a type menu item is selected. It updates the table view
     * to display appointment counts by month for the selected type.
     * @param event the action event triggered by the menu item selection.
     */
    @FXML
    private void handleTypeMenuItemAction(ActionEvent event) {
        MenuItem selectedTypeItem = (MenuItem) event.getSource();
        String selectedType = selectedTypeItem.getText();
        try {
            ObservableList<Pair<String, Integer>> counts = reportDAO.getAppointmentMonthByType(selectedType);
            System.out.println(counts);
            populateAppointmentTypeMonthTable(counts);
        } catch (SQLException ex) {
            // Handle exception
        }
    }

    /**
     * Populates the appointment by type and month table view. It sets the data and configures
     * the cell value factories for each column.
     * @param counts a list of pairs where each pair represents a month and its corresponding appointment count.
     */
    @FXML
    private void populateAppointmentTypeMonthTable(ObservableList<Pair<String, Integer>> counts) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ObservableList<Pair<String, Integer>> data = FXCollections.observableArrayList();

        int totalCount = 0;

        for (String month : months) {
            int monthCount = 0;
            for (Pair<String, Integer> count : counts) {
                if (count.getKey().equals(month)) {
                    monthCount = count.getValue();
                    break;
                }
            }
            totalCount += monthCount;
            data.add(new Pair<>(month, monthCount));
        }
        data.add(new Pair<>("TOTAL", totalCount));

        appointmentTypeMonthTable.setItems(data);

        monthColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        totalByMonthColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());
    }

    /**
     * Populates the appointment customer table with data.
     * @throws SQLException if there is an error fetching data from the database.
     */
    @FXML
    private void populateAppointmentCustomerTable() throws SQLException {
        ObservableList<Pair<String, Integer>> customersData = reportDAO.getAppointmentsCountByCustomer();

        customerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        totalByCustomerColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        appointmentCustomerTable.setItems(customersData);
    }

    /**
     * Populates the contact menu button with available contacts.
     * @throws SQLException if there is an error fetching data from the database.
     */
    @FXML
    private void populateContactMenuButton() throws SQLException {
        ObservableList<Contact> contacts = contactDAO.getAllContactsObservable();

        for (Contact contact : contacts) {
            MenuItem menuItem = new MenuItem(contact.getName());
            menuItem.setOnAction(event -> {
                // Set the MenuButton's text to the selected contact's name
                contactMenuButton.setText(contact.getName());

                try {
                    updateContactAppointmentsTable(contact.getId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            contactMenuButton.getItems().add(menuItem);
        }
    }

    /**
     * Updates the contact appointments table with appointments associated with a specific contact.
     * @param contactId the ID of the contact.
     * @throws SQLException if there is an error fetching data from the database.
     */
    @FXML
    private void updateContactAppointmentsTable(int contactId) throws SQLException {
        ObservableList<Appointment> appointmentsForContact = reportDAO.getAppointmentsByContact(contactId);

        // Set cell value factories for each column
        appointmentIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        appointmentTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        startTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<LocalDateTime>(cellData.getValue().getStartDateTime()));
        endTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<LocalDateTime>(cellData.getValue().getEndDateTime()));
        customerIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCustomerId()).asObject());
        userIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserId()).asObject());

        // Now set the items for the table
        contactAppointmentTable.setItems(appointmentsForContact);
    }

    /**
     * Handles the action of the exit button click, closing the current stage.
     */
    @FXML
    private void handleExitClick() {
        try {
            Stage currentStage = (Stage) exitButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScreen.fxml"));
            Parent mainScreenRoot = loader.load();

            Stage mainScreenStage = new Stage();
            mainScreenStage.setTitle("Main Screen");
            mainScreenStage.setScene(new Scene(mainScreenRoot));
            mainScreenStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


