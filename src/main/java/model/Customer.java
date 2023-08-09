package model;

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

    // Division ID
    public int getDivisionId() {return divisionId;}

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}

