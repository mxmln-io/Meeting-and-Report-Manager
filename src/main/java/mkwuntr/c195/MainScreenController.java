package mkwuntr.c195;

import dataaccessobjects.AppointmentDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class MainScreenController {

    @FXML
    private Button appointmentsButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button signOutButton;

    @FXML
    public void initialize() throws SQLException {
        checkForUpcomingAppointments();
    }

    private void checkForUpcomingAppointments() throws SQLException {
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        // Fetch the upcoming appointment within the next 15 minutes.
        Optional<Appointment> upcomingAppointment = appointmentDAO.getAllAppointmentsObservable().stream()
                .filter(appointment -> appointment.getStartDateTime().isAfter(LocalDateTime.now()) &&
                        appointment.getStartDateTime().isBefore(LocalDateTime.now().plusMinutes(15)))
                .findFirst();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (upcomingAppointment.isPresent()) {
            // If there's an upcoming appointment, show an alert with its details.
            alert.setTitle("Upcoming Appointment Alert");
            alert.setHeaderText("Upcoming Appointment Alert");
            alert.setContentText("You have an appointment with ID " + upcomingAppointment.get().getId()
                    + " starting at " + upcomingAppointment.get().getStartDateTime() + " within the next 15 minutes.");
        } else {
            // If there's no upcoming appointment, show a different alert.
            alert.setTitle("No Upcoming Appointments");
            alert.setHeaderText("No Upcoming Appointments");
            alert.setContentText("You have no appointments within the next 15 minutes.");
        }
        alert.showAndWait();
    }

    @FXML
    private void handleAppointmentsClick() {
        try {
            // Load the FXML file for the Appointments page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("appointmentScreen.fxml"));
            Parent appointmentsRoot = loader.load();

            // Create a new stage for the Appointments page
            Stage appointmentsStage = new Stage();
            appointmentsStage.setTitle("Appointments");
            appointmentsStage.setScene(new Scene(appointmentsRoot));
            appointmentsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCustomersClick() {
        try {
            // Load the FXML file for the Appointments page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customerScreen.fxml"));
            Parent customersRoot = loader.load();

            // Create a new stage for the Appointments page
            Stage customersStage = new Stage();
            customersStage.setTitle("Customers");
            customersStage.setScene(new Scene(customersRoot));
            customersStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReportsClick() {
        try {
            // Load the FXML file for the Appointments page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reports.fxml"));
            Parent reportsRoot = loader.load();

            // Create a new stage for the Appointments page
            Stage reportsStage = new Stage();
            reportsStage.setTitle("Reports");
            reportsStage.setScene(new Scene(reportsRoot));
            reportsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignOutClick() {
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        stage.close();
    }
}
