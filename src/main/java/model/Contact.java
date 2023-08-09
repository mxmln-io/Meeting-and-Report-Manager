package model;

/**
 * Represents a contact with a unique ID, name, and email.
 */
public class Contact {
    private int id;
    private String name;
    private String email;

    /**
     * Initializes a new instance of the Contact class.
     *
     * @param id    the unique ID of the contact
     * @param name  the name of the contact
     * @param email the email address of the contact
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the ID of the contact.
     *
     * @return the ID of the contact
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the contact.
     *
     * @param id the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the contact.
     *
     * @return the name of the contact
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the contact.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the email address of the contact.
     *
     * @return the email address of the contact
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the contact.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
