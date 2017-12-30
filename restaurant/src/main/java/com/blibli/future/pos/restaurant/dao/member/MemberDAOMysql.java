package com.blibli.future.pos.restaurant.dao.member;


import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.dao.MysqlDAO;
import com.blibli.future.pos.restaurant.common.model.Member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MemberDAOMysql extends MysqlDAO<Member> implements MemberDAO {
    private Member member;
    private List<Member> members;

    @Override
    protected void mappingObject(Member member, ResultSet rs) throws SQLException {
        member.setId(rs.getInt("id"));
        member.setName(rs.getString("name"));
        member.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        member.setAddress(rs.getString("address"));
        member.setEmail(rs.getString("email"));
    }

    @Override
    public void create(Member member) throws SQLException {
        String query = "INSERT INTO members(name, address, email)" +
                " VALUES(?, ?, ?)";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setString(1, member.getName());
        ps.setString(2, member.getAddress());
        ps.setString(3, member.getEmail());

        int affected = ps.executeUpdate();
        if (affected > 0) {
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                member.setId(rs.getInt(1));
            }
        } else {
            throw new SQLException("No affected query. No member inserting");
        }
    }

    @Override
    public Member findById(int id) throws SQLException {
        member = new Member();

        members = find("id = "+ id);
        if(members.size()>0){
            member = members.get(0);
        }
        return member;
    }

    @Override
    public List<Member> find(String filter) throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members WHERE "+filter;
        ps = TransactionHelper.getConnection().prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Member member = new Member();
            mappingObject(member, rs);
            members.add(member);
        }
        return members;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM members WHERE id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setInt(1, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No member deleted");
        }
    }

    @Override
    public void update(int id, Member member) throws SQLException {
        String query = "UPDATE members SET " +
                "name = ?, " +
                "address = ?," +
                "email = ? " +
                "WHERE id = ?";
        ps = TransactionHelper.getConnection().prepareStatement(query);
        ps.setString(1, member.getName());
        ps.setString(2, member.getAddress());
        ps.setString(3, member.getEmail());
        ps.setInt(4, id);

        int affected = ps.executeUpdate();
        if (affected <= 0) {
            throw new SQLException("No affected query. No member update");
        }
    }
}
