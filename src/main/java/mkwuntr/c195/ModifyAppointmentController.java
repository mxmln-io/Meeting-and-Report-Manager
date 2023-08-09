package mkwuntr.c195;

import dataaccessobjects.AppointmentDAO;
import dataaccessobjects.CustomerDAO;
import dataaccessobjects.UserDAO;
import dataaccessobjects.ContactDAO;
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
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Controller for the ModifyAppointment screen.
 * Allows the user to modify details of an existing appointment.
 */
public class ModifyAppointmentController {

    @FXML
    private TextField idTextField, titleTextField, descriptionTextField, locationTextField, typeTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private MenuButton contactMenuButton, customerIdMenuButton, userIdMenuButton,
            startTimeMenuButton, endTimeMenuButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField contactField;

    @FXML
    private Appointment appointment;

    @FXML
    private CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    private ObservableList<Customer> customerObservableList;

    /**
     * Initializes the controller.
     * <p>
     * Sets up the Contact, User, and Customer dropdown menus, and the start and end time selectors.
     * </p>
     */
    @FXML
    public void initialize() throws SQLException {

        //Set items in Contact menu
        ObservableList<Contact> contacts = ContactDAO.getAllContactsObservable();
        for (Contact contact : contacts) {
            MenuItem menuItem = new MenuItem(String.valueOf(contact.getId()));
            menuItem.setOnAction(event -> {
                contactMenuButton.setText(String.valueOf(contact.getId()));
                contactField.setText(contact.getName());  // Assume contact has getName() method
            });
            contactMenuButton.getItems().add(menuItem);
        }


        //Set items in User menu
        ObservableList<User> users = UserDAO.getAllUsersObservable();
        for (User user : users) {
            MenuItem menuItem = new MenuItem(String.valueOf(user.getId()));
            menuItem.setOnAction(event -> userIdMenuButton.setText(String.valueOf(user.getId())));
            userIdMenuButton.getItems().add(menuItem);
        }

        //Set items in Customer menu
        CustomerDAO customerDAO = new CustomerDAO();
        customerObservableList = CustomerDAO.getAllCustomersObservable();
        for (Customer customer : customerObservableList) {
            MenuItem menuItem = new MenuItem(String.valueOf(customer.getId()));
            menuItem.setOnAction(event -> customerIdMenuButton.setText(String.valueOf(customer.getId())));
            customerIdMenuButton.getItems().add(menuItem);
        }

        //Set Times
        for (MenuItem item : startTimeMenuButton.getItems()) {
            item.setOnAction(event -> startTimeMenuButton.setText(item.getText()));
        }

        for (MenuItem item : endTimeMenuButton.getItems()) {
            item.setOnAction(event -> endTimeMenuButton.setText(item.getText()));
        }
    }

    /**
     * Set the appointment details to the UI elements.
     *
     * @param appointment The appointment to set.
     * @throws SQLException If there is a database access error.
     */
    @FXML
    public void setAppointment(Appointment appointment) throws SQLException {
        this.appointment = appointment;

        //Set Appointment Details
        idTextField.setText(Integer.toString(appointment.getId()));
        titleTextField.setText(appointment.getTitle());
        descriptionTextField.setText(appointment.getDescription());
        locationTextField.setText(appointment.getLocation());
        typeTextField.setText(appointment.getType());
        datePicker.setValue(appointment.getStartDateTime().toLocalDate());

        //Set Time
        LocalTime startTime = appointment.getStartDateTime().toLocalTime();
        LocalTime endTime = appointment.getEndDateTime().toLocalTime();
        startTimeMenuButton.setText(convertTimeToString(startTime));
        endTimeMenuButton.setText(convertTimeToString(endTime));

        Integer contactId = appointment.getContactId();
        Integer customerId = appointment.getCustomerId();
        Integer userId = appointment.getUserId();
        contactMenuButton.setText(convertIntegerToString(contactId));
        customerIdMenuButton.setText(convertIntegerToString(customerId));
        userIdMenuButton.setText(convertIntegerToString(userId));
        Contact contact = ContactDAO.getContactById(contactId);
        if (contact != null) {
            contactField.setText(contact.getName());
        }

    }

    /**
     * Handles the save button click.
     * <p>
     * Validates the input, checks for overlapping appointments, and business hours.
     * </p>
     *
     * @throws IOException If there is an I/O error.
     * @throws SQLException If there is a database access error.
     */
    @FXML
    private void handleSaveClick() throws IOException, SQLException, IOException {

        if (idTextField.getText().isEmpty() || titleTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty()
                || locationTextField.getText().isEmpty() || typeTextField.getText().isEmpty() || datePicker.getValue() == null
                || startTimeMenuButton.getText().isEmpty() || endTimeMenuButton.getText().isEmpty()
                || contactMenuButton.getText().isEmpty() || customerIdMenuButton.getText().isEmpty() || userIdMenuButton.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Error");
            alert.setContentText("Please fill all the fields before saving.");
            alert.showAndWait();
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(startTimeMenuButton.getText()));
        LocalDateTime endDateTime = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(endTimeMenuButton.getText()));

