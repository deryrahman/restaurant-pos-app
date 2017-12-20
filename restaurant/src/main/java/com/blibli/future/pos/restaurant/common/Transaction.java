package com.blibli.future.pos.restaurant.common;

import java.sql.Connection;
import java.sql.SQLException;

public interface Transaction {
    public Object execute(Connection conn) throws SQLException;
}
