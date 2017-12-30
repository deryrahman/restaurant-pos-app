package com.blibli.future.pos.restaurant.common.exception;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAuthorizedMapper extends BaseExceptionMapper<NotAuthorizedException> {
    @Override
    public Integer getStatusCode() {
        return Response.Status.UNAUTHORIZED.getStatusCode();
    }
}
