package com.blibli.future.pos.restaurant.services;


import com.blibli.future.pos.restaurant.Metadata;
import com.blibli.future.pos.restaurant.dao.restaurant.RestaurantDAOMysql;
import com.blibli.future.pos.restaurant.model.Restaurant;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/restaurants")
public class RestaurantResource {
    private RestaurantDAOMysql restaurantDAO = new RestaurantDAOMysql();
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

    // ---- BEGIN /restaurants ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(Restaurant restaurant){
        if(restaurantDAO.create(restaurant)) {
            return Response.status(201).build();
        }
        String json = gson.toJson(restaurantDAO.getMessage());
        return Response.status(400).entity(json).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll(){
        Gson gson = new Gson();
        List<Restaurant> restaurants = restaurantDAO.getBulk("true");

        Map<String, Object> map = new HashMap<>();
        Metadata metadata = new Metadata();
        metadata.setCount(restaurants.size());
        metadata.setLimit(restaurants.size());

        map.put("metadata", metadata);
        map.put("results", restaurants);

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
    // ---- END /restaurants ----

    // ---- BEGIN /restaurants/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id){
        Gson gson = new Gson();
        Restaurant restaurant = restaurantDAO.getById(id);
        if(restaurant == null){
            return get404Response();
        }
        String json = gson.toJson(restaurant);
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
        if (restaurantDAO.delete(id)) {
            return Response.status(204).build();
        }
        return Response.status(500).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, Restaurant restaurant){
        Gson gson = new Gson();
        gson.toJson(restaurant);
        if(restaurantDAO.update(id,restaurant)){
            return Response.status(204).build();
        }
        return Response.status(500).build();
    }
    // ---- END /restaurants/{id} ----
}
