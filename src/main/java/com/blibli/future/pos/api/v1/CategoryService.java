package com.blibli.future.pos.api.v1;

import com.blibli.future.pos.dao_impl.CategoryDAOImpl;
import com.blibli.future.pos.entity.Category;
import com.blibli.future.pos.util.MetaData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dery on 10/27/17.
 */
@Path("/categories")
public class CategoryService {
    private CategoryDAOImpl categoryDAOImpl;

    public CategoryService() {
        categoryDAOImpl = new CategoryDAOImpl();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getAllItems(){
        // Insert map
        Map map = new HashMap();
        // Insert map -> metadata
        Long size = Long.valueOf(categoryDAOImpl.getAllCategories().size());
        map.put("metadata", new MetaData(size,Integer.valueOf(10)));
        map.put("results",categoryDAOImpl.getAllCategories());

        return map;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Category getItem(@PathParam("id") Long id){
        return categoryDAOImpl.getCategory(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCategoryInJSON(Category category) {
        if(categoryDAOImpl.createCategory(category)) {
            String result = "Item saved : \n" + category;
            return Response.status(201).entity(result).build();
        }
        return Response.status(409).build();
    }

    @Path("{id}")
    public ItemService getItemService(@PathParam("id") Long categoryId){
        return new ItemService(categoryId);
    }
}
