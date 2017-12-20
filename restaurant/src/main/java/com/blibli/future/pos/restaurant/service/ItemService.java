package com.blibli.future.pos.restaurant.service;


import com.blibli.future.pos.restaurant.common.model.*;
import com.blibli.future.pos.restaurant.common.model.custom.ItemWithStock;
import com.blibli.future.pos.restaurant.dao.item.ItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.itemwithstock.ItemWithStockDAOMysql;
import com.blibli.future.pos.restaurant.dao.restaurant.RestaurantDAOMysql;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("ALL")
@Path("/items")
public class ItemService extends BaseRESTService{
    private ItemDAOMysql itemDAO = new ItemDAOMysql();
    private RestaurantDAOMysql restaurantDAO = new RestaurantDAOMysql();
    private ItemWithStockDAOMysql itemWithStockDAO = new ItemWithStockDAOMysql();

    // ---- BEGIN /items ----

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(Item item) throws Exception {
        if(item.notValidAttribute()){
            throw new BadRequestException(ErrorMessage.requiredValue(item));
        }
        tx.init();
        itemDAO.create(item);
        tx.commit();
        return Response.status(201).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {
        tx.init();
        List<Item> items = itemDAO.find("true");
        tx.commit();

        if(items.size()==0){
            throw new NotFoundException("Item not found");
        }

        baseResponse = new BaseResponse(true,200,items);

        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

//    /**
//     * Special purpose for nested com.blibli.future.pos.restaurant.category
//     * @param categoryId
//     */
//    public Response getAll(int categoryId) throws Exception{
//        Gson gson = new Gson();
//        List<Item> items = itemDAO.find("category_id=" + categoryId);
//
//        Map<String, Object> map = new HashMap<>();
//        Metadata metadata = new Metadata();
//        metadata.setCount(items.size());
//        metadata.setLimit(items.size());
//
//        map.put("metadata", metadata);
//        map.put("results", items);
//
//        String json = gson.toJson(map);
//        return Response.status(200).entity(json).build();
//    }

    @DELETE
    @Produces("application/json")
    public Response delete() throws Exception {
        throw new NotAllowedException(ErrorMessage.DELETE_NOT_ALLOWED, Response.status(405).build());
    }

    @PUT
    @Produces("application/json")
    public Response update() throws Exception {
        throw new NotAllowedException(ErrorMessage.PUT_NOT_ALLOWED, Response.status(405).build());
    }
    // ---- END /items ----


    // ---- BEGIN /items/{id} ----

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id) throws Exception {
        tx.init();
        Item item = itemDAO.findById(id);
        tx.commit();
        if(item.isEmpty()){
            throw new NotFoundException(ErrorMessage.NotFoundFrom(item));
        }

        json = objectMapper.writeValueAsString(item);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/{id}")
    @Produces("application/json")
    public Response create(@PathParam("id") int id) throws Exception {
        throw new NotAllowedException(ErrorMessage.POST_NOT_ALLOWED, Response.status(405).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) throws Exception {
        tx.init();
        if(itemDAO.findById(id).isEmpty()){
            tx.commit();
            throw new NotFoundException(ErrorMessage.NotFoundFrom(new Item()));
        }
        itemDAO.delete(id);
        tx.commit();
        return Response.status(200).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, Item item) throws Exception {
        tx.init();
        Item item1 = itemDAO.findById(id);
        if(item1.isEmpty()){
            tx.commit();
            throw new NotFoundException(ErrorMessage.NotFoundFrom(item));
        }
        if(item1.getId() != id){
            tx.commit();
            throw new BadRequestException("Id not match");
        }
        itemDAO.update(id,item);
        tx.commit();
        return Response.status(200).build();
    }

    // ---- END /items/{id} ----

    // NESTED WITH RESTAURANT
    @GET
    @Produces("application/json")
    @Path("/{id}/restaurants")
    public Response getAll(@PathParam("id") Integer id) throws Exception {
        tx.init();
        List<ItemWithStock> itemWithStockList = itemWithStockDAO.findByItemId(id, "true");
        tx.commit();

        if(itemWithStockList.size()==0){
            throw new NotFoundException("Restaurant not found");
        }

        baseResponse = new BaseResponse(true,200,itemWithStockList);

        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{id}/restaurants")
    public Response create(@PathParam("id") Integer id, ItemWithStock itemWithStock) throws Exception {
        if(itemWithStock.notValidAttribute()){
            throw new BadRequestException(ErrorMessage.requiredValue(itemWithStock));
        }
        if(id != itemWithStock.getItemId()){
            throw new BadRequestException("itemId not match");
        }
        if(itemDAO.findById(id).isEmpty() || restaurantDAO.findById(itemWithStock.getRestaurantId()).isEmpty()){
            throw new NotFoundException(ErrorMessage.NotFoundFrom(new Restaurant()));
        }
        tx.init();
        itemWithStockDAO.create(itemWithStock.getRestaurantId(),id,itemWithStock.getStock());
        tx.commit();
        return Response.status(201).build();
    }

    @POST
    @Consumes("application/json")
    @Path("/{itemId}/restaurants/{restaurantId}")
    public Response update(@PathParam("itemId") Integer itemId, @PathParam("restaurantId") Integer restaurantId, ItemWithStock itemWithStock) throws Exception {
        tx.init();
        ItemWithStock itemWithStock1 = itemWithStockDAO.findById(restaurantId,itemId);
        if(itemWithStock1.isEmpty()){
            tx.commit();
            throw new NotFoundException(ErrorMessage.NotFoundFrom(itemWithStock));
        }
        if(itemWithStock1.getItemId() != itemId || itemWithStock1.getRestaurantId() != restaurantId){
            tx.commit();
            throw new BadRequestException("Id not match");
        }
        itemWithStockDAO.update(restaurantId,itemId,itemWithStock.getStock());
        tx.commit();
        return Response.status(200).build();
    }

    @DELETE
    @Path("/{itemId}/restaurants/{restaurantId}")
    public Response delete(@PathParam("itemId") Integer itemId, @PathParam("restaurantId") Integer restaurantId) throws Exception {
        tx.init();
        if(itemWithStockDAO.findById(restaurantId,itemId).isEmpty()){
            tx.commit();
            throw new NotFoundException(ErrorMessage.NotFoundFrom(new ItemWithStock()));
        }
        itemWithStockDAO.delete(restaurantId, itemId);
        tx.commit();
        return Response.status(200).build();
    }
}
