package com.blibli.future.pos.api.v1;

import com.blibli.future.pos.Item;
import com.blibli.future.pos.MetaData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("items")
public class ItemService {
    private ArrayList<Item> items = new ArrayList<Item>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getAllItems(){
        // Insert map
        Map map = new HashMap();
        // Insert map -> metadata
        map.put("metadata", new MetaData(Long.valueOf(40),Integer.valueOf(10)));
        // Insert map -> results
        for (int i=1; i<=1000; i++){
            items.add(new Item(
                    Long.valueOf(i),
                    "Title "+String.valueOf(i),
                    "50000")
            );
        }
        map.put("results",items);

        return map;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Item getItem(@PathParam("id") Long id){
        Item item = new Item(id);
        return item;
    }

//    @POST
//    @Path("/post")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response createTrackInJSON(Item item) {
//
//        String result = "Item saved : " + item;
//        return Response.status(201).entity(result).build();
//
//    }
}
