package com.blibli.future.pos.restaurant.service;


import com.blibli.future.pos.restaurant.common.model.*;
import com.blibli.future.pos.restaurant.common.model.custom.ItemOnReceipt;
import com.blibli.future.pos.restaurant.common.model.custom.ItemWithStock;
import com.blibli.future.pos.restaurant.common.model.custom.ReceiptWithItem;
import com.blibli.future.pos.restaurant.dao.item.ItemDAO;
import com.blibli.future.pos.restaurant.dao.item.ItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.custom.itemwithstock.ItemWithStockDAOMysql;
import com.blibli.future.pos.restaurant.dao.receipt.ReceiptDAOMysql;
import com.blibli.future.pos.restaurant.dao.custom.receiptwithitem.ReceiptWithItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.restaurant.RestaurantDAOMysql;
import com.blibli.future.pos.restaurant.dao.user.UserDAOMysql;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
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
    public Response create(Restaurant restaurant) throws Exception {
        if(restaurant.notValidAttribute()){
            throw new BadRequestException(ErrorMessage.requiredValue(restaurant));
        }
        th.runTransaction(conn -> {
            restaurantDAO.create(restaurant);
            return null;
        });

        baseResponse = new BaseResponse(true, 201);
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
        throw new Exception("Method not allowed");
    }

    @PUT
    @Produces("application/json")
    public Response update() throws Exception {
        throw new Exception("Method not allowed");
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
        throw new Exception("Method not allowed");
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) throws Exception {
        th.runTransaction(conn -> {
            if(restaurantDAO.findById(id).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(restaurant));
            }
            restaurantDAO.delete(id);
            return null;
        });

        baseResponse = new BaseResponse(true,200);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, Restaurant restaurant) throws Exception {
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

    // ---- NESTED ----

    /**
     * special purpose of nested resources. Like /restaurants/1/items. It will call /items, on itemsResources and its stock for specific restaurant
     */
    @GET
    @Path("/{restaurantId}/items")
    @Produces("application/json")
    public Response getAllItem(@PathParam("restaurantId") int restaurantId) throws Exception {
        itemWithStockList = (List<ItemWithStock>) th.runTransaction(conn -> {
            if(restaurantDAO.findById(restaurantId).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(restaurant));
            }
            List<ItemWithStock> itemWithStockList = itemWithStockDAO.findByRestaurantId(restaurantId, "true");
            return itemWithStockList;
        });
        baseResponse = new BaseResponse(true,200, itemWithStockList);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/{restaurantId}/items")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addItem(ItemWithStock itemWithStock, @PathParam("restaurantId") int restaurantId) throws Exception {
        // Check item valid
        itemWithStock.setRestaurantId(restaurantId);
        if(itemWithStock.notValidAttribute()){
            throw new BadRequestException(ErrorMessage.requiredValue(itemWithStock));
        }

        th.runTransaction(conn -> {
            // Check restaturant valid
            Restaurant restaurant = restaurantDAO.findById(restaurantId);
            if(restaurant.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(restaurant));
            }

            if(itemDAO.findById(itemWithStock.getItemId()).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Item()));
            }
            // Temporary 0
            itemWithStockDAO.create(restaurantId, itemWithStock.getItemId(), itemWithStock.getStock());
            return null;
        });

        baseResponse = new BaseResponse(true, 201);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(201).entity(json).build();
    }

    @GET
    @Path("/{restaurantId}/users")
    @Produces("application/json")
    public Response getAllUser(@PathParam("restaurantId") int restaurantId) throws Exception {
        users = (List<User>) th.runTransaction(conn -> {
            if(restaurantDAO.findById(restaurantId).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(restaurant));
            }
            List<User> users = userDAO.find("restaurant_id="+restaurantId);
            return users;
        });
        baseResponse = new BaseResponse(true,200, users);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @GET
    @Path("/{restaurantId}/receipts")
    @Produces("application/json")
    public Response getAllReceipt(@PathParam("restaurantId") int restaurantId) throws Exception {
        receipts = (List<Receipt>) th.runTransaction(conn -> {
            if(restaurantDAO.findById(restaurantId).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Restaurant()));
            }
            List<Receipt> receipts = receiptDAO.find("restaurant_id="+restaurantId);
            if (receipts.size() == 0) {
                throw new NotFoundException(ErrorMessage.NotFoundFrom(receipts));
            }
            return receipts;
        });
        baseResponse = new BaseResponse(true,200, receipts);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/{restaurantId}/receipts")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addReceipt(@PathParam("restaurantId") Integer restaurantId, ReceiptWithItem receiptWithItem) throws Exception {
        if(receiptWithItem.notValidAttribute()){
            throw new BadRequestException(ErrorMessage.requiredValue(receiptWithItem));
        }

        th.runTransaction(conn -> {

            // Check if item on receipt is valid
            for (ItemOnReceipt item : receiptWithItem.getItems()) {
                if(item.notValidAttribute()){
                    throw new BadRequestException(ErrorMessage.requiredValue(item));
                }
                if(itemDAO.findById(item.getItemId()).isEmpty()){
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(item));
                }
            }

            // Check if receipt id is valid
            if(!receiptWithItemDAO.findById(receiptWithItem.getReceiptId()).isEmpty()){
                throw new BadRequestException("Id already taken");
            }

            // Create new receipt
            receipt = new Receipt();
            receipt.setId(receiptWithItem.getReceiptId());
            receipt.setRestaurantId(restaurantId);
            // Temporary 1
            receipt.setUserId(1);

            // Create receipt first
            receiptDAO.create(receipt);

            // create receipt with item
            receiptWithItemDAO.create(receiptWithItem);
            return null;
        });

        baseResponse = new BaseResponse(true, 201);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(201).entity(json).build();
    }
//

//
//    /**
//     * special purpose of nested resources. Like /restaurants/1/users. It will call /users, on userResource
//     */
//    @GET
//    @Path("/{restaurantId}/users")
//    @Produces("application/json")
//    public Response getAllUser(@PathParam("restaurantId") int restaurantId) throws Exception {
//        tx.init();
//        List<User> users = userDAO.find("restaurant_id="+restaurantId);
//        tx.commit();
//
//        Map<String, Object> map = new HashMap<>();
//        Metadata metadata = new Metadata();
//        metadata.setCount(users.size());
//        metadata.setLimit(users.size());
//
//        map.put("metadata", metadata);
//        map.put("results", users);
//
//        String json = gson.toJson(map);
//        return Response.status(200).entity(json).build();
//    }

    // ---- END NESTED ----
}
