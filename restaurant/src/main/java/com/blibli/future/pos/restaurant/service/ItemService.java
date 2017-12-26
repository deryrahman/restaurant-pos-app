package com.blibli.future.pos.restaurant.service;


import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.model.*;
import com.blibli.future.pos.restaurant.dao.category.CategoryDAOMysql;
import com.blibli.future.pos.restaurant.dao.item.ItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.custom.itemwithstock.ItemWithStockDAOMysql;
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
    private CategoryDAOMysql categoryDAO = new CategoryDAOMysql();

    private List<Item> items;
    private Item item;

    // ---- BEGIN /items ----

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(List<Item> items) throws Exception {
        if(items.isEmpty()){
            throw new BadRequestException();
        }

        for (Item item : items) {
            if(item.getCategoryId() == null){
                item.setCategoryId(1);
            }
            if(item.notValidAttribute()){
                throw new BadRequestException(ErrorMessage.requiredValue(item));
            }
            th.runTransaction(conn -> {
                if(categoryDAO.findById(item.getCategoryId()).isEmpty()){
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(new Category()));
                }
                itemDAO.create(item);
                return null;
            });
        }

        baseResponse = new BaseResponse(true, 201);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(201).entity(json).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {
        items = (List<Item>) th.runTransaction(conn -> {
            List<Item> items = itemDAO.find("true");
            if(items.size()==0){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(item));
            }
            return items;
        });

        baseResponse = new BaseResponse(true,200,items);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

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
        item = (Item) th.runTransaction(conn -> {
            Item item = itemDAO.findById(id);
            if(item.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(item));
            }
            return item;
        });

        baseResponse = new BaseResponse(true,200,item);
        json = objectMapper.writeValueAsString(baseResponse);
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
    @Produces("application/json")
    public Response delete(@PathParam("id") int id) throws Exception {
        throw new NotAllowedException(ErrorMessage.DELETE_NOT_ALLOWED, Response.status(405).build());
//        th.runTransaction(conn -> {
//            if(itemDAO.findById(id).isEmpty()){
//                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Item()));
//            }
//            itemDAO.delete(id);
//            return null;
//        });
//
//        baseResponse = new BaseResponse(true,200);
//        json = objectMapper.writeValueAsString(baseResponse);
//        return Response.status(200).entity(json).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(@PathParam("id") int id, Item item) throws Exception {
        if(item.notValidAttribute()){
            throw new BadRequestException(ErrorMessage.requiredValue(item));
        }
        th.runTransaction(conn -> {
            this.item = itemDAO.findById(id);

            if(this.item.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(item));
            }

            if(this.item.getId() != id){
                throw new BadRequestException("Id not match");
            }

            itemDAO.update(id,item);

            return null;
        });

        baseResponse = new BaseResponse(true,200);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    // ---- END /items/{id} ----

}
