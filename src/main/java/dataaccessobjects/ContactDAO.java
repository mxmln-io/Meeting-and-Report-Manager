package dataaccessobjects;

import helper.JDBC;
import model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDAO {
    // Method to retrieve an ObservableList of all contacts from the database
    public ObservableList<Contact> getAllContactsObservable() throws SQLException {
        // Initialize an ObservableList to store the results
        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        // Define the SQL query to retrieve all contacts
        String query = "SELECT * FROM contacts";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);

        // Execute the query
        ResultSet resultSet = preparedStatement.executeQuery();

        // Iterate through the results and create Contact objects for each row
        while (resultSet.next()) {
            int id = resultSet.getInt("Contact_ID");
            String name = resultSet.getString("Contact_Name");
            String email = resultSet.getString("Email");
            Contact contact = new Contact(id, name, email);
            contactList.add(contact);
        }
        return contactList;
    }
}
