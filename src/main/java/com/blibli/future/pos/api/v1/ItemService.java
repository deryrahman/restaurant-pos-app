package com.blibli.future.pos.api.v1;

import com.blibli.future.pos.dao_impl.ItemDAOImpl;
import com.blibli.future.pos.entity.Item;
import com.blibli.future.pos.util.MetaData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/items")
public class ItemService {
    private ItemDAOImpl itemDAOImpl;

    public ItemService(Long categoryId) {
        itemDAOImpl = new ItemDAOImpl(categoryId);
    }

    public ItemService(){
        itemDAOImpl = new ItemDAOImpl(Long.valueOf(-1));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getAllItems(){
        // Insert map
        Map map = new HashMap();
        // Insert map -> metadata
        Long size = Long.valueOf(itemDAOImpl.getAllItems().size());
        map.put("metadata", new MetaData(size,Integer.valueOf(10)));
        map.put("results",itemDAOImpl.getAllItems());

        return map;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Item getItem(@PathParam("id") Long id){
        return itemDAOImpl.getItem(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createItemInJSON(Item item) {
        if(itemDAOImpl.createItem(item)) {
            String result = "Item saved : \n" + item;
            return Response.status(201).entity(result).build();
        }
        return Response.status(409).build();
    }
}
