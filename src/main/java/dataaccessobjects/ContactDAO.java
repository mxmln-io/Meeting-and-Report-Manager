package dataaccessobjects;

import helper.JDBC;
import model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access object (DAO) for handling contact database operations.
 */
public class ContactDAO {
    /**
     * Retrieves an observable list of all contacts from the database.
     *
     * @return ObservableList containing all contacts
     * @throws SQLException if database query fails
     */
    public static ObservableList<Contact> getAllContactsObservable() throws SQLException {

        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        String query = "SELECT * FROM contacts";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("Contact_ID");
            String name = resultSet.getString("Contact_Name");
            String email = resultSet.getString("Email");
            Contact contact = new Contact(id, name, email);
            contactList.add(contact);
        }
        return contactList;
    }

    /**
     * Retrieves a contact associated with a particular contact ID.
     *
     * @param contactId the ID of the contact to retrieve
     * @return the Contact object if found, null otherwise
     * @throws SQLException if database query fails
     */
    public static Contact getContactById(Integer contactId) throws SQLException {
        String query = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        preparedStatement.setInt(1, contactId);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return new Contact(
                    resultSet.getInt("Contact_ID"),
                    resultSet.getString("Contact_Name"),
                    resultSet.getString("Email")
            );
        }
        return null;
    }

}
