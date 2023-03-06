package com.xmap.v04.models;

public class LoginResponseModel {

    private final String accessToken;
    private final String refreshToken;
    private final String userUuid;
    private final String defaultData;


    public LoginResponseModel(String accessToken, String refreshToken, String userUuid, String defaultData) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userUuid = userUuid;
        this.defaultData = defaultData;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public String getDefaultData() { return defaultData; }
}

