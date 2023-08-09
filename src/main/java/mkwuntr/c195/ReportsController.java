package mkwuntr.c195;

import dataaccessobjects.AppointmentDAO;
import dataaccessobjects.ContactDAO;
import dataaccessobjects.ReportDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Appointment;
import model.Contact;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ReportsController {

    @FXML
    private Button exitButton;

    @FXML
    private TableView<Pair<String, Integer>> appointmentTypeTable;

    @FXML
    private TableColumn<Pair<String, Integer>, String> typeColumn;

    @FXML
    private TableColumn<Pair<String, Integer>, Integer> totalByTypeColumn;

    @FXML
    private TableView<Pair<String, Integer>> appointmentMonthTable;

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

    @FXML
    public void initialize() throws SQLException {
        populateAppointmentTypeTable();
        populateAppointmentMonthTable();
        populateAppointmentCustomerTable();
        populateContactMenuButton();
    }

    private void populateAppointmentTypeTable() throws SQLException {
        ObservableList<Pair<String, Integer>> typesData = reportDAO.getAppointmentsCountByType();

        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        totalByTypeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        appointmentTypeTable.setItems(typesData);
    }


    private void populateAppointmentMonthTable() throws SQLException {
        ObservableList<Pair<String, Integer>> monthsData = reportDAO.getAppointmentsCountByMonth();

        monthColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        totalByMonthColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        appointmentMonthTable.setItems(monthsData);
    }


    private void populateAppointmentCustomerTable() throws SQLException {
        ObservableList<Pair<String, Integer>> customersData = reportDAO.getAppointmentsCountByCustomer();

        customerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        totalByCustomerColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        appointmentCustomerTable.setItems(customersData);
    }


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


    @FXML
    private void handleExitClick(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
