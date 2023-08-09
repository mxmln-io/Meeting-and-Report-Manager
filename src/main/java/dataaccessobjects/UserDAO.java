package dataaccessobjects;
import model.User;
import helper.JDBC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Method to retrieve an ObservableList of all users from the database
    public static ObservableList<User> getAllUsersObservable() throws SQLException {
        // Initialize an ObservableList to store the results
        ObservableList<User> userList = FXCollections.observableArrayList();

        // Define the SQL query to retrieve all users
        String query = "SELECT * FROM users";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);

        // Execute the query
        ResultSet resultSet = preparedStatement.executeQuery();

        // Iterate through the results and create User objects for each row
        while (resultSet.next()) {
            int id = resultSet.getInt("User_ID");
            String name = resultSet.getString("User_Name");
            String password = resultSet.getString("Password");
            User user = new User(id, name, password);
            userList.add(user);
        }
        return userList;
    }

    public boolean verifyUser(String username, String password) throws SQLException {
        // Define the SQL query to retrieve the user with the specified username and password
        String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);

        // Execute the query
        ResultSet resultSet = preparedStatement.executeQuery();

        // If there is a result, the user exists and the username and password are correct
        if (resultSet.next()) {
            return true;
        }

        // If there is no result, the user doesn't exist or the username and password are incorrect
        return false;
    }

}
