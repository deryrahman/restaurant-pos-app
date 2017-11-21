package com.blibli.future.pos.restaurant.dao;

import com.blibli.future.pos.restaurant.Metadata;
import com.blibli.future.pos.restaurant.MySQLUtility;
import com.blibli.future.pos.restaurant.model.Category;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class CategoryDAOMysql implements CategoryDAO {
    @Override
    public boolean create(Category category) {
        Connection conn = null;
        try {
            BasicDataSource ds = MySQLUtility.getDataSource();
            conn = ds.getConnection();
            System.out.println(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public Category getById(int id) {
        return null;
    }

    @Override
    public Category[] getAll() {
        return new Category[0];
    }

    @Override
    public Category[] getWithMetadata(Metadata metadata) {
        return new Category[0];
    }

    @Override
    public boolean delete(Category category) {
        return false;
    }

    @Override
    public boolean update(Category category) {
        return false;
    }
}
