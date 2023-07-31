package dataaccessobjects;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO {

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
