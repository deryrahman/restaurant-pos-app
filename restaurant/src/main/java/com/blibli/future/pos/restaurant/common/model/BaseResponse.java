package com.blibli.future.pos.restaurant.common.model;

public class BaseResponse {
    private boolean success;
    private String message;
    private Object payload;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
                ", message='" + message + '\'' +
                ", payload=" + payload +
                '}';
    }
}
