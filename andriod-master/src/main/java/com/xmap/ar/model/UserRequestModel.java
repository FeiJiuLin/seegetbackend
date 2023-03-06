package com.xmap.ar.model;

import lombok.ToString;

@ToString
public class UserRequestModel {
    private String phone;
    private String password;

    public UserRequestModel(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}