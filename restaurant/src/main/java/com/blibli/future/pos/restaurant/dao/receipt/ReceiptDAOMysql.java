package com.blibli.future.pos.restaurant.dao.receipt;


import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.dao.MysqlDAO;
import com.blibli.future.pos.restaurant.common.model.Receipt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ReceiptDAOMysql extends MysqlDAO<Receipt> implements ReceiptDAO{
    private List<Receipt> receipts;
    private Receipt receipt;

    @Override
    protected void mappingObject(Receipt receipt, ResultSet rs) throws SQLException {
        receipt.setId(rs.getInt("id"));
        receipt.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        receipt.setRestaurantId(rs.getInt("restaurant_id"));
        receipt.setUserId(rs.getInt("user_id"));
        receipt.setMemberId(rs.getInt("member_id"));
        receipt.setTotalPrice(rs.getBigDecimal("total_price"));
        receipt.setNote(rs.getString("note"));
        receipt.setTax(rs.getBigDecimal("tax"));
    }

    @Override
    public void create(Receipt receipt) throws SQLException {
            String query = "INSERT INTO receipts(restaurant_id, user_id, member_id, total_price, note, tax)" +
                    " VALUES(?, ?, ?, ?, ?, ?)";
            ps = TransactionHelper.getConnection().prepareStatement(query);
            ps.setInt(1, receipt.getRestaurantId());
            ps.setInt(2, receipt.getUserId());
            if(receipt.getMemberId() == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, receipt.getMemberId());
            }
            ps.setBigDecimal(4, receipt.getTotalPrice());
            if(receipt.getNote() == null){
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setString(5, receipt.getNote());
            }

            if(receipt.getTax() == null){
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setBigDecimal(6, receipt.getTax());
            }

            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    receipt.setId(rs.getInt(1));
                }
            } else{
                throw new SQLException("No affected query. No receipt inserting");
            }
    }

    @Override
    public Receipt findById(Integer id) throws SQLException {
        receipt = new Receipt();
        receipts = find("id = "+id);
        if(receipts.size()>0){
            receipt = receipts.get(0);
        }
        return receipt;
    }

    @Override
    public List<Receipt> find(String filter) throws SQLException {
        List<Receipt> receipts = new ArrayList<>();
        String query = "SELECT * FROM receipts WHERE "+filter;
        ps = TransactionHelper.getConnection().prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Receipt receipt = new Receipt();
            mappingObject(receipt, rs);
            receipts.add(receipt);
        }
        return receipts;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM receipts WHERE id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setInt(1, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No receipt deleted");
        }
    }

    @Override
    public void update(int id, Receipt receipt) throws SQLException {
        String query = "UPDATE receipts SET " +
                "restaurant_id = ?, " +
                "user_id = ?," +
                "member_id = ?," +
                "total_price = ?," +
                "note = ? +" +
                "WHERE id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setInt(1, receipt.getRestaurantId());
        ps.setInt(2, receipt.getUserId());
        ps.setInt(3, receipt.getMemberId());
        ps.setBigDecimal(4, receipt.getTotalPrice());
        ps.setString(5, receipt.getNote());
        ps.setInt(6, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No receipt update");
        }
    }
}
