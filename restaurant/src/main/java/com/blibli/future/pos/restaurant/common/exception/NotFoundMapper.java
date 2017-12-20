package com.blibli.future.pos.restaurant.common.exception;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundMapper extends BaseExceptionMapper<NotFoundException> {
    @Override
    public Integer getStatusCode() {
        return Response.Status.NOT_FOUND.getStatusCode();
    }
}
