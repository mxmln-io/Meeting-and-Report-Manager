package mkwuntr.c195;

import dataaccessobjects.AppointmentDAO;
import dataaccessobjects.ContactDAO;
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

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Controller for the Appointment Screen which handles the CRUD operations for appointments.
 */
public class AppointmentScreenController {

    @FXML
    private Button addButton;

    @FXML
    private Button modifyButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button exitButton;

    @FXML
    private RadioButton weekRadioButton;

    @FXML
    private RadioButton monthRadioButton;

    @FXML
    private RadioButton allDatesRadioButton;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, Integer> idColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, String> locationColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startTimeColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endTimeColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;

    @FXML
    private TableColumn<Appointment, String> contactNameColumn;

    @FXML
    private ContactDAO contactDAO = new ContactDAO();

    @FXML
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    @FXML
    private ObservableList<Appointment> appointmentList;

    @FXML
    private ToggleGroup radioButtonGroup = new ToggleGroup();

    /**
     * Initializes the AppointmentScreenController.
     * This method sets up the table columns, radio buttons, and initial set of appointments.
     * It uses a lambda expression to efficiently populate the contact name column
     * based on the contact ID from the Appointment object.
     * A lambda expression has also been added to improve the code structure for
     * the radio buttons when selecting a date range.
     * @throws SQLException if database operations fail.
     */
    @FXML
    public void initialize() throws SQLException {
        //Initialize Appointment columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        //Lambda expression
        contactNameColumn.setCellValueFactory(cellData -> {
            int contactId = cellData.getValue().getContactId();
            try {
                String contactName = contactDAO.getContactById(contactId).getName();
                return new SimpleStringProperty(contactName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });

        AppointmentDAO appointmentDAO = new AppointmentDAO();
        appointmentList = appointmentDAO.getAllAppointmentsObservable();
        appointmentTable.setItems(appointmentList);

        // Setup radio buttons to be in a group
        allDatesRadioButton.setToggleGroup(radioButtonGroup);
        weekRadioButton.setToggleGroup(radioButtonGroup);
        monthRadioButton.setToggleGroup(radioButtonGroup);

        // Set default selection
        allDatesRadioButton.setSelected(true);

        // Add listeners to the radio buttons. Lambda function has been added
        Consumer<RadioButton> setRadioButtonAction = radioButton -> radioButton.setOnAction(event -> {
            try {
                filterAppointments();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        setRadioButtonAction.accept(allDatesRadioButton);
        setRadioButtonAction.accept(weekRadioButton);
        setRadioButtonAction.accept(monthRadioButton);


        // Load the initial set of appointments
        filterAppointments();
    }

    /**
     * Filters the appointments in the table based on the selected radio button.
     *
     * @throws SQLException if database operations fail.
     */
    private void filterAppointments() throws SQLException {
        if (allDatesRadioButton.isSelected()) {
            appointmentList = appointmentDAO.getAllAppointmentsObservable();
        } else if (weekRadioButton.isSelected()) {
            appointmentList = appointmentDAO.getAppointmentsForWeekObservable();
        } else if (monthRadioButton.isSelected()) {
            appointmentList = appointmentDAO.getAppointmentsForMonthObservable();
        }
        appointmentTable.setItems(appointmentList);
    }

    /**
     * Opens the "Add Appointment" screen.
     */
    @FXML
    private void handleAddClick(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addAppointment.fxml"));
            Parent addAppointmentRoot = loader.load();

            Stage addAppointmentStage = new Stage();
            addAppointmentStage.setTitle("Appointments");
            addAppointmentStage.setScene(new Scene(addAppointmentRoot));
            addAppointmentStage.show();

            Stage currentStage = (Stage) addButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the "Modify Appointment" screen for the selected appointment.
     * Displays an error alert if no appointment is selected.
     */
    @FXML
    private void handleModifyClick(){
        try {
            // Get the selected appointment
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

            // If no appointment is selected, show an error message and return
            if (selectedAppointment == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Selection");
                alert.setHeaderText(null);
                alert.setContentText("Please select an appointment to modify.");
                alert.showAndWait();
                return;
            }
            //Open Modify Appointments screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyAppointment.fxml"));
            Parent modifyAppointmentRoot = loader.load();

            ModifyAppointmentController controller = loader.getController();
            controller.setAppointment(selectedAppointment);

            Stage modifyAppointmentStage = new Stage();
            modifyAppointmentStage.setTitle("Modify Appointments");
            modifyAppointmentStage.setScene(new Scene(modifyAppointmentRoot));
            modifyAppointmentStage.show();

            Stage currentStage = (Stage) appointmentTable.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes the selected appointment from the table and the database.
     * Displays error or confirmation alerts as needed.
     */
    @FXML
    private void handleDeleteClick(){
        // Get the selected appointment
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        // If no appointment is selected, display an alert
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }

        // Create an Alert for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this appointment?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // If the user confirms, show another alert with the Type and ID of the appointment to be deleted
            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setTitle("Appointment Cancelled");
            alertInfo.setHeaderText(null);
            alertInfo.setContentText("You have cancelled the appointment with ID: " + selectedAppointment.getId() + " and Type: " + selectedAppointment.getType() + ".");
            alertInfo.showAndWait();

            // Delete the appointment from the database
            try {
                appointmentDAO.deleteAppointment(selectedAppointment.getId());
                appointmentList.remove(selectedAppointment);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the appointment screen.
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
