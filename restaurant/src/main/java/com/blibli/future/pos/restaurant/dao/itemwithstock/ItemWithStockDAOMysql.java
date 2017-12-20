package com.blibli.future.pos.restaurant.dao.itemwithstock;

import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.common.model.Item;
import com.blibli.future.pos.restaurant.common.model.custom.ItemWithStock;
import com.blibli.future.pos.restaurant.common.model.Restaurant;
import com.blibli.future.pos.restaurant.dao.MysqlDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ItemWithStockDAOMysql extends MysqlDAO<ItemWithStock> implements ItemWithStockDAO{
    private Item item;
    private ItemWithStock itemWithStock;
    private Restaurant restaurant;
    private List<ItemWithStock> itemWithStockList;

    private void removeItemAttr(){
        itemWithStock.setItemId(null);
        itemWithStock.setItemName(null);
    }

    private void removeRestaurantAttr(){
        itemWithStock.setRestaurantId(null);
        itemWithStock.setRestaurantAddress(null);
    }

    @Override
    protected void mappingObject(ItemWithStock itemWithStock, ResultSet rs) throws SQLException {
        itemWithStock.setItemId(rs.getInt("items.id"));
        itemWithStock.setItemName(rs.getString("items.name"));
        itemWithStock.setRestaurantId(rs.getInt("restaurants.id"));
        itemWithStock.setRestaurantAddress(rs.getString("restaurants.address"));
        itemWithStock.setStock(rs.getInt("stock"));
    }

    @Override
    public void create(Integer restaurantId, Integer itemId, Integer stock) throws SQLException {
        String query = "INSERT INTO restaurant_item(restaurant_id, item_id, stock)" +
                " VALUES(?, ?, ?)";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setInt(1, restaurantId);
        ps.setInt(2, itemId);
        ps.setInt(3, stock);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No restaurantItem inserting");
        }
    }

    @Override
    public ItemWithStock findById(Integer restaurantId, Integer itemId) throws SQLException {
        itemWithStock = new ItemWithStock();
        itemWithStockList = find("restaurants.id = "+ restaurantId+" AND items.id = "+itemId);

        if(itemWithStockList.size()>0){
            itemWithStock = itemWithStockList.get(0);
        }
        return itemWithStock;
    }

    @Override
    public List<ItemWithStock> findByRestaurantId(Integer restaurantId, String filter) throws SQLException {
        itemWithStockList = find("restaurants.id = "+ restaurantId);
        for (ItemWithStock itemWithStock1: itemWithStockList) {
            itemWithStock = itemWithStock1;
            removeRestaurantAttr();
        }
        return itemWithStockList;
    }

    @Override
    public List<ItemWithStock> findByItemId(Integer itemId, String filter) throws SQLException {
        itemWithStockList = find("items.id = "+ itemId);
        for (ItemWithStock itemWithStock1: itemWithStockList) {
            itemWithStock = itemWithStock1;
            removeItemAttr();
        }
        return itemWithStockList;
    }

    @Override
    public List<ItemWithStock> find(String filter) throws SQLException {
        itemWithStockList = new ArrayList<>();
        String query = "SELECT items.*, restaurants.*, restaurant_item.stock as stock " +
                "FROM restaurants JOIN restaurant_item JOIN items " +
                "WHERE restaurants.id=restaurant_item.restaurant_id AND items.id=restaurant_item.item_id AND " +
                filter;
        ps = TransactionHelper.getConnection().prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            itemWithStock = new ItemWithStock();
            mappingObject(itemWithStock, rs);
            itemWithStockList.add(itemWithStock);
        }
        return itemWithStockList;
    }

    @Override
    public void delete(Integer restaurantId, Integer itemId) throws SQLException {
        String query = "DELETE FROM restaurant_item WHERE restaurant_id = ? AND item_id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setInt(1, restaurantId);
        ps.setInt(2, itemId);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No item deleted");
        }
    }

    @Override
    public void update(Integer restaurantId, Integer itemId, Integer stock) throws SQLException {
        String query = "UPDATE restaurant_item SET " +
                "stock = ? +" +
                "WHERE restaurant_id = ?" +
                "AND item_id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setInt(1, stock);
        ps.setInt(2, restaurantId);
        ps.setInt(3, itemId);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No restaurant item update");
        }
    }

}
