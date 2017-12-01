package com.blibli.future.pos.restaurant.user;

import com.blibli.future.pos.restaurant.common.model.User;

import java.util.List;

public interface UserDAO {
    /**
     * Create user
     * @param user : only one user will be created
     * @return true if category is successed to build, false otherwise
     */
    public boolean create(User user);

    /**
     * Get user by id
     * @param id : integer parameter, id must be valid
     * @return User object
     */
    public User getById(int id);

    /**
     * Get all user with specific limitation
     * @param filter : is a WHERE statemtment of mysql query
     *               To get all users, just set filter to "true"
     * @return list of filtered users
     */
    public List<User> getBulk(String filter);

    /**
     * Delete user
     * @param id integer : only one user will be deleted. User must be valid
     * @return true if success to deleted, false otherwise
     */
    public boolean delete(int id);

    /**
     * Update user
     * @param id, user: only one user will be updated. User must be valid
     * @return true if success to update, false otherwise
     */
    public boolean update(int id, User user);
}
