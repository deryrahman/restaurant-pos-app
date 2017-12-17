package com.blibli.future.pos.restaurant.service;


import com.blibli.future.pos.restaurant.common.TransactionUtility;
import com.blibli.future.pos.restaurant.common.model.*;
import com.blibli.future.pos.restaurant.dao.item.ItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.restaurant.RestaurantDAOMysql;
import com.blibli.future.pos.restaurant.dao.restaurant.RestaurantItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.user.UserDAOMysql;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Path("/restaurants")
public class RestaurantService {
    private static RestaurantDAOMysql restaurantDAO = new RestaurantDAOMysql();
    private static ItemDAOMysql itemDAO = new ItemDAOMysql();
    private static RestaurantItemDAOMysql restaurantItemDAO = new RestaurantItemDAOMysql();
    private static UserDAOMysql userDAO = new UserDAOMysql();
    private static Gson gson = new Gson();
    private static TransactionUtility tx;

    // ---- BEGIN /restaurants ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(Restaurant restaurant) throws Exception {
        tx.beginTransaction();
        restaurantDAO.create(restaurant);
        tx.commitTransaction();
        return Response.status(201).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {
        tx.beginTransaction();
        List<Restaurant> restaurants = restaurantDAO.getBulk("true");
        tx.commitTransaction();
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
    public Response delete() throws Exception {
        throw new Exception("Method not allowed");
    }

    @PUT
    @Produces("application/json")
    public Response update() throws Exception {
        throw new Exception("Method not allowed");
    }
    // ---- END /restaurants ----

    // ---- BEGIN /restaurants/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id) throws Exception {
        tx.beginTransaction();
        Restaurant restaurant = restaurantDAO.getById(id);
        tx.commitTransaction();
        if(restaurant == null){
            throw new Exception("Not found");
        }
        String json = gson.toJson(restaurant);
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
        restaurantDAO.delete(id);
        return Response.status(204).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, Restaurant restaurant) throws Exception {
        Gson gson = new Gson();
        gson.toJson(restaurant);
        restaurantDAO.update(id,restaurant);
        return Response.status(204).build();
    }
    // ---- END /restaurants/{id} ----

    // ---- NESTED ----

    /**
     * special purpose of nested resources. Like /restaurants/1/items. It will call /items, on itemsResources and its stock for specific restaurant
     */
    @GET
    @Path("/{restaurantId}/items")
    @Produces("application/json")
    public Response getAllItem(@PathParam("restaurantId") int restaurantId) throws Exception {
        tx.beginTransaction();
        List<Item> items = itemDAO.getAllItemByRestaurantId(restaurantId,"true");
        tx.commitTransaction();
        Map<String, Object> map = new HashMap<>();
        Metadata metadata = new Metadata();
        metadata.setCount(items.size());
        metadata.setLimit(items.size());

        map.put("metadata", metadata);
        map.put("results", items);

        String json = gson.toJson(map);
        return Response.status(200).entity(json).build();
    }

    /**
     * Add com.blibli.future.pos.restaurant.item to restaurant. Item id must be guaranteed exist on items table
     */
    @POST
    @Path("/{restaurantId}/items")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addItem(Item item, @PathParam("restaurantId") int restaurantId) throws Exception {
        tx.beginTransaction();
        if(itemDAO.getBulk("name = " + item.getName()).size() == 0){
            itemDAO.create(item);
        }
        restaurantDAO.addRelationRestaurantItem(item.getId(), restaurantId, 0);
        tx.commitTransaction();
        return Response.status(201).build();
    }

    /**
     * special purpose of nested resources. Like /restaurants/1/users. It will call /users, on userResource
     */
    @GET
    @Path("/{restaurantId}/users")
    @Produces("application/json")
    public Response getAllUser(@PathParam("restaurantId") int restaurantId) throws Exception {
        tx.beginTransaction();
        List<User> users = userDAO.getBulk("restaurant_id="+restaurantId);
        tx.commitTransaction();

        Map<String, Object> map = new HashMap<>();
        Metadata metadata = new Metadata();
        metadata.setCount(users.size());
        metadata.setLimit(users.size());

        map.put("metadata", metadata);
        map.put("results", users);

        String json = gson.toJson(map);
        return Response.status(200).entity(json).build();
    }

    // ---- END NESTED ----
}
