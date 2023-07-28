package model;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String country;
    private String state;
    private String postalCode;
    private String phoneNumber;

    public Customer(int id, String name, String address, String country, String state,
                    String postalCode, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.country = country;
        this.state = state;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters for all fields
    // ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Country
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // State/Province
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Postal Code
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    // Phone Number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

