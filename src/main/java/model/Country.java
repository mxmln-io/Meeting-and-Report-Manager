package model;

/**
 * Represents a country with a unique ID and name.
 */
public class Country {
    private int id;
    private String name;

    /**
     * Initializes a new instance of the Country class.
     *
     * @param id   the unique ID of the country
     * @param name the name of the country
     */
    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the ID of the country.
     *
     * @return the ID of the country
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the country.
     *
     * @param id the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the country.
     *
     * @return the name of the country
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the country.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
