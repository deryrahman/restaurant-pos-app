package com.blibli.future.pos.restaurant.common.exception;

import javax.ws.rs.ext.Provider;
import java.sql.SQLException;

@Provider
public class SQLExceptionMapper extends BaseExceptionMapper<SQLException> {

    @Override
    public int generateStatus() {
        return 500;
    }

    @Override
    public String getExceptionName() {
        return "SQL Exception";
    }
}
