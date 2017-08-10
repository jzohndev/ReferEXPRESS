package com.quickenloans.referexpress.data;

/**
 * Created by jdeville on 6/21/17.
 */

public final class User {

    // Properties
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    // DEFAULTS
    private static final int DEFAULT_ID = 0;
    private static final String DEFAULT_FIRST_NAME = "";
    private static final String DEFAULT_LAST_NAME = "";
    private static final String DEFAULT_EMAIL = "";

    // Getters and Setters
    public int getId(){ return id; }

    public void setId(int value){this.id = value;}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(){
        this.Initialize();
    }

    // Constructors
    public User(
            String firstName,
            String lastName,
            String email
    ) {
        this();

        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public User(
            int id,
            String firstName,
            String lastName,
            String email
    ) {
        this();

        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    //Instance methods
    private void Initialize(){
        setId(DEFAULT_ID);
        setFirstName(DEFAULT_FIRST_NAME);
        setLastName(DEFAULT_LAST_NAME);
        setEmail(DEFAULT_EMAIL);
    }

    // Static methods
    public String getFullName(){
        return firstName + " " + lastName;
    }
}
