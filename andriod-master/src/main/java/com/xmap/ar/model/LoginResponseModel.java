package com.xmap.ar.model;

public class LoginResponseModel {

    private final String accessToken;
    private final String refreshToken;


    public LoginResponseModel(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
