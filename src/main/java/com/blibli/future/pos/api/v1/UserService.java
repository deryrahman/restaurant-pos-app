package com.blibli.future.pos.api.v1;

import com.blibli.future.pos.dao.UserDAO;
import com.blibli.future.pos.dao_impl.UserDAOImpl;
import com.blibli.future.pos.entity.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/users")
public class UserService {
    private UserDAO userDAO = new UserDAOImpl();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

}
