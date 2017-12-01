package com.blibli.future.pos.restaurant.user;


import com.blibli.future.pos.restaurant.common.model.Message;
import com.blibli.future.pos.restaurant.common.model.Metadata;
import com.blibli.future.pos.restaurant.common.model.User;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Path("/users")
public class UserResource {
    private UserDAOMysql userDAO = new UserDAOMysql();
    private Gson gson = new Gson();
    private Message msg = new Message();

    private Response get405Response(){
        msg.setMessage("Method not allowed");
        String json = gson.toJson(msg);
        return Response.status(405).entity(json).build();
    }

    private Response get404Response(){
        msg.setMessage("Not found");
        String json = gson.toJson(msg);
        return Response.status(404).entity(json).build();
    }

    // ---- BEGIN /users ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(User user){
        if(userDAO.create(user)) {
            return Response.status(201).build();
        }
        String json = gson.toJson(userDAO.getMessage());
        return Response.status(400).entity(json).build();
    }
    @GET
    @Produces("application/json")
    public Response getAll(){
        Gson gson = new Gson();
        List<User> users = userDAO.getBulk("true");

        Map<String, Object> map = new HashMap<>();
        Metadata metadata = new Metadata();
        metadata.setCount(users.size());
        metadata.setLimit(users.size());

        map.put("metadata", metadata);
        map.put("results", users);

        String json = gson.toJson(map);
        return Response.status(200).entity(json).build();
    }

    /**
     * Special purpose for nested restaurant
     * @param restaurantId
     */
    public Response getAll(int restaurantId){
        Gson gson = new Gson();
        List<User> users = userDAO.getBulk("restaurant_id="+restaurantId);

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
    public Response delete(){
        return get405Response();
    }

    @PUT
    @Produces("application/json")
    public Response update(){
        return get405Response();
    }
    // ---- END /users ----

    // ---- BEGIN /users/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id){
        Gson gson = new Gson();
        User user = userDAO.getById(id);

        if(user == null){
            return get404Response();
        }

        String json = gson.toJson(user);
        return Response.status(200).entity(json).build();
    }
    public Response get(int id, int restaurantId){
        Gson gson = new Gson();
        User user = userDAO.getById(id);

        if(user.getId()!=id || user == null){
            return get404Response();
        }

        String json = gson.toJson(user);
        return Response.status(200).entity(json).build();
    }
    @POST
    @Path("/{id}")
    @Produces("application/json")
    public Response create(@PathParam("id") int id){
        return get405Response();
    }
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id){
        if (userDAO.delete(id)) {
            return Response.status(204).build();
        }
        return Response.status(500).build();
    }
    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, User user){
        Gson gson = new Gson();
        gson.toJson(user);
        if(userDAO.update(id,user)){
            return Response.status(204).build();
        }
        return Response.status(500).build();
    }
    // ---- END /users/{id} ----
}
