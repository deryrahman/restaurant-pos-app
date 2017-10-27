package com.blibli.future.pos.dao_impl;

import com.blibli.future.pos.dao.ItemDAO;
import com.blibli.future.pos.entity.Item;
import com.blibli.future.pos.util.JDBCConn;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dery on 10/27/17.
 */
public class ItemDAOImpl implements ItemDAO {
    List<Item> items;

    public ItemDAOImpl() {
        items = new ArrayList<Item>();
        Connection conn = JDBCConn.getConnection();
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM items";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                //Retrieve by column name
                Long ID  = Long.valueOf(rs.getInt("ID"));
                String name = rs.getString("name");
                String price = rs.getString("price");

                items.add(new Item(ID,name,price));
                System.out.print(ID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Item> getAllItems() {
        return items;
    }

    public Item getItem(Long id) {
        return null;
    }

    public void updateItem(Item item) {

    }

    public void deleteItem(Item item) {

    }
}
