package com.xmap.v04.models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRequestModel {
    @NotNull
    private String username;
    @NotNull
    private String password;

    public UserRequestModel() {
    }

    public UserRequestModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
