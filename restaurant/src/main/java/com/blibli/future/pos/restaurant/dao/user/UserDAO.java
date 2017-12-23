package com.blibli.future.pos.restaurant.dao.user;

import com.blibli.future.pos.restaurant.common.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    /**
     * Create user
     * @param user : only one user will be created
     */
    public void create(User user) throws SQLException;

    /**
     * Get user by id
     * @param id : integer parameter, id must be valid
     * @return User object
     */
    public User findById(int id) throws SQLException;

    /**
     * Get user by id
     * @param id : string parameter, id must be valid
     * @return User object
     */
    public User findById(String id) throws SQLException;

    /**
     * Get all user with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all users, just set filter to "true"
     * @return list of filtered users
     */
    public List<User> find(String filter) throws SQLException;

    /**
     * Delete user
     * @param id integer : only one user will be deleted. User must be valid
     */
    public void delete(int id) throws SQLException;

    /**
     * Update user
     * @param id, user: only one user will be updated. User must be valid
     */
    public void update(int id, User user) throws SQLException;
}
