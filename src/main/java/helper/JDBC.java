package helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * JDBC helper class.
 * This class provides methods to open and close a connection to the database.
 */
public abstract class JDBC {

    private static final String PROTOCOL = "jdbc";
    private static final String VENDOR = ":mysql:";
    private static final String LOCATION = "//localhost/";
    private static final String DATABASE_NAME = "client_schedule";
    private static final String JDBC_URL = PROTOCOL + VENDOR + LOCATION + DATABASE_NAME + "?serverTimezone=UTC";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USERNAME = "sqlUser";
    private static final String PASSWORD = "Passw0rd!";
    public static Connection connection;

    private JDBC() {}

    /**
     * Opens a database connection.
     * @return The active database connection.
     */
    public static Connection openConnection() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch(Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return connection;
    }

    /**
     * Closes the active database connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch(Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
