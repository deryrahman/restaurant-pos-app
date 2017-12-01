package com.blibli.future.pos.restaurant.receipt;


import com.blibli.future.pos.restaurant.common.MysqlDAO;
import com.blibli.future.pos.restaurant.common.model.Receipt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDAOMysql extends MysqlDAO implements ReceiptDAO{

    private void setReceiptFromQuery(Receipt receipt, ResultSet rs) throws SQLException {
        receipt.setId(rs.getInt("id"));
        receipt.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        receipt.setRestaurantId(rs.getInt("restaurant_id"));
        receipt.setUserId(rs.getInt("user_id"));
        receipt.setMemberId(rs.getInt("member_id"));
        receipt.setTotalPrice(rs.getBigDecimal("total_price"));
        receipt.setNote(rs.getString("note"));
        receipt.autoSetHref();
    }

    @Override
    public int create(Receipt receipt) {
        if (!open()) {
            return 0;
        }
        try {
            String query = "INSERT INTO receipts(restaurant_id, user_id, member_id, total_price, note)" +
                    " VALUES(?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            ps.setInt(1, receipt.getRestaurantId());
            ps.setInt(2, receipt.getUserId());
            ps.setInt(3, receipt.getMemberId());
            ps.setBigDecimal(4, receipt.getTotalPrice());
            ps.setString(5, receipt.getNote());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    return rs.getInt(1);
                }
            } else {
                message.setMessage("No affected query. No receipt inserting");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on create receipt");
            e.printStackTrace();
        } finally {
            close();
        }
        return 0;
    }

    @Override
    public Receipt getById(int id) {
        Receipt receipt = new Receipt();
        if (!open()) {
            return receipt;
        }
        try {
            String query = "SELECT * FROM receipts WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            rs.next();
            setReceiptFromQuery(receipt, rs);
            return receipt;
        } catch (SQLException e) {
            message.setMessage("Something wrong on get receipt");
            e.printStackTrace();
        } finally {
            close();
        }
        return receipt;
    }

    @Override
    public List<Receipt> getBulk(String filter) {
        List<Receipt> receipts = new ArrayList<>();
        if (!open()) {
            return receipts;
        }
        try {
            String query = "SELECT * FROM receipts WHERE "+filter;
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Receipt receipt = new Receipt();
                setReceiptFromQuery(receipt, rs);
                receipts.add(receipt);
            }
            return receipts;
        } catch (SQLException e) {
            message.setMessage("Something wrong on getBulk receipts");
            e.printStackTrace();
        } finally {
            close();
        }
        return receipts;
    }

    @Override
    public boolean delete(int id) {
        if (!open()) {
            return false;
        }
        try {
            String query = "DELETE FROM receipts WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No receipt deleted");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on delete receipt");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    @Override
    public boolean update(int id, Receipt receipt) {
        if (!open()) {
            return false;
        }
        try {
            String query = "UPDATE receipts SET " +
                    "restaurant_id = ?, " +
                    "user_id = ?," +
                    "member_id = ?," +
                    "total_price = ?," +
                    "note = ? +" +
                    "WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, receipt.getRestaurantId());
            ps.setInt(2, receipt.getUserId());
            ps.setInt(3, receipt.getMemberId());
            ps.setBigDecimal(4, receipt.getTotalPrice());
            ps.setString(5, receipt.getNote());
            ps.setInt(6, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No receipt update");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on update receipt");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
}
