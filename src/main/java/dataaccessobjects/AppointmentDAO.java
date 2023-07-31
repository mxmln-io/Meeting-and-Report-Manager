package dataaccessobjects;

import model.Appointment;
import helper.JDBC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AppointmentDAO {
    // Method to retrieve an ObservableList of all appointments from the database
    public ObservableList<Appointment> getAllAppointmentsObservable() throws SQLException {
        // Initialize an ObservableList to store the results
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        // Define the SQL query to retrieve all appointments
        String query = "SELECT * FROM appointments";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);

        // Execute the query
        ResultSet resultSet = preparedStatement.executeQuery();

        // Iterate through the results and create Appointment objects for each row
        while (resultSet.next()) {
            int id = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime startDateTime = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endDateTime = resultSet.getTimestamp("End").toLocalDateTime();
            int customerId = resultSet.getInt("Customer_ID");
            int userId = resultSet.getInt("User_ID");
            int contactId = resultSet.getInt("Contact_ID");

            ZonedDateTime utcStartDateTime = resultSet.getTimestamp("Start").toInstant().atZone(ZoneId.of("UTC"));
            LocalDateTime localStartDateTime = utcStartDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

            ZonedDateTime utcEndDateTime = resultSet.getTimestamp("End").toInstant().atZone(ZoneId.of("UTC"));
            LocalDateTime localEndDateTime = utcEndDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

            Appointment appointment = new Appointment(id, title, description, location, type, localStartDateTime, localEndDateTime, customerId, userId, contactId);

            appointmentList.add(appointment);
        }
        return appointmentList;
    }
}
