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
     * Retrieves the count of appointments grouped by their type.
     *
     * @return ObservableList of pairs where the key is the type of the appointment and the value is the count
     * @throws SQLException if database query fails
     */
    public ObservableList<Pair<String, Integer>> getAppointmentsCountByType() throws SQLException {
        String query =  "SELECT Type, COUNT(*) as Total FROM appointments GROUP BY Type";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Pair<String, Integer>> typeCounts = FXCollections.observableArrayList();
        while(resultSet.next()){
            String type = resultSet.getString("Type");
            int count = resultSet.getInt("Total");
            typeCounts.add(new Pair<>(type, count));
        }
        return typeCounts;
    }

    /**
     * Retrieves the count of appointments grouped by the month of their start date.
     *
     * @return ObservableList of pairs where the key is the month name and the value is the count
     * @throws SQLException if database query fails
     */
    public ObservableList<Pair<String, Integer>> getAppointmentsCountByMonth() throws SQLException {
        String query =  "SELECT MONTHNAME(Start) as Month, COUNT(*) as Total \n" +
                "FROM appointments \n" +
                "GROUP BY MONTH(Start), MONTHNAME(Start)\n";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Pair<String, Integer>> monthCounts = FXCollections.observableArrayList();
        while(resultSet.next()){
            String month = resultSet.getString("Month");
            int count = resultSet.getInt("Total");
            monthCounts.add(new Pair<>(month, count));
        }
        return monthCounts;
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
}
