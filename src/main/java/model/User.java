package model;

/**
 * Represents a user in the system, including their credentials and associated metadata.
 */
public class User {
    private int id;
    private String name;
    private String password;

    /**
     * Constructs a new User with the specified id, name, and password.
     *
     * @param id       the user's unique ID
     * @param name     the name of the user
     * @param password the user's password for authentication
     */
    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    /**
     * Returns the user's unique ID.
     *
     * @return the user's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user's unique ID.
     *
     * @param id the ID to set for the user
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name to set for the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the password of the user.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     *
     * @param password the password to set for the user
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
