package com.blibli.future.pos.restaurant.services;

import com.blibli.future.pos.restaurant.Metadata;
import com.blibli.future.pos.restaurant.dao.category.CategoryDAOMysql;
import com.blibli.future.pos.restaurant.model.Category;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/categories")
public class CategoryResource {
    private CategoryDAOMysql categoryDAO = new CategoryDAOMysql();;
    private Gson gson = new Gson();
    private Message msg = new Message();

    private Response get405Response(){
        msg.setMessage("Method not allowed");
        String json = gson.toJson(msg);
        return Response.status(405).entity(json).build();
    }

    // ---- BEGIN /categories ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(Category category){
        if(categoryDAO.create(category)) {
            return Response.status(201).build();
        }
        String json = gson.toJson(categoryDAO.getMessage());
        return Response.status(400).entity(json).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll(){
        Gson gson = new Gson();
        List<Category> categories = categoryDAO.getBulk("true");

        Map<String, Object> map = new HashMap<>();
        Metadata metadata = new Metadata();
        metadata.setCount(categories.size());
        metadata.setLimit(categories.size());

        map.put("metadata", metadata);
        map.put("results", categories);

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
    // ---- END /categories ----

    // ---- BEGIN /categories/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id){
        Gson gson = new Gson();
        Category category = categoryDAO.getById(id);

        String json = gson.toJson(category);
        return Response.status(200).entity(json).build();
    }

    /**
     * special purpose of nested resources. Like /categories/1/items. It will call /items, on itemsResources
     */
    @GET
    @Path("/{categoryId}/items")
    @Produces("application/json")
    public Response getAllItem(@PathParam("categoryId") int categoryId){
        ItemResource itemResource = new ItemResource();
        return itemResource.getAllByCategoryId(categoryId);
    }
    @GET
    @Path("/{categoryId}/items/{id}")
    @Produces("application/json")
    public Response getItem(@PathParam("categoryId") int categoryId,
                            @PathParam("id") int id){
        ItemResource itemResource = new ItemResource();
        return itemResource.get(id, categoryId);
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
        if (categoryDAO.delete(id)) {
            return Response.status(204).build();
        }
        return Response.status(500).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, Category category){
        Gson gson = new Gson();
        gson.toJson(category);
        if(categoryDAO.update(id,category)){
            return Response.status(204).build();
        }
        return Response.status(500).build();
    }
    // ---- END /categories/{id} ----
}
