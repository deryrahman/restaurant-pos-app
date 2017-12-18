package com.blibli.future.pos.restaurant.common.exception;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundMapper extends BaseExceptionMapper<NotFoundException> {
    @Override
    public int generateStatus() {
        return 404;
    }

    @Override
    public String getExceptionName() {
        return "Not Found Exception";
    }
}
