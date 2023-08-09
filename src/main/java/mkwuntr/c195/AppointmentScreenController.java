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

    @FXML
    public void initialize() throws SQLException {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
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

        // Add listeners to the radio buttons
        allDatesRadioButton.setOnAction(event -> {
            try {
                filterAppointments();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        weekRadioButton.setOnAction(event -> {
            try {
                filterAppointments();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        monthRadioButton.setOnAction(event -> {
            try {
                filterAppointments();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        // Load the initial set of appointments
        filterAppointments();
    }

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

    @FXML
    private void handleAddClick(){
        try {
            // Load the FXML file for the Appointments page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addAppointment.fxml"));
            Parent addAppointmentRoot = loader.load();

            // Create a new stage for the Appointments page
            Stage addAppointmentStage = new Stage();
            addAppointmentStage.setTitle("Appointments");
            addAppointmentStage.setScene(new Scene(addAppointmentRoot));
            addAppointmentStage.show();

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

            // Load the FXML file for the Modify Appointments page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyAppointment.fxml"));
            Parent modifyAppointmentRoot = loader.load();

            // Get the controller and pass the selected appointment
            ModifyAppointmentController controller = loader.getController();
            controller.setAppointment(selectedAppointment);

            // Create a new stage for the Modify Appointments page
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

    @FXML
    private void handleDeleteClick(){
        // Get the selected appointment from the TableView
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
                // If successful, remove the appointment from the ObservableList
                appointmentList.remove(selectedAppointment);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
    private void handleExitClick(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
