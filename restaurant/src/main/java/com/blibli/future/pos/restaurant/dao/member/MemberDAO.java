package com.blibli.future.pos.restaurant.dao.member;

import com.blibli.future.pos.restaurant.common.model.Member;

import java.sql.SQLException;
import java.util.List;

public interface MemberDAO {

    /**
     * Create member
     * @param member : only one member will be created
     */
    public void create(Member member) throws SQLException;

    /**
     * Get member by id
     * @param id : integer parameter, id must be valid
     * @return member object
     */
    public Member findById(int id) throws SQLException;

    /**
     * Get all member with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all members, just set filter to "true"
     * @return list of filtered members
     */
    public List<Member> find(String filter) throws SQLException;

    /**
     * Delete member
     * @param id integer : only one member will be deleted. Member must be valid
     */
    public void delete(int id) throws SQLException;

    /**
     * Update member
     * @param id, member: only one member will be updated. Member must be valid
     */
    public void update(int id, Member member) throws SQLException;
}
