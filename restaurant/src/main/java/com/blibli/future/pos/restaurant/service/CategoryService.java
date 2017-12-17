package com.blibli.future.pos.restaurant.service;

import com.blibli.future.pos.restaurant.common.model.Metadata;
import com.blibli.future.pos.restaurant.common.model.Category;
import com.blibli.future.pos.restaurant.dao.category.CategoryDAOMysql;
import com.blibli.future.pos.restaurant.service.ItemService;
import com.blibli.future.pos.restaurant.common.model.Message;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/categories")
public class CategoryService {
    private CategoryDAOMysql categoryDAO = new CategoryDAOMysql();
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

    // ---- BEGIN /categories ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(Category category) throws Exception {
        categoryDAO.create(category);
        return Response.status(201).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {
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
    public Response delete() throws Exception {
        throw new Exception("Method not allowed");
    }

    @PUT
    @Produces("application/json")
    public Response update() throws Exception {
        throw new Exception("Method not allowed");
    }
    // ---- END /categories ----

    // ---- BEGIN /categories/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id) throws Exception {
        Gson gson = new Gson();
        Category category = categoryDAO.getById(id);
        if(category == null){
            throw new Exception("Not found");
        }
        String json = gson.toJson(category);
        return Response.status(200).entity(json).build();
    }

    /**
     * special purpose of nested resources. Like /categories/1/items. It will call /items, on itemsResources
     */
//    @GET
//    @Path("/{categoryId}/items")
//    @Produces("application/json")
//    public Response getAllItem(@PathParam("categoryId") int categoryId) throws Exception {
//        ItemService itemService = new ItemService();
//        return itemService.getAll(categoryId);
//    }
    @GET
    @Path("/{categoryId}/items/{id}")
    @Produces("application/json")
    public Response getItem(@PathParam("categoryId") int categoryId,
                            @PathParam("id") int id) throws Exception {
        ItemService itemService = new ItemService();
        return itemService.get(id, categoryId);
    }

    @POST
    @Path("/{id}")
    @Produces("application/json")
    public Response create(@PathParam("id") int id){
        return get405Response();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) throws Exception {
        categoryDAO.delete(id);
        return Response.status(204).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, Category category) throws Exception {
        Gson gson = new Gson();
        gson.toJson(category);
        categoryDAO.update(id,category);
        return Response.status(204).build();
    }
    // ---- END /categories/{id} ----
}
