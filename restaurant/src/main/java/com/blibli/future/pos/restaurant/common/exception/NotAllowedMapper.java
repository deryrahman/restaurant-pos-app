package com.blibli.future.pos.restaurant.common.exception;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAllowedMapper extends BaseExceptionMapper<NotAllowedException> {
    @Override
    public int generateStatus() {
        return 405;
    }

    @Override
    public String getExceptionName() {
        return "Not Allowed Exception";
    }
}
