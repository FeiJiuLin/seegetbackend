package com.xmap.ar.model;


public class NewPasswordModel {
    private String phone;
    private String password;
    private String newPassword;

    public NewPasswordModel() {
    }

    public NewPasswordModel(String username, String password, String newPassword) {
        this.phone = username;
        this.password = password;
        this.newPassword = newPassword;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}