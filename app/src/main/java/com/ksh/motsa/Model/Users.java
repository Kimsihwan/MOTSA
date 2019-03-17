package com.ksh.motsa.Model;

public class Users {
    private String email, name, location, phone;

    public Users() {
    }

    public Users(String email, String name, String location, String phone) {
        this.email = email;
        this.name = name;
        this.location = location;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
