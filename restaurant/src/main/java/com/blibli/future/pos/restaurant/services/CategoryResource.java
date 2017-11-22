package com.blibli.future.pos.restaurant.services;

import com.blibli.future.pos.restaurant.Metadata;
import com.blibli.future.pos.restaurant.dao.CategoryDAOMysql;
import com.blibli.future.pos.restaurant.model.Category;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/categories")
public class CategoryResource {
    private CategoryDAOMysql categoryDAO;

    public CategoryResource(){
        categoryDAO = new CategoryDAOMysql();
    }

    private Response get405Response(){
        Gson gson = new Gson();
        Message msg = new Message();

        msg.setMsg("Method not allowed");
        String json = gson.toJson(msg);
        return Response.status(405).entity(json).build();
    }

    // ---- BEGIN /categories ----
    @POST
    @Consumes("application/json")
    public Response createCategory(Category category){
        if(categoryDAO.create(category)) {
            return Response.status(201).build();
        }
        return Response.status(400).build();
    }

    @GET
    @Produces("application/json")
    public Response getAllCategory(){
        Gson gson = new Gson();
        List<Category> categories = categoryDAO.getAll();

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
    public Response put(){
        return get405Response();
    }
    // ---- END /categories ----

    // ---- BEGIN /categories/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getCategory(@PathParam("id") int id){
        Gson gson = new Gson();
        Category category = categoryDAO.getById(id);

        String json = gson.toJson(category);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/{id}")
    public Response postCategory(@PathParam("id") int id){
        return get405Response();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") int id){
        if (categoryDAO.delete(id)) {
            return Response.status(204).build();
        }
        return Response.status(500).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response editCategory(@PathParam("id") int id, Category category){
        Gson gson = new Gson();
        gson.toJson(category);
        if(categoryDAO.update(id,category)){
            return Response.status(204).build();
        }
        return Response.status(500).build();
    }
    // ---- END /categories/{id} ----
}
