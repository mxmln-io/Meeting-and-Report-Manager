package dataaccessobjects;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public ObservableList<Customer> getAllCustomersObservable() throws SQLException {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        String query = "SELECT * FROM customers";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("Customer_ID");
            String name = resultSet.getString("Customer_Name");
            String address = resultSet.getString("Address");
            //String country = resultSet.getString("Country");
            //String state = resultSet.getString("State");
            String postalCode = resultSet.getString("Postal_Code");
            String phoneNumber = resultSet.getString("Phone");
            int divisionId = resultSet.getInt("Division_ID");

            Customer customer = new Customer(id, name, address, postalCode, phoneNumber, divisionId);
            customerList.add(customer);
        }

        return customerList;
    }
}
