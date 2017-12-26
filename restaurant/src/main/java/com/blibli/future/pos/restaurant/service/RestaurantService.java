package com.blibli.future.pos.restaurant.service;


import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.model.*;
import com.blibli.future.pos.restaurant.common.model.custom.ItemOnReceipt;
import com.blibli.future.pos.restaurant.common.model.custom.ItemWithStock;
import com.blibli.future.pos.restaurant.common.model.custom.ReceiptWithItem;
import com.blibli.future.pos.restaurant.dao.category.CategoryDAOMysql;
import com.blibli.future.pos.restaurant.dao.item.ItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.custom.itemwithstock.ItemWithStockDAOMysql;
import com.blibli.future.pos.restaurant.dao.receipt.ReceiptDAOMysql;
import com.blibli.future.pos.restaurant.dao.custom.receiptwithitem.ReceiptWithItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.restaurant.RestaurantDAOMysql;
import com.blibli.future.pos.restaurant.dao.user.UserDAOMysql;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
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
        if(restaurants.isEmpty()){
            throw new BadRequestException();
        }
        for (Restaurant restaurant: restaurants) {
            if(restaurant.notValidAttribute()){
                throw new BadRequestException(ErrorMessage.requiredValue(restaurant));
            }
            th.runTransaction(conn -> {
                restaurantDAO.create(restaurant);
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
    @Produces("application/json")
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
    @Produces("application/json")
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
    public Response getAllItem(@PathParam("restaurantId") Integer restaurantId) throws Exception {
        itemWithStockList = (List<ItemWithStock>) th.runTransaction(conn -> {
            if(restaurantDAO.findById(restaurantId).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(restaurant));
            }
            List<ItemWithStock> itemWithStockList = itemWithStockDAO.findByRestaurantId(restaurantId, "true");
            return itemWithStockList;
        });
        if(itemWithStockList.isEmpty()){
            throw new NotFoundException(ErrorMessage.NotFoundFrom(new ItemWithStock()));
        }
        baseResponse = new BaseResponse(true,200, itemWithStockList);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @GET
    @Path("/{restaurantId}/categories/{categoryId}/items")
    @Produces("application/json")
    public Response GetAllItemByCategoryId(@PathParam("restaurantId") Integer restaurantId, @PathParam("categoryId") Integer categoryId) throws Exception{
        itemWithStockList = (List<ItemWithStock>) th.runTransaction(conn -> {
            if(restaurantDAO.findById(restaurantId).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Restaurant()));
            }
            if(categoryDAO.findById(categoryId).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Category()));
            }
            List<ItemWithStock> itemWithStockList = itemWithStockDAO.findByRestaurantId(restaurantId, "category_id="+categoryId);
            return itemWithStockList;
        });
        if(itemWithStockList.isEmpty()){
            throw new NotFoundException(ErrorMessage.NotFoundFrom(new ItemWithStock()));
        }
        baseResponse = new BaseResponse(true,200, itemWithStockList);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/{restaurantId}/items")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addItem(List<ItemWithStock> itemWithStockList, @PathParam("restaurantId") Integer restaurantId) throws Exception {
        if(itemWithStockList.isEmpty()){
            throw new BadRequestException();
        }
        for (ItemWithStock itemWithStock : itemWithStockList) {
            // Check item valid
            itemWithStock.setRestaurantId(restaurantId);
            if(itemWithStock.notValidAttribute()){
                throw new BadRequestException(ErrorMessage.requiredValue(itemWithStock));
            }

            insertItem(restaurantId, itemWithStock);
        }

        baseResponse = new BaseResponse(true, 201);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(201).entity(json).build();
    }

    private void insertItem(int restaurantId, ItemWithStock itemWithStock) throws Exception {
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
    }

    @PUT
    @Path("/{restaurantId}/items/{itemId}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateStock(@PathParam("restaurantId") Integer restaurantId, @PathParam("itemId") Integer itemId, ItemWithStock itemWithStock) throws Exception{
        itemWithStock.setRestaurantId(restaurantId);
        itemWithStock.setItemId(itemId);
        if(itemWithStock.isEmpty()){
            throw new BadRequestException(ErrorMessage.requiredValue(itemWithStock));
        }
        th.runTransaction(conn -> {
            // Check restaturant valid
            Restaurant restaurant = restaurantDAO.findById(restaurantId);
            if(restaurant.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Restaurant()));
            }

            if(itemDAO.findById(itemWithStock.getItemId()).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Item()));
            }

            itemWithStockDAO.update(itemWithStock.getRestaurantId(), itemWithStock.getItemId(), itemWithStock.getStock());
            return null;
        });

        baseResponse = new BaseResponse(true, 200);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }
    @GET
    @Path("/{restaurantId}/users")
    @Produces("application/json")
    public Response getAllUser(@PathParam("restaurantId") Integer restaurantId) throws Exception {
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
    public Response getAllReceipt(@PathParam("restaurantId") Integer restaurantId) throws Exception {
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

        json = objectMapper.writeValueAsString(receipts);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/{restaurantId}/receipts")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addReceipt(@PathParam("restaurantId") Integer restaurantId, List<ReceiptWithItem> receiptWithItemList) throws Exception {
        if(receiptWithItemList.isEmpty()){
            throw new BadRequestException();
        }
        for (ReceiptWithItem receiptWithItem : receiptWithItemList) {
            if(receiptWithItem.notValidAttribute()){
                throw new BadRequestException(ErrorMessage.requiredValue(receiptWithItem));
            }
            insertReceipt(restaurantId, receiptWithItem);
        }

        baseResponse = new BaseResponse(true, 201);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(201).entity(json).build();
    }

    public void insertReceipt(Integer restaurantId, ReceiptWithItem receiptWithItem) throws Exception {
        th.runTransaction(conn -> {
            // check if restaurant exist
            if(restaurantDAO.findById(restaurantId).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Restaurant()));
            }

            // Check if item on receipt is valid
            BigDecimal total = BigDecimal.ZERO;
            for(int i = 0; i < receiptWithItem.getItems().size(); i++){
                ItemOnReceipt item = receiptWithItem.getItems().get(i);
                if(item.notValidAttribute() || item.getCount() == 0){
                    throw new BadRequestException(ErrorMessage.requiredValue(item));
                }
                Item item1 = itemDAO.findById(item.getItemId());
                if(item1.isEmpty()){
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(item));
                }
                item.setItemName(item1.getName());
                BigDecimal decimal = item1.getPrice();
                BigDecimal subtotal = decimal.multiply(BigDecimal.valueOf(item.getCount().longValue()));
                item.setSubTotal(subtotal);
                total = total.add(subtotal);

                // check stock and decrease stock by count item
                ItemWithStock itemWithStock = itemWithStockDAO.findById(restaurantId, item.getItemId());
                if(itemWithStock.getStock()<item.getCount()){
                    throw new BadRequestException("Stock is not sufficient");
                }
                Integer remainStock = itemWithStock.getStock()-item.getCount();
                itemWithStockDAO.update(restaurantId, item.getItemId(), remainStock);
            }

            // Check if receipt id is valid
            if(!receiptWithItemDAO.findById(receiptWithItem.getReceiptId()).isEmpty()){
                throw new BadRequestException("Id already taken");
            }

            // Create new receipt
            receipt = new Receipt();
            receipt.setId(receiptWithItem.getReceiptId());
            receipt.setRestaurantId(restaurantId);
            receipt.setTotalPrice(total);
            // Temporary 1
            receipt.setUserId(1);

            // Create receipt first
            receiptDAO.create(receipt);
            receiptWithItem.setReceiptId(receipt.getId());
            // create receipt with item
            receiptWithItemDAO.create(receiptWithItem);
            return null;
        });
    }

    @DELETE
    @Path("/{restaurantId}/items/{itemId}")
    @Produces("application/json")
    public Response deleteItemOnRestaurant(@PathParam("restaurantId") Integer restaurantId, @PathParam("itemId") Integer itemId) throws Exception{
        th.runTransaction(conn -> {
            if(restaurantDAO.findById(restaurantId).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Restaurant()));
            }
            if(itemDAO.findById(itemId).isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Item()));
            }
            itemWithStockDAO.delete(restaurantId, itemId);
            return null;
        });
        baseResponse = new BaseResponse(true,200);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }
}
