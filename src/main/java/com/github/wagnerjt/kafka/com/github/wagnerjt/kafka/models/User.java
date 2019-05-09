package com.github.wagnerjt.kafka.com.github.wagnerjt.kafka.models;

/**
 * Identifies some unique User
 */
public class User {
    private String id;
    private String firstName;
    private String lastName;

    public User(String id) {
        this.id = id;
    }

    public User(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
