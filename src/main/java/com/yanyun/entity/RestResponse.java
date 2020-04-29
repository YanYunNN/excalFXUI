package com.yanyun.entity;


public class RestResponse<RT> {
    private int code;
    private String message;
    private RT result;

    public RestResponse() {
    }

    public RestResponse(int code, String message, RT result) {
        this.setCode(code);
        this.setMessage(message);
        this.setResult(result);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setResult(RT result) {
        this.result = result;
    }

    public RT getResult() {
        return this.result;
    }

    public static <T> RestResponse<T> good(T result) {
        return result(0, "OK", result);
    }

    public static <T> RestResponse<T> result(int code, String message, T result) {
        return new RestResponse(code, message, result);
    }

    public static <T> RestResponse<T> bad(int code, String message, T result) {
        return result(code, message, result);
    }

    public static <T> RestResponse<T> bad(int code, String message) {
        return result(code, message, null);
    }

}
