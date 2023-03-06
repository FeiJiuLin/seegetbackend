package com.xmap.v04.ResultEntiy;

public enum ResultEnum {

    SUCCESS(200, "成功"),

    NO_PERMISSION(403, "权限不足"),

    NOT_FOUND(404, "资源不存在"),

    SERVER_ERROR(500, "服务器错误");

    private final Integer code;

    private final String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResultEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}


