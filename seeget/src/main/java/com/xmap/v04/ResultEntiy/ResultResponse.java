package com.xmap.v04.ResultEntiy;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResultResponse<D> {

    private final Integer code;

    private final String message;

    private D data;

    public ResultResponse(ResultEnum resultEnum, String msg, D data){
        this.code = resultEnum.getCode();
        this.message = msg == null?resultEnum.getMessage():msg;
        this.data = data;

    }

    public ResultResponse(ResultEnum resultEnum, String msg){
        this.code = resultEnum.getCode();
        this.message = msg == null?resultEnum.getMessage():msg;
    }

    public ResultResponse(ResultEnum resultEnum){
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code.equals(ResultEnum.SUCCESS.getCode());
    }

}
