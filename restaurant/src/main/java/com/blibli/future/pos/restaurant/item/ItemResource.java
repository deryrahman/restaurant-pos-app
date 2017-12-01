package com.blibli.future.pos.restaurant.item;


import com.blibli.future.pos.restaurant.common.model.Metadata;
import com.blibli.future.pos.restaurant.common.model.Item;
import com.blibli.future.pos.restaurant.common.model.Message;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Path("/items")
public class ItemResource {
    private ItemDAOMysql itemDAO = new ItemDAOMysql();
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

    // ---- BEGIN /items ----

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(Item item){
        if(itemDAO.create(item)) {
            return Response.status(201).build();
        }
        String json = gson.toJson(itemDAO.getMessage());
        return Response.status(400).entity(json).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll(){
        Gson gson = new Gson();
        List<Item> items = itemDAO.getBulk("true");

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
     * Special purpose for nested category
     * @param categoryId
     */
    public Response getAll(int categoryId){
        Gson gson = new Gson();
        List<Item> items = itemDAO.getBulk("category_id=" + categoryId);

        Map<String, Object> map = new HashMap<>();
        Metadata metadata = new Metadata();
        metadata.setCount(items.size());
        metadata.setLimit(items.size());

        map.put("metadata", metadata);
        map.put("results", items);

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
    // ---- END /items ----


    // ---- BEGIN /items/{id} ----

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id){
        Gson gson = new Gson();
        Item item = itemDAO.getById(id);

        if(item == null){
            return get404Response();
        }

        String json = gson.toJson(item);
        return Response.status(200).entity(json).build();
    }

    public Response get(int id, int categoryId){
        Gson gson = new Gson();
        Item item = itemDAO.getById(id);
        if(item.getCategoryId()!=categoryId || item == null){
            return get404Response();
        }
        String json = gson.toJson(item);
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
        if (itemDAO.delete(id)) {
            return Response.status(204).build();
        }
        return Response.status(500).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, Item item){
        Gson gson = new Gson();
        gson.toJson(item);
        if(itemDAO.update(id,item)){
            return Response.status(204).build();
        }
        return Response.status(500).build();
    }

    // ---- END /items/{id} ----
}
