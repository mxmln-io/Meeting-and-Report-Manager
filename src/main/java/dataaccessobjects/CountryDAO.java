package dataaccessobjects;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access object (DAO) for handling country database operations.
 */
public class CountryDAO {

    /**
     * Retrieves an observable list of all countries from the database.
     *
     * @return ObservableList containing all countries
     * @throws SQLException if database query fails
     */
    public ObservableList<Country> getAllCountriesObservable() throws SQLException {
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        String query = "SELECT * FROM countries";
        PreparedStatement preparedStatement = JDBC.openConnection().prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("Country_ID");
            String name = resultSet.getString("Country");
            Country country = new Country(id, name);
            countryList.add(country);
        }

        return countryList;
    }
}
