package dataaccessobjects;

import javafx.util.Pair;
import model.Appointment;
import helper.JDBC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Data access object (DAO) for handling appointment database operations.
 */
public class AppointmentDAO {

    /**
     * Retrieves an observable list of all appointments from the database.
     *
     * @return ObservableList containing all appointments
     * @throws SQLException if database query fails
     */
    public ObservableList<Appointment> getAllAppointmentsObservable() throws SQLException {
        String query = "SELECT * FROM appointments";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        return createAppointmentsFromResultSet(resultSet);
    }

    /**
     * Retrieves an observable list of appointments associated with a particular customer ID.
     *
     * @param customerIdSearch the ID of the customer for whom to retrieve appointments
     * @return ObservableList of appointments for the specified customer
     * @throws SQLException if database query fails
     */
    public ObservableList<Appointment> getAllAppointmentsByCustomer(int customerIdSearch) throws SQLException {
        String query =  "SELECT * FROM appointments WHERE customer_id = ?";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        preparedStatement.setInt(1, customerIdSearch);
        ResultSet resultSet = preparedStatement.executeQuery();
        return createAppointmentsFromResultSet(resultSet);
    }

    /**
     * Deletes an appointment with the specified ID from the database.
     *
     * @param id the ID of the appointment to delete
     * @throws SQLException if database query fails
     */
    public void deleteAppointment(int id) throws SQLException {
        String query = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    /**
     * Updates an existing appointment in the database.
     *
     * @param appointment the updated appointment
     * @throws SQLException if database query fails
     */
    public void updateAppointment(Appointment appointment) throws SQLException {
        String query = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, " +
                "Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);

        preparedStatement.setString(1, appointment.getTitle());
        preparedStatement.setString(2, appointment.getDescription());
        preparedStatement.setString(3, appointment.getLocation());
        preparedStatement.setString(4, appointment.getType());
        preparedStatement.setTimestamp(5, Timestamp.valueOf(appointment.getStartDateTime()));
        preparedStatement.setTimestamp(6, Timestamp.valueOf(appointment.getEndDateTime()));

        preparedStatement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setString(8, "user");

        preparedStatement.setInt(9, appointment.getCustomerId());
        preparedStatement.setInt(10, appointment.getUserId());
        preparedStatement.setInt(11, appointment.getContactId());
        preparedStatement.setInt(12, appointment.getId());

        preparedStatement.executeUpdate();
    }

    /**
     * Adds a new appointment to the database.
     *
     * @param appointment the appointment to add
     * @throws SQLException if database query fails
     */
    public void addAppointment(Appointment appointment) throws SQLException {
        String query = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);

        preparedStatement.setInt(1, appointment.getId());
        preparedStatement.setString(2, appointment.getTitle());
        preparedStatement.setString(3, appointment.getDescription());
        preparedStatement.setString(4, appointment.getLocation());
        preparedStatement.setString(5, appointment.getType());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(appointment.getStartDateTime()));
        preparedStatement.setTimestamp(7, Timestamp.valueOf(appointment.getEndDateTime()));

        preparedStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setString(9, "user");
        preparedStatement.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setString(11, "user");


        preparedStatement.setInt(12, appointment.getCustomerId());
        preparedStatement.setInt(13, appointment.getUserId());
        preparedStatement.setInt(14, appointment.getContactId());

        preparedStatement.executeUpdate();
    }


    /**
     * Retrieves an observable list of appointments for the current week.
     *
     * @return ObservableList of appointments for the current week
     * @throws SQLException if database query fails
     */
    public ObservableList<Appointment> getAppointmentsForWeekObservable() throws SQLException {
        String query = "SELECT * FROM appointments WHERE YEARWEEK(DATE(Start), 1) = YEARWEEK(CURDATE(), 1)";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        return createAppointmentsFromResultSet(resultSet);
    }

    /**
     * Retrieves an observable list of appointments for the current month.
     *
     * @return ObservableList of appointments for the current month
     * @throws SQLException if database query fails
     */
    public ObservableList<Appointment> getAppointmentsForMonthObservable() throws SQLException {
        String query = "SELECT * FROM appointments WHERE MONTH(Start) = MONTH(CURDATE()) AND YEAR(Start) = YEAR(CURDATE())";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        return createAppointmentsFromResultSet(resultSet);
    }

    /**
     * Constructs and returns an observable list of appointments based on the provided result set.
     *
     * @param resultSet the result set containing appointment data
     * @return ObservableList of appointments constructed from the result set
     * @throws SQLException if there is an issue reading from the result set
     */
    private ObservableList<Appointment> createAppointmentsFromResultSet(ResultSet resultSet) throws SQLException {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

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

    /**
     * Retrieves the maximum appointment ID from the database.
     *
     * @return the maximum appointment ID
     * @throws SQLException if database query fails
     */
    public int getNextAppointmentId() throws SQLException {
        String query = "SELECT MAX(Appointment_ID) AS max_id FROM appointments";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("max_id") + 1;
        } else {
            throw new SQLException("Unable to fetch maximum Appointment ID from database");
        }
    }
}
