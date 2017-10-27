package com.blibli.future.pos.dao;


import com.blibli.future.pos.entity.User;

import java.util.Collection;
import java.util.List;

public interface UserDAO {
    public List<User> getAllUsers();
    public User getUser(Long id);
    public void updateUser(User item);
    public void deleteUser(User item);
}
