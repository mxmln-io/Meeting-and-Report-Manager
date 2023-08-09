package model;

/**
 * Represents a customer with an ID, name, address, postal code, phone number, and division ID.
 */
public class Customer {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int divisionId;

    /**
     * Initializes a new instance of the Customer class.
     *
     * @param id         the unique ID of the customer
     * @param name       the name of the customer
     * @param address    the address of the customer
     * @param postalCode the postal code of the customer
     * @param phoneNumber the phone number of the customer
     * @param divisionId  the ID of the division the customer belongs to
     */
    public Customer(int id, String name, String address,
                    String postalCode, String phoneNumber,
                    int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionId = divisionId;
    }

    /**
     * Returns the ID of the customer.
     *
     * @return the ID of the customer
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the customer.
     *
     * @param id the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the customer.
     *
     * @return the name of the customer
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the customer.
     *
     * @param name the name to set for the customer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the address of the customer.
     *
     * @return the address of the customer
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address the address to set for the customer
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the postal code of the customer.
     *
     * @return the postal code of the customer
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code for the customer.
     *
     * @param postalCode the postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Returns the phone number of the customer.
     *
     * @return the phone number of the customer
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number for the customer.
     *
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the division ID the customer belongs to.
     *
     * @return the division ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the division ID for the customer.
     *
     * @param divisionId the division ID to set
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}



