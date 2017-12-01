package com.blibli.future.pos.restaurant.member;


import com.blibli.future.pos.restaurant.common.MysqlDAO;
import com.blibli.future.pos.restaurant.common.model.Member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MemberDAOMysql extends MysqlDAO implements MemberDAO {

    private void setItemFromQuery(Member member, ResultSet rs) throws SQLException {
        member.setId(rs.getInt("id"));
        member.setName(rs.getString("name"));
        member.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        member.setAddress(rs.getString("address"));
        member.setEmail(rs.getString("email"));
        member.autoSetHref();
    }

    @Override
    public boolean create(Member member) {
        if (!open()) {
            return false;
        }
        try {
            String query = "INSERT INTO members(name, address, email)" +
                    " VALUES(?, ?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, member.getName());
            ps.setString(2, member.getAddress());
            ps.setString(3, member.getEmail());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No member inserting");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on create member");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    @Override
    public Member getById(int id) {
        Member member = new Member();
        if (!open()) {
            return member;
        }
        try {
            String query = "SELECT * FROM members WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            rs.next();
            setItemFromQuery(member, rs);
            return member;
        } catch (SQLException e) {
            message.setMessage("Something wrong on get member");
            e.printStackTrace();
        } finally {
            close();
        }
        return member;
    }

    @Override
    public List<Member> getBulk(String filter) {
        List<Member> members = new ArrayList<>();
        if (!open()) {
            return members;
        }
        try {
            String query = "SELECT * FROM items WHERE "+filter;
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Member member = new Member();
                setItemFromQuery(member, rs);
                members.add(member);
            }
            return members;
        } catch (SQLException e) {
            message.setMessage("Something wrong on getBulk members");
            e.printStackTrace();
        } finally {
            close();
        }
        return members;
    }

    @Override
    public boolean delete(int id) {
        if (!open()) {
            return false;
        }
        try {
            String query = "DELETE FROM members WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No member deleted");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on delete member");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    @Override
    public boolean update(int id, Member member) {
        if (!open()) {
            return false;
        }
        try {
            String query = "UPDATE members SET " +
                    "name = ?, " +
                    "address = ?," +
                    "email = ?," +
                    "WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, member.getName());
            ps.setString(2, member.getAddress());
            ps.setString(3, member.getEmail());
            ps.setInt(4, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No member update");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on update member");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
}
