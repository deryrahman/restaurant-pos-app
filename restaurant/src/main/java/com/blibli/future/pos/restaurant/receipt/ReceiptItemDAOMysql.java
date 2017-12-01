package com.blibli.future.pos.restaurant.receipt;


import com.blibli.future.pos.restaurant.common.MysqlDAO;
import com.blibli.future.pos.restaurant.common.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ReceiptItemDAOMysql extends MysqlDAO implements ReceiptItemDAO {
    private void setItemFromQuery(Item item, ResultSet rs) throws SQLException {
        item.setId(rs.getInt("items.id"));
        item.setName(rs.getString("name"));
        item.setTimestampCreated(rs.getTimestamp("items.timestamp_created"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setDescription(rs.getString("description"));
        item.setCategoryId(rs.getInt("category_id"));
        item.setStatus(rs.getString("status"));
        item.setCount(rs.getInt("count_item"));
        item.autoSetHref();
    }
    @Override
    public boolean createBulk(int receiptId, List<Item> items) {
        if (!open()) {
            return false;
        }
        try {
            String query = "INSERT INTO receipt_item(receipt_id, item_id, count_item)" +
                    " VALUES(?, ?, ?)";
            ps = conn.prepareStatement(query);
            for (Item item:items) {
                ps.setInt(1, receiptId);
                ps.setInt(2, item.getId());
                ps.setInt(3, item.getCount());
                ps.addBatch();
            }

            int[] affecteds = ps.executeBatch();
            boolean complete = true;
            for (int affected: affecteds) {
                if(affected <= 0){
                    complete = false;
                }
            }
            if (complete) {
                return true;
            } else {
                message.setMessage("There are several items not successfully inserting");
                return true;
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on create receipt");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    @Override
    public ArrayList<Item> getBulkByReceiptId(int receiptId) {
        ArrayList<Item> items = new ArrayList<>();
        if (!open()) {
            return items;
        }
        try {
            String query = "SELECT  receipts.*, items.*, receipt_item.count_item as count_item FROM receipt_item JOIN receipts JOIN items WHERE receipts.id=receipt_item.receipt_id AND items.id=receipt_item.item_id AND receipts.id="+receiptId;
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Item item = new Item();
                setItemFromQuery(item, rs);
                items.add(item);
            }
            return items;
        } catch (SQLException e) {
            message.setMessage("Something wrong on getBulk items");
            e.printStackTrace();
        } finally {
            close();
        }
        return items;
    }
}
