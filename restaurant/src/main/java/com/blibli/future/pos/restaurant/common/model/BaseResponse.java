package com.blibli.future.pos.restaurant.common.model;

public class BaseResponse {
    private Boolean success;
    private Integer statusCode;
    private String message;
    private Object payload;

    public BaseResponse() {
    }

    public BaseResponse(Boolean success, Integer statusCode) {
        this.success = success;
        this.statusCode = statusCode;
    }

    public BaseResponse(Boolean success, Integer statusCode, String message) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
    }

    public BaseResponse(Boolean success, Integer statusCode, Object payload) {
        this.success = success;
        this.statusCode = statusCode;
        this.payload = payload;
    }

    public BaseResponse(Boolean success, Integer statusCode, String message, Object payload) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.payload = payload;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "success=" + success +
                ", statusCode='" + statusCode + '\'' +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                '}';
    }
}
