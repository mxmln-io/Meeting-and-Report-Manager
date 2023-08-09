package dataaccessobjects;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionDAO {

    public ObservableList<Division> getAllDivisionsObservable() throws SQLException {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();

        String query = "SELECT * FROM first_level_divisions";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("Division_ID");
            String name = resultSet.getString("Division");
            int countryId = resultSet.getInt("Country_ID");

            Division division = new Division(id, name, countryId);
            divisionList.add(division);
        }

        return divisionList;
    }

    public Division getDivisionById(int id) throws SQLException {
        String query = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String name = resultSet.getString("Division");
            int countryId = resultSet.getInt("Country_ID");

            return new Division(id, name, countryId);
        }

        return null;
    }

    public String getCountryNameByDivisionId(int divisionId) throws SQLException {
        Division division = getDivisionById(divisionId);
        if (division != null) {
            int countryId = division.getCountryId();

            switch (countryId) {
                case 1:
                    return "USA";
                case 2:
                    return "UK";
                case 3:
                    return "CAN";
                default:
                    return "Unknown Country ID";
            }
        }
        return "Unknown Division ID";
    }

    public ObservableList<Division> getDivisionsByCountryId(int countryId) throws SQLException {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();

        String query = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
        preparedStatement.setInt(1, countryId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("Division_ID");
            String name = resultSet.getString("Division");
            int countryIdFromDb = resultSet.getInt("Country_ID");

            Division division = new Division(id, name, countryIdFromDb);
            divisionList.add(division);
        }

        return divisionList;
    }

    public int getDivisionIdByDivisionName(String divisionName) throws SQLException {
        String query = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
        preparedStatement.setString(1, divisionName);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("Division_ID");
        }

        return -1; // Return -1 or any other value to indicate "not found".
    }

}
