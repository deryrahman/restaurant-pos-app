package com.blibli.future.pos.restaurant.dao.util;

public class ErrorHandler{
    private String msg;

    public ErrorHandler() {
        msg = "";
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg += msg + "\n";
    }
}