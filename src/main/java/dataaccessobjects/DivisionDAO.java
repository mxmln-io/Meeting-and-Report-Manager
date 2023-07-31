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
}
