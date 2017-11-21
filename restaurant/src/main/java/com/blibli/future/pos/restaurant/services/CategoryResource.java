package com.blibli.future.pos.restaurant.services;

import com.blibli.future.pos.restaurant.dao.CategoryDAO;
import com.blibli.future.pos.restaurant.dao.CategoryDAOMysql;
import com.blibli.future.pos.restaurant.model.Category;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("/categories")
public class CategoryResource {
    CategoryDAOMysql categoryDAO;

    public CategoryResource(){
        categoryDAO = new CategoryDAOMysql();
    }

    @POST
    @Consumes("application/json")
    public Response createCategory(Category category){
        categoryDAO.create(category);
        return Response.status(201).build();
    }

    @GET
    @Produces("application/json")
    public Response getAllCategory(){
        Gson gson = new Gson();
        String json = gson.toJson(new HashMap<>());
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    public Category getCategory(@PathParam("id") int id){
        return new Category();
    }
}
