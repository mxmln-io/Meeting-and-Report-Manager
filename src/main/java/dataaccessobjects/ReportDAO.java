package dataaccessobjects;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Data access object (DAO) for handling report-related database operations.
 */
public class ReportDAO {

    /**
     * Parses the given ResultSet to create a list of Appointment objects.
     *
     * @param resultSet ResultSet object from a query
     * @return ObservableList of Appointment objects
     * @throws SQLException if database operation fails
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
     * Retrieves an observable list of appointments associated with a given contact ID.
     *
     * @param contactIdSearch ID of the contact
     * @return ObservableList containing appointments of the specified contact
     * @throws SQLException if database query fails
     */
    public ObservableList<Appointment> getAppointmentsByContact(int contactIdSearch) throws SQLException {
        String query =  "SELECT * FROM appointments WHERE Contact_ID = ?";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        preparedStatement.setInt(1, contactIdSearch);
        ResultSet resultSet = preparedStatement.executeQuery();
        return createAppointmentsFromResultSet(resultSet);
    }

    /**
     * Retrieves the count of appointments grouped by customer.
     *
     * @return ObservableList of pairs where the key is the customer's name and the value is the count
     * @throws SQLException if database query fails
     */
    public ObservableList<Pair<String, Integer>> getAppointmentsCountByCustomer() throws SQLException {
        String query =  "SELECT c.Customer_Name, COUNT(*) as Total FROM appointments a JOIN customers c ON a.Customer_ID = c.Customer_ID GROUP BY a.Customer_ID";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Pair<String, Integer>> customerCounts = FXCollections.observableArrayList();
        while(resultSet.next()){
            String customerName = resultSet.getString("Customer_Name");
            int count = resultSet.getInt("Total");
            customerCounts.add(new Pair<>(customerName, count));
        }
        return customerCounts;
    }

    /**
     * Retrieves a list of all unique appointment types.
     *
     * @return ObservableList containing all unique appointment types
     * @throws SQLException if database query fails
     */
    public ObservableList<String> getAllTypes() throws SQLException {
        String query = "SELECT DISTINCT Type FROM appointments";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<String> types = FXCollections.observableArrayList();
        while(resultSet.next()) {
            String type = resultSet.getString("Type");
            types.add(type);
        }
        return types;
    }

    /**
     * Fetches the count of appointments of a specific type for each month from the database.
     * This method ensures that if a month has no appointments of the given type, it still returns
     * that month with a count of zero.
     *
     * @param type The type of appointment to filter by.
     * @return An observable list containing pairs, where each pair represents a month and its
     * corresponding count of appointments of the given type.
     * @throws SQLException if there is an error fetching data from the database.
     */
    public ObservableList<Pair<String, Integer>> getAppointmentMonthByType(String type) throws SQLException {
        String query = "SELECT MONTHNAME(Start) as Month, COUNT(*) as Total " +
                "FROM appointments " +
                "WHERE Type = ? " +
                "GROUP BY MONTH(Start), MONTHNAME(Start);";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        preparedStatement.setString(1, type);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Initialize a map with default counts for each month
        Map<String, Integer> monthCounts = new HashMap<>();
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            monthCounts.put(month, 0);
        }

        // Update the counts with data from the database
        while (resultSet.next()) {
            String monthName = resultSet.getString("Month");
            int count = resultSet.getInt("Total");
            monthCounts.put(monthName, count);
        }

        ObservableList<Pair<String, Integer>> monthTypeCounts = FXCollections.observableArrayList();
        for (String month : months) {
            monthTypeCounts.add(new Pair<>(month, monthCounts.get(month)));
        }

        return monthTypeCounts;
    }
}
