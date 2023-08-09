package mkwuntr.c195;

import dataaccessobjects.AppointmentDAO;
import dataaccessobjects.ContactDAO;
import dataaccessobjects.CustomerDAO;
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

public class AddAppointmentController {
    @FXML
    private TextField idTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private MenuButton contactMenuButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private MenuButton startTimeMenuButton;
    @FXML
    private MenuButton userIdMenuButton;
    @FXML
    private MenuButton customerIdMenuButton;
    @FXML
    private MenuButton endTimeMenuButton;
    @FXML
    private TextField contactField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    @FXML
    private CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    private ObservableList<Customer> customerObservableList;

    @FXML
    public void initialize() throws SQLException {
        try {
            idTextField.setText(String.valueOf(appointmentDAO.getNextAppointmentId()));

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveClick() throws IOException, SQLException {
        // Check for empty fields
        if (idTextField.getText().isEmpty() || titleTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty()
                || locationTextField.getText().isEmpty() || typeTextField.getText().isEmpty() || contactMenuButton.getText().isEmpty()
                || datePicker.getValue() == null || startTimeMenuButton.getText().isEmpty() || userIdMenuButton.getText().isEmpty()
                || customerIdMenuButton.getText().isEmpty() || endTimeMenuButton.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Field Error");
            alert.setContentText("Please make sure all fields are filled out.");
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

    public boolean isWithinBusinessHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
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



    public boolean isOverlapping(Integer customerId, LocalDateTime newStartTime, LocalDateTime newEndTime) throws SQLException {
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

    public boolean authenticate() throws SQLException {
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


    public String convertTimeToString(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public String convertIntegerToString(Integer integer) {
        return String.valueOf(integer);
    }

    public void saveAppointment() throws SQLException, IOException {
        // First, authenticate the user
        if (!authenticate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Authentication Error");
            alert.setContentText("Unable to authenticate user.");
            alert.showAndWait();
            return;
        }

        // If authenticated, create a new Appointment object
        int id = Integer.parseInt(idTextField.getText());
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        LocalDateTime startDateTime = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(startTimeMenuButton.getText(), DateTimeFormatter.ofPattern("HH:mm")));
        LocalDateTime endDateTime = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(endTimeMenuButton.getText(), DateTimeFormatter.ofPattern("HH:mm")));
        int customerId = Integer.parseInt(customerIdMenuButton.getText());
        int userId = Integer.parseInt(userIdMenuButton.getText());
        int contactId = Integer.parseInt(contactMenuButton.getText());

        Appointment newAppointment = new Appointment(id, title, description, location, type, startDateTime, endDateTime, customerId, userId, contactId);

        // Then, add the new appointment to the database
        appointmentDAO.addAppointment(newAppointment);

        // Close the window after saving
        Stage currentStage = (Stage) saveButton.getScene().getWindow();
        currentStage.close();

        // Load the AppointmentScreen.fxml again
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appointmentScreen.fxml"));
        Parent AppointmentScreenRoot = loader.load();

        // Create a new stage for the AppointmentScreen.fxml
        Stage AppointmentScreenStage = new Stage();
        AppointmentScreenStage.setTitle("Appointment Screen");
        AppointmentScreenStage.setScene(new Scene(AppointmentScreenRoot));
        AppointmentScreenStage.show();

    }


}