        // Check if the appointment is within business hours
        if (!isWithinBusinessHours(startDateTime, endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Error");
            alert.setContentText("Appointments can only be scheduled between 8:00 AM and 10:00 PM ET.");
            alert.showAndWait();
            return;
        }

        // Check if there are overlapping appointments
        Integer customerId = Integer.parseInt(customerIdMenuButton.getText());
        if (isOverlapping(customerId, startDateTime, endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Error");
            alert.setContentText("This customer already has an appointment at the selected time.");
            alert.showAndWait();
            return;
        }

        // Check if the start time is before the end time
        if (startDateTime.isAfter(endDateTime) || startDateTime.isEqual(endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Error");
            alert.setContentText("The start time cannot be the same as or after the end time.");
            alert.showAndWait();
            return;
        }

        // If everything is fine, save the appointment
        saveAppointment();
    }

    /**
     * Handles the cancel button click.
     * Returns the user to the Appointment Screen.
     *
     * @throws IOException If there is an I/O error.
     */
    @FXML
    private void handleCancelClick() throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

        // Load the AppointmentScreen.fxml again
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentScreen.fxml"));
        Parent root = loader.load();

        // Create a new stage for the AppointmentScreen.fxml
        Stage stage2 = new Stage();
        stage2.setTitle("Appointment Screen");
        stage2.setScene(new Scene(root));
        stage2.show();
    }

    /**
     * Checks if the appointment is within the business hours.
     *
     * @param startDateTime Start date and time of the appointment.
     * @param endDateTime End date and time of the appointment.
     * @return true if the appointment is within business hours, false otherwise.
     */
    @FXML
    private boolean isWithinBusinessHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        ZoneId easternTimeZone = ZoneId.of("America/New_York");
        ZoneId localTimeZone = ZoneId.systemDefault();

        ZonedDateTime startDateTimeLocal = startDateTime.atZone(localTimeZone);
        ZonedDateTime endDateTimeLocal = endDateTime.atZone(localTimeZone);

        // Convert business hours to local time
        ZonedDateTime businessStartTimeET = ZonedDateTime.of(startDateTime.toLocalDate(), LocalTime.of(8, 0), easternTimeZone);
        ZonedDateTime businessEndTimeET = ZonedDateTime.of(endDateTime.toLocalDate(), LocalTime.of(22, 0), easternTimeZone);
        LocalTime businessStartTimeLocal = businessStartTimeET.withZoneSameInstant(localTimeZone).toLocalTime();
        LocalTime businessEndTimeLocal = businessEndTimeET.withZoneSameInstant(localTimeZone).toLocalTime();

        return !startDateTimeLocal.toLocalTime().isBefore(businessStartTimeLocal) && !endDateTimeLocal.toLocalTime().isAfter(businessEndTimeLocal);
    }

    /**
     * Checks if there is an overlapping appointment.
     *
     * @param customerId The customer ID for checking overlapping.
     * @param newStartTime New appointment start time.
     * @param newEndTime New appointment end time.
     * @return true if there's an overlapping appointment, false otherwise.
     * @throws SQLException If there is a database access error.
     */
    @FXML
    private boolean isOverlapping(Integer customerId, LocalDateTime newStartTime, LocalDateTime newEndTime) throws SQLException {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        ObservableList<Appointment> existingAppointments = appointmentDAO.getAllAppointmentsByCustomer(customerId);

        for (Appointment appointment : existingAppointments) {
            if (newStartTime.isBefore(appointment.getEndDateTime()) && newEndTime.isAfter(appointment.getStartDateTime())) {
                // This means the new appointment is overlapping with an existing one.
                return true;
            }
        }

        return false;
    }

    /**
     * Authenticate the user before saving the changes.
     *
     * @return true if the user is authenticated, false otherwise.
     * @throws SQLException If there is a database access error.
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
     * Convert a LocalTime to a formatted String.
     *
     * @param time The time to convert.
     * @return Formatted string representation of the time.
     */
    @FXML
    private String convertTimeToString(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    /**
     * Convert an Integer to a String.
     *
     * @param integer The integer to convert.
     * @return String representation of the integer.
     */
    @FXML
    private String convertIntegerToString(Integer integer) {
        return String.valueOf(integer);
    }

    /**
     * Save the appointment to the database after updating.
     *
     * @throws SQLException If there is a database access error.
     * @throws IOException If there is an I/O error.
     */
    @FXML
    private void saveAppointment() throws SQLException, IOException {
        // First, authenticate the user
        if (!authenticate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Authentication Error");
            alert.setContentText("Username or password is incorrect.");
            alert.showAndWait();
            return;
        }

        // If authenticated, update the appointment object
        appointment.setTitle(titleTextField.getText());
        appointment.setDescription(descriptionTextField.getText());
        appointment.setLocation(locationTextField.getText());
        appointment.setType(typeTextField.getText());
        appointment.setStartDateTime(LocalDateTime.of(datePicker.getValue(), LocalTime.parse(startTimeMenuButton.getText())));
        appointment.setEndDateTime(LocalDateTime.of(datePicker.getValue(), LocalTime.parse(endTimeMenuButton.getText())));
        appointment.setContactId(Integer.parseInt(contactMenuButton.getText()));
        appointment.setCustomerId(Integer.parseInt(customerIdMenuButton.getText()));
        appointment.setUserId(Integer.parseInt(userIdMenuButton.getText()));

        // Then, update the appointment in the database
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        appointmentDAO.updateAppointment(appointment);

        // Close the window after saving
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

        // Load the AppointmentScreen.fxml again
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentScreen.fxml"));
        Parent root = loader.load();

        // Create a new stage for the AppointmentScreen.fxml
        Stage newStage = new Stage();
        newStage.setTitle("Appointment Screen");
        newStage.setScene(new Scene(root));
        newStage.show();
    }
}
