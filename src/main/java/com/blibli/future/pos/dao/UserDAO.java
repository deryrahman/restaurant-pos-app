package com.blibli.future.pos.dao;


import com.blibli.future.pos.entity.User;

import java.util.List;

public interface UserDAO {
    public List<User> getAllUsers();
    public User getUser(Long id);
    public boolean createUser(User user);
    public void updateUser(User user);
    public void deleteUserById(Long id);
}
