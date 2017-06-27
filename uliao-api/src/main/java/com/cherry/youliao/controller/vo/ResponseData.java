package com.cherry.youliao.controller.vo;

import lombok.Data;

@Data
public class ResponseData<T> {

    private boolean success = false;

    private String message = "";

    private T data;

    public static <T> ResponseData<T> success(T data) {
        return success("okay", data);
    }

    public static <T> ResponseData<T> error(String message) {
        return error(message, null);
    }

    public static <T> ResponseData<T> success(String message, T data) {
        ResponseData<T> response = new ResponseData<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> ResponseData<T> error(String message, T data) {
        ResponseData<T> response = new ResponseData<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(data);

        return response;
    }

}
