package com.blibli.future.pos.restaurant.dao.custom.receiptwithitem;

import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.common.model.custom.ItemOnReceipt;
import com.blibli.future.pos.restaurant.common.model.custom.ReceiptWithItem;
import com.blibli.future.pos.restaurant.dao.MysqlDAO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptWithItemDAOMysql extends MysqlDAO<ReceiptWithItem> implements ReceiptWithItemDAO {
    private List<ReceiptWithItem> receiptWithItemList;
    private ReceiptWithItem receiptWithItem;
    private List<ItemOnReceipt> itemOnReceiptList;
    private ItemOnReceipt itemOnReceipt;


    @Override
    protected void mappingObject(ReceiptWithItem receiptWithItem, ResultSet rs) throws SQLException {
        receiptWithItem.setReceiptId(rs.getInt("receipt_id"));
        receiptWithItem.setMemberId(rs.getInt("member_id"));
        receiptWithItem.setNote(rs.getString("note"));
        receiptWithItem.setTax(rs.getBigDecimal("tax"));
    }

    private void mappingItem(ItemOnReceipt items, ResultSet rs) throws SQLException {
        items.setItemId(rs.getInt("item_id"));
        items.setItemName(rs.getString("name"));
        items.setCount(rs.getInt("count_item"));
        items.setSubTotal(rs.getBigDecimal("subtotal"));
    }

    @Override
    public void create(ReceiptWithItem receiptWithItem) throws SQLException {

        String query = "INSERT INTO receipt_item(receipt_id, item_id, count_item, subtotal)" +
                " VALUES(?, ?, ?, ?)";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        for (ItemOnReceipt item: receiptWithItem.getItems()) {
            ps.setInt(1, receiptWithItem.getReceiptId());
            ps.setInt(2, item.getItemId());
            ps.setInt(3, item.getCount());
            ps.setBigDecimal(4, item.getSubTotal());
            ps.addBatch();
        }

        int[] affecteds = ps.executeBatch();
        boolean complete = true;
        for (int affected: affecteds) {
            if(affected <= 0){
                complete = false;
            }
        }
        if (!complete) {
            throw new SQLException("There are several items not successfully inserting");
        }
    }

    @Override
    public ReceiptWithItem findById(Integer receiptId) throws SQLException {
        receiptWithItem = new ReceiptWithItem();

        receiptWithItemList = find("receipt_id = "+ receiptId);
        if(receiptWithItemList.size()>0){
            receiptWithItem = receiptWithItemList.get(0);
        }
        return receiptWithItem;
    }

    @Override
    public List<ReceiptWithItem> find(String filter) throws SQLException {
        receiptWithItemList = new ArrayList<>();
        List<Integer> receiptIdList = new ArrayList<>();

        String query = "SELECT DISTINCT receipt_item.receipt_id FROM receipt_item";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            receiptIdList.add(rs.getInt("receipt_id"));
        }

        for (Integer i: receiptIdList) {
            query = "SELECT receipts.id as receipt_id, items.id as item_id, items.name, receipt_item.count_item, receipt_item.subtotal, receipts.tax as tax, receipts.note as note " +
                    "FROM receipt_item JOIN receipts JOIN items " +
                    "WHERE receipts.id=receipt_item.receipt_id AND items.id=receipt_item.item_id AND receipts.id=" +
                    i + " AND " +
                    filter;
            ps = TransactionHelper.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            itemOnReceiptList = new ArrayList<>();
            BigDecimal tax = null;
            String note = null;
            while (rs.next()){
                if(tax == null){
                    tax = rs.getBigDecimal("tax");
                }
                if(note == null){
                    note = rs.getString("note");
                }
                itemOnReceipt = new ItemOnReceipt();
                mappingItem(itemOnReceipt,rs);
                itemOnReceiptList.add(itemOnReceipt);
            }
            if(itemOnReceiptList.isEmpty()){
                continue;
            }
            receiptWithItem = new ReceiptWithItem();
            receiptWithItem.setReceiptId(i);
            receiptWithItem.setNote(note);
            receiptWithItem.setTax(tax);
            receiptWithItem.setItems(itemOnReceiptList);
            receiptWithItemList.add(receiptWithItem);
        }
        return receiptWithItemList;
    }

    @Override
    public void delete(Integer receiptId) throws SQLException {

    }

    @Override
    public void update(Integer receiptId, ReceiptWithItem receiptWithItem) throws SQLException {

    }

}
