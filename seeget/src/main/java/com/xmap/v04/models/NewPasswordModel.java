package com.xmap.v04.models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewPasswordModel {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String newPassword;

    public NewPasswordModel() {
    }

    public NewPasswordModel(String username, String password, String newPassword) {
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
