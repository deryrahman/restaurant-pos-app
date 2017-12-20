package com.blibli.future.pos.restaurant.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper extends BaseExceptionMapper<Exception> {
    @Override
    public Integer getStatusCode() {
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }
}
