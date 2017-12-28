package com.blibli.future.pos.restaurant.service;


import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.model.*;
import com.blibli.future.pos.restaurant.common.model.custom.ItemWithStock;
import com.blibli.future.pos.restaurant.dao.category.CategoryDAOMysql;
import com.blibli.future.pos.restaurant.dao.item.ItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.custom.itemwithstock.ItemWithStockDAOMysql;
import com.blibli.future.pos.restaurant.dao.receipt.ReceiptDAOMysql;
import com.blibli.future.pos.restaurant.dao.custom.receiptwithitem.ReceiptWithItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.restaurant.RestaurantDAOMysql;
import com.blibli.future.pos.restaurant.dao.user.UserDAOMysql;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Path("/restaurants")
public class RestaurantService extends BaseRESTService {
    private static RestaurantDAOMysql restaurantDAO = new RestaurantDAOMysql();
    private static ItemDAOMysql itemDAO = new ItemDAOMysql();
    private static ItemWithStockDAOMysql itemWithStockDAO = new ItemWithStockDAOMysql();
    private static ReceiptDAOMysql receiptDAO = new ReceiptDAOMysql();
    private static ReceiptWithItemDAOMysql receiptWithItemDAO = new ReceiptWithItemDAOMysql();
    private static UserDAOMysql userDAO = new UserDAOMysql();
    private static CategoryDAOMysql categoryDAO = new CategoryDAOMysql();

    private Restaurant restaurant;
    private Receipt receipt;
    private List<Restaurant> restaurants;
    private List<ItemWithStock> itemWithStockList;
    private List<User> users;
    private List<Receipt> receipts;

    // ---- BEGIN /restaurants ----
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(List<Restaurant> restaurants) throws Exception {
        initializeRole();
        if(!userIs(ADMIN)){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        if(restaurants.isEmpty()){
            throw new BadRequestException();
        }
        this.restaurants = new ArrayList<>();
        for (Restaurant restaurant: restaurants) {
            if(restaurant.notValidAttribute()){
                throw new BadRequestException(ErrorMessage.requiredValue(restaurant));
            }
            Restaurant restaurant1 = (Restaurant) th.runTransaction(conn -> {
                restaurantDAO.create(restaurant);
                return restaurant;
            });
            this.restaurants.add(restaurant1);
        }

        baseResponse = new BaseResponse(true, 201, this.restaurants);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(201).entity(json).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {
        restaurants = (List<Restaurant>) th.runTransaction(conn -> {
            List<Restaurant> restaurants = restaurantDAO.find("true");
            return restaurants;
        });

        baseResponse = new BaseResponse(true,200,restaurants);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @DELETE
    @Produces("application/json")
    public Response delete() throws Exception {
        throw new NotAllowedException(ErrorMessage.DELETE_NOT_ALLOWED);
    }

    @PUT
    @Produces("application/json")
    public Response update() throws Exception {
        throw new NotAllowedException(ErrorMessage.PUT_NOT_ALLOWED);
    }
    // ---- END /restaurants ----

    // ---- BEGIN /restaurants/{id} ----
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id) throws Exception {
        restaurant = (Restaurant) th.runTransaction(conn -> {
            Restaurant restaurant = restaurantDAO.findById(id);
            if(restaurant.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(restaurant));
            }
            return restaurant;
        });

        baseResponse = new BaseResponse(true,200,restaurant);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/{id}")
    @Produces("application/json")
    public Response create(@PathParam("id") int id) throws Exception {
        throw new NotAllowedException(ErrorMessage.POST_NOT_ALLOWED);
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") int id) throws Exception {
        throw new NotAllowedException(ErrorMessage.DELETE_NOT_ALLOWED);
//        if(!userIs(ADMIN)){
//            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
//        }
//        th.runTransaction(conn -> {
//            if(restaurantDAO.findById(id).isEmpty()){
//                throw new NotFoundException(ErrorMessage.NotFoundFrom(restaurant));
//            }
//            restaurantDAO.delete(id);
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
    public Response update(@PathParam("id") int id, Restaurant restaurant) throws Exception {
        initializeRole();
        if(!(userIs(ADMIN) || (this.restaurantId == id && userIs(MANAGER)))){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        th.runTransaction(conn -> {
            this.restaurant = restaurantDAO.findById(id);

            if(this.restaurant.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(this.restaurant));
            }

            if(this.restaurant.getId() != id){
                throw new BadRequestException("Id not match");
            }

            restaurantDAO.update(id,restaurant);

            return null;
        });

        baseResponse = new BaseResponse(true,200);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }
    // ---- END /restaurants/{id} ----

}
