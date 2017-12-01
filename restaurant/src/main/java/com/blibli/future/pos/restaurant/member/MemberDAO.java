package com.blibli.future.pos.restaurant.member;

import com.blibli.future.pos.restaurant.common.model.Member;

import java.util.List;

public interface MemberDAO {

    /**
     * Create member
     * @param member : only one member will be created
     * @return true if member is successed to build, false otherwise
     */
    public boolean create(Member member);

    /**
     * Get member by id
     * @param id : integer parameter, id must be valid
     * @return member object
     */
    public Member getById(int id);

    /**
     * Get all member with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all members, just set filter to "true"
     * @return list of filtered members
     */
    public List<Member> getBulk(String filter);

    /**
     * Delete member
     * @param id integer : only one member will be deleted. Member must be valid
     * @return true if success to deleted, false otherwise
     */
    public boolean delete(int id);

    /**
     * Update member
     * @param id, member: only one member will be updated. Member must be valid
     * @return true if success to update, false otherwise
     */
    public boolean update(int id, Member member);
}
