package com.blibli.future.pos.restaurant.dao.item;

import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.dao.MysqlDAO;
import com.blibli.future.pos.restaurant.common.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ItemDAOMysql extends MysqlDAO<Item> implements ItemDAO{
    private Item item;
    private List<Item> items;

    @Override
    public void mappingObject(Item item, ResultSet rs) throws SQLException {
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setDescription(rs.getString("description"));
        item.setCategoryId(rs.getInt("category_id"));
        item.setStatus(rs.getString("status"));
    }

    @Override
    public void create(Item item) throws SQLException {
        String query = "INSERT INTO items(name, price, description, category_id, status)" +
                " VALUES(?, ?, ?, ?, ?)";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setString(1, item.getName());
        ps.setBigDecimal(2, item.getPrice());
        ps.setString(3, item.getDescription());
        if(item.getCategoryId() == null) {
            ps.setNull(4, Types.INTEGER);
        } else {
            ps.setInt(4, item.getCategoryId());
        }
        ps.setString(5, item.getStatus());

        int affected = ps.executeUpdate();
        if (affected > 0) {
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                item.setId(rs.getInt(1));
            }
        } else {
            throw new SQLException("No affected query. No item inserting");
        }
    }

    @Override
    public Item findById(int id) throws SQLException {
        item = new Item();

        items = find("id = "+ id);
        if(items.size()>0){
            item = items.get(0);
        }
        return item;
    }

    @Override
    public List<Item> find(String filter) throws SQLException {
        items = new ArrayList<>();
        String query = "SELECT * FROM items WHERE "+filter;
        ps = TransactionHelper.getConnection().prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Item item = new Item();
            mappingObject(item, rs);
            items.add(item);
        }
        return items;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM items WHERE id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setInt(1, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No item deleted");
        }
    }

    @Override
    public void update(int id, Item item) throws SQLException {
        String query = "UPDATE items SET " +
                "name = ?, " +
                "price = ?," +
                "description = ?," +
                "category_id = ?," +
                "status = ? +" +
                "WHERE id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setString(1, item.getName());
        ps.setBigDecimal(2, item.getPrice());
        ps.setString(3, item.getDescription());
        ps.setInt(4, item.getCategoryId());
        ps.setString(5, item.getStatus());
        ps.setInt(6, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No item update");
        }
    }

}
