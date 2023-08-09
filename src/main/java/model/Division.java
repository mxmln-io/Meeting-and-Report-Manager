package model;

/**
 * Represents a division or region within a country.
 */
public class Division {
    private int id;
    private String name;
    private int countryId;

    /**
     * Constructs a new Division with the specified id, name, and country ID.
     *
     * @param id        the division's unique ID
     * @param name      the name of the division
     * @param countryId the ID of the country this division belongs to
     */
    public Division(int id, String name, int countryId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }

    /**
     * Returns the division's unique ID.
     *
     * @return the division's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the division's unique ID.
     *
     * @param id the ID to set for the division
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the division.
     *
     * @return the name of the division
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the division.
     *
     * @param name the name to set for the division
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the country ID the division belongs to.
     *
     * @return the country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the country ID for the division.
     *
     * @param countryId the country ID to set for the division
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
