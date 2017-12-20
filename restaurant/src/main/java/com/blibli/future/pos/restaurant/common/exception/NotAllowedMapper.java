package com.blibli.future.pos.restaurant.common.exception;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAllowedMapper extends BaseExceptionMapper<NotAllowedException> {
    @Override
    public Integer getStatusCode() {
        return Response.Status.METHOD_NOT_ALLOWED.getStatusCode();
    }
}
