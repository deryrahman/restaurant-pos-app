package com.blibli.future.pos.restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class MysqlDAO<T>  {
    protected Connection conn;
    protected PreparedStatement ps = null;

    protected abstract void mappingObject(T obj, ResultSet rs) throws SQLException;
//
//    public MysqlDAO(){
//        conn = TransactionHelper.getConnection();
//    }
}
