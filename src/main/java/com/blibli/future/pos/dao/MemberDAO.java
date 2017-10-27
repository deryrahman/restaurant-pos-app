package com.blibli.future.pos.dao;

import com.blibli.future.pos.entity.Member;

import java.util.List;

public interface MemberDAO {
    public List<Member> getAllMembers();
    public Member getMember(Long id);
    public boolean createMember(Member member);
    public void updateMember(Member member);
    public void deleteItemById(Long id);
}
