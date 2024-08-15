package com.example.myapplication.Models;

public class UpdatedUser {

    private String name;
    private String email;
    private String location;
    private String role;

    public UpdatedUser() {
    }

    public UpdatedUser(String name, String email, String location, String role) {
        this.name = name;
        this.email = email;
        this.location = location;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
