package model;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    //private LocalDateTime createDateTime;
    //private String createdBy;
    //private LocalDateTime lastUpdateDateTime;
    //private String lastUpdatedBy;
    private int customerId;
    private int userId;
    private int contactId; // Assuming contactId is int type.

    public Appointment(int id, String title, String description, String location, String type,
                       LocalDateTime startDateTime, LocalDateTime endDateTime,
                       //LocalDateTime createDateTime, String createdBy, LocalDateTime lastUpdateDateTime, String lastUpdatedBy,
                       int customerId, int userId, int contactId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        //this.createDateTime = createDateTime;
        //this.createdBy = createdBy;
        //this.lastUpdateDateTime = lastUpdateDateTime;
        //this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    // Getters and Setters for all fields
    // ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Location
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Start Date and Time
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    // End Date and Time
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    // Create Date and Time
    /*
    public LocalDateTime getCreateDateTime() {return createDateTime;}

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    // Created By
     public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // Last Update Date and Time
    public LocalDateTime getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(LocalDateTime lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    // Last Updated By
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    */

    // Customer ID
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    // User ID
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Contact ID
    public int getContactId() {return contactId;}

    public void setContactId(int contactId) {this.contactId = contactId;}
}
