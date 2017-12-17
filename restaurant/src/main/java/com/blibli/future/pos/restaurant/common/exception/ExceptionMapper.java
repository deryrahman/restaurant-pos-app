package com.blibli.future.pos.restaurant.common.exception;

import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper extends BaseExceptionMapper<Exception> {
    @Override
    public int generateStatus() {
        return 500;
    }

    @Override
    public String getExceptionName() {
        return "Exception";
    }
}
