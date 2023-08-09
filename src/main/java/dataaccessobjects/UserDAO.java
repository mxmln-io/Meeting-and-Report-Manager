package dataaccessobjects;
import model.User;
import helper.JDBC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access object (DAO) for handling user-related database operations.
 */
public class UserDAO {

    /**
     * Retrieves an observable list of all users from the database.
     *
     * @return ObservableList containing all User objects from the database.
     * @throws SQLException if database query fails.
     */
    public static ObservableList<User> getAllUsersObservable() throws SQLException {

        ObservableList<User> userList = FXCollections.observableArrayList();

        String query = "SELECT * FROM users";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("User_ID");
            String name = resultSet.getString("User_Name");
            String password = resultSet.getString("Password");
            User user = new User(id, name, password);
            userList.add(user);
        }
        return userList;
    }

    /**
     * Verifies if a user with the specified username and password exists in the database.
     *
     * @param username Username to be verified.
     * @param password Password to be verified.
     * @return true if a user with the specified username and password exists, otherwise false.
     * @throws SQLException if database query fails.
     */
    public boolean verifyUser(String username, String password) throws SQLException {

        String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return true;
        }

        return false;
    }
}
