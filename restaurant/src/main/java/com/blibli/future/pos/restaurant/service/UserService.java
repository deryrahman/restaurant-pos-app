package com.blibli.future.pos.restaurant.service;


import com.blibli.future.pos.restaurant.common.TransactionUtility;
import com.blibli.future.pos.restaurant.common.model.Metadata;
import com.blibli.future.pos.restaurant.common.model.User;
import com.blibli.future.pos.restaurant.dao.user.UserDAOMysql;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Path("/users")
public class UserService {
    private static UserDAOMysql userDAO = new UserDAOMysql();
    private Gson gson = new Gson();

    // ---- BEGIN /users ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(User user) throws Exception {
        TransactionUtility.beginTransaction();
        userDAO.create(user);
        TransactionUtility.commitTransaction();
        return Response.status(201).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {
        TransactionUtility.beginTransaction();
        List<User> users = userDAO.find("true");
        TransactionUtility.commitTransaction();

        Map<String, Object> map = new HashMap<>();
        Metadata metadata = new Metadata();
        metadata.setCount(users.size());
        metadata.setLimit(users.size());

        map.put("metadata", metadata);
        map.put("results", users);

        String json = gson.toJson(map);
        return Response.status(200).entity(json).build();
    }

    @DELETE
    @Produces("application/json")
    public Response delete() throws Exception {
        throw new NotAllowedException("DELETE method not allowed");
    }

    @PUT
    @Produces("application/json")
    public Response update() throws Exception {
        throw new NotAllowedException("PUT method not allowed");
    }
    // ---- END /users ----

    // ---- BEGIN /users/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id) throws Exception {
        TransactionUtility.beginTransaction();
        User user = userDAO.findById(id);
        TransactionUtility.commitTransaction();
        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }

        String json = gson.toJson(user);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/{id}")
    @Produces("application/json")
    public Response create(@PathParam("id") int id) throws Exception {
        throw new Exception("Method not allowed");
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) throws Exception {
        TransactionUtility.beginTransaction();
        userDAO.delete(id);
        TransactionUtility.commitTransaction();
        return Response.status(200).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, User user) throws Exception {
        TransactionUtility.beginTransaction();
        userDAO.update(id,user);
        TransactionUtility.commitTransaction();
        return Response.status(200).build();
    }
    // ---- END /users/{id} ----
}
