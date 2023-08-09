package model;

import java.time.LocalDateTime;

/**
 * Represents an appointment in the scheduling system.
 */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int customerId;
    private int userId;
    private int contactId;

    /**
     * Constructs an instance of the Appointment.
     *
     * @param id            the appointment's unique ID
     * @param title         the appointment's title
     * @param description   the appointment's description
     * @param location      the appointment's location
     * @param type          the type/category of the appointment
     * @param startDateTime the start time and date of the appointment
     * @param endDateTime   the end time and date of the appointment
     * @param customerId    the ID of the associated customer
     * @param userId        the ID of the associated user
     * @param contactId     the ID of the associated contact
     */
    public Appointment(int id, String title, String description, String location, String type,
                       LocalDateTime startDateTime, LocalDateTime endDateTime,
                       int customerId, int userId, int contactId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    // ... getters and setters with appropriate JavaDoc comments ...

    /**
     * Returns the appointment's unique ID.
     *
     * @return the ID of the appointment
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the appointment's unique ID.
     *
     * @param id the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the title of the appointment.
     *
     * @return the title of the appointment
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the appointment.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description of the appointment.
     *
     * @return the description of the appointment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the appointment.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the location of the appointment.
     *
     * @return the location of the appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the appointment.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the type of the appointment.
     *
     * @return the type of the appointment
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the appointment.
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the start date and time of the appointment.
     *
     * @return the start date and time of the appointment
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the start date and time of the appointment.
     *
     * @param startDateTime the start date and time to set
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Returns the end date and time of the appointment.
     *
     * @return the end date and time of the appointment
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets the end date and time of the appointment.
     *
     * @param endDateTime the end date and time to set
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Returns the ID of the associated customer.
     *
     * @return the ID of the associated customer
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the ID of the associated customer.
     *
     * @param customerId the customer ID to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns the ID of the associated user.
     *
     * @return the ID of the associated user
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the associated user.
     *
     * @param userId the user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Returns the ID of the associated contact.
     *
     * @return the ID of the associated contact
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the ID of the associated contact.
     *
     * @param contactId the contact ID to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
