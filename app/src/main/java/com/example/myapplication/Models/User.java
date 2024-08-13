package com.example.myapplication.Models;

import com.google.gson.annotations.SerializedName;

public class User {



    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SerializedName("password")
    private String password;

    @SerializedName("location")
    private String location;

    @SerializedName("role")

    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getter for id

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for age


    // Getter for location
    public String getLocation() {
        return location;
    }

    // Setter for location
    public void setLocation(String location) {
        this.location = location;
    }

    public User( String name, String email, String password,String location,String role) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.role = role;
    }
}
