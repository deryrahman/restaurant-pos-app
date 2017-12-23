package com.blibli.future.pos.restaurant.common.exception;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestMapper extends BaseExceptionMapper<BadRequestException> {
    @Override
    public Integer getStatusCode() {
        return Response.Status.BAD_REQUEST.getStatusCode();
    }
}
