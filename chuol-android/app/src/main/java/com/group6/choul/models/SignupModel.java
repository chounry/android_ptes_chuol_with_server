package com.group6.choul.models;

public class SignupModel {
    private String fname,lname,email,phone,password;

    public SignupModel(String fname, String lname, String email, String phone, String password) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
