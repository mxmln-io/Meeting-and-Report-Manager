package model;

import java.time.LocalDateTime;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String country;
    private String state;
    private String postalCode;
    private String phoneNumber;
    /* private LocalDateTime createDateTime;
    private String createdBy;
    private LocalDateTime lastUpdateDateTime;
    private String lastUpdatedBy; */
    private int divisionId;

    public Customer(int id, String name, String address,
                    //String country, String state,
                    String postalCode, String phoneNumber,
                    //LocalDateTime createDateTime, String createdBy, LocalDateTime lastUpdateDateTime, String lastUpdatedBy,
                    int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        //this.country = country;
        //this.state = state;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        /* this.createDateTime = createDateTime;
        this.createdBy = createdBy;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.lastUpdatedBy = lastUpdatedBy; */
        this.divisionId = divisionId;
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
    /*
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
    */
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

    // Create Date and Time
    /* public LocalDateTime getCreateDateTime() {return createDateTime;}

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    // Created By
    public String getCreatedBy() {return createdBy;}

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // Last Update Date and Time
    public LocalDateTime getLastUpdateDateTime() {return lastUpdateDateTime;}

    public void setLastUpdateDateTime(LocalDateTime lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    // Last Updated By
    public String getLastUpdatedBy() {return lastUpdatedBy;}

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    */

    // Division ID
    public int getDivisionId() {return divisionId;}

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}

