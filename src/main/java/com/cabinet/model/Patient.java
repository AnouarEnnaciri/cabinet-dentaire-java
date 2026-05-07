package com.cabinet.model;

// Patient inherits id, dateCreation, dateModification, creePar modifiePar from BaseEntity
public class Patient extends BaseEntity {
    private String name;      // full name of the patient
    private String phone;     // contact number
    private String email;     // email address for notifications
    private int age;          // age in years


    // empty constructor needed so we can create object then fill later

    public Patient() {
        super(); // calls BaseEntity constructor to set dateCreation
    }

    // constructor used when we have all data ready from form

    public Patient(String name, String phone, String email, int age) {
        super(); // sets dateCreation automatically
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.age = age;
        // id is inherited from BaseEntity database will generate it
    }

    // getters to read values
    public String getName() {
        return name;
    }

    // setters anddd this is to set values / change them
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}