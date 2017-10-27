package com.blibli.future.pos.dao_impl;

import com.blibli.future.pos.dao.ItemDAO;
import com.blibli.future.pos.entity.Item;
import com.blibli.future.pos.util.JDBCConn;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dery on 10/27/17.
 */
public class ItemDAOImpl implements ItemDAO {
    private List<Item> items;
    Hashtable<Long, Item> hashItems;
    private int size;
    private Connection conn;
    Statement stmt;
    PreparedStatement pstmt;

    public ItemDAOImpl() {
        hashItems = new Hashtable<Long, Item>();
        conn = null;
        stmt = null;
        try {
            conn = JDBCConn.getConnection();
            stmt = conn.createStatement();
            String query = "SELECT * FROM items";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //Retrieve by column name
                Long ID = Long.valueOf(rs.getInt("ID"));
                String name = rs.getString("name");
                String price = rs.getString("price");
                String description = rs.getString("description");

                // Create item
                Item item = new Item(ID);
                item.setName(name);
                item.setPrice(price);
                item.setDescription(description);

                // Add to hash
                hashItems.put(ID, item);
            }
            items = new ArrayList<Item>(hashItems.values());
            size = items.size();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Item> getAllItems() {
        if (size == 0) return null;
        return items;
    }

    public Item getItem(Long id) {
        if (id > size) return null;
        return hashItems.get(id);
    }

    public boolean createItem(Item item) {
        conn = null;
        pstmt = null;
        String query = "INSERT INTO items (ID,date_created, name, price, description, category_id, status)" +
                "VALUES (NULL,?, ?, ?, ?, ?, ?);";

        String datetime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(item.getCreatedDate());
        String name = item.getName();
        String price = item.getPrice();
        String description = item.getDescription();
        Long categoyId = item.getCategoryId();
        String status = item.getStatus();
        try {
            conn = JDBCConn.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, datetime);
            pstmt.setString(2, name);
            pstmt.setString(3, price);
            pstmt.setString(4, description);
            pstmt.setLong(5, categoyId!=null?categoyId:0);
            pstmt.setString(6, status!=null?status:"draft");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void updateItem(Item item) {

    }

    public void deleteItemById(Long id) {

    }
}
