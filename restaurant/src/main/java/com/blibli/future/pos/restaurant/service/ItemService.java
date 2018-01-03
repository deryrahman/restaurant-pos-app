package com.blibli.future.pos.restaurant.service;


import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.model.*;
import com.blibli.future.pos.restaurant.common.model.custom.ItemWithStock;
import com.blibli.future.pos.restaurant.dao.category.CategoryDAOMysql;
import com.blibli.future.pos.restaurant.dao.item.ItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.custom.itemwithstock.ItemWithStockDAOMysql;
import com.blibli.future.pos.restaurant.dao.restaurant.RestaurantDAOMysql;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Path("/items")
public class ItemService extends BaseRESTService{
    private static final ItemDAOMysql itemDAO = new ItemDAOMysql();
    private static final RestaurantDAOMysql restaurantDAO = new RestaurantDAOMysql();
    private static final ItemWithStockDAOMysql itemWithStockDAO = new ItemWithStockDAOMysql();
    private static final CategoryDAOMysql categoryDAO = new CategoryDAOMysql();
    private List<Item> items;
    private Item item;

    // ---- BEGIN /items ----

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(@Context HttpServletRequest req, List<Item> items) throws Exception {
        setUser(req);

        if(!userIs(ADMIN)){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        if(items.isEmpty()){
            throw new BadRequestException();
        }
        this.items = new ArrayList<>();
        for (Item item : items) {
            if(item.getCategoryId() == null){
                item.setCategoryId(1);
            }
            if(item.notValidAttribute()){
                throw new BadRequestException(ErrorMessage.requiredValue(item));
            }
            Item item1 = (Item) th.runTransaction(conn -> {
                if(categoryDAO.findById(item.getCategoryId()).isEmpty()){
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(new Category()));
                }
                itemDAO.create(item);
                return item;
            });
            this.items.add(item1);
        }

        baseResponse = new BaseResponse(true, 201,this.items);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(201).entity(json).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll(@Context HttpServletRequest req) throws Exception {
        setUser(req);

        if(userIs(ADMIN)){
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

        List<ItemWithStock> itemWithStockList = (List<ItemWithStock>) th.runTransaction(conn -> {
            List<ItemWithStock> itemWithStockList1 = itemWithStockDAO.findByRestaurantId(this.restaurantId, "true");
            if(itemWithStockList1.size()==0){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(item));
            }
            if(userIs(MANAGER)){
                items = itemDAO.find("true");
                int j = 0;
                for (int i = 0; i<itemWithStockList1.size(); i++) {
                    for(;j<items.size();j++){
                        if(items.get(j).getId() == itemWithStockList1.get(i).getItemId()){
                            break;
                        } else {
                            Integer itemId = items.get(j).getId();
                            String name = items.get(j).getName();
                            BigDecimal price = items.get(j).getPrice();
                            Integer categoryId = items.get(j).getCategoryId();
                            ItemWithStock newItem = new ItemWithStock();
                            newItem.setItemId(itemId);
                            newItem.setCategoryId(categoryId);
                            newItem.setStock(0);
                            newItem.setPrice(price);
                            newItem.setItemName(name);
                            itemWithStockList1.add(newItem);
                        }
                    }
                }
            }
            return itemWithStockList1;
        });

        baseResponse = new BaseResponse(true,200,itemWithStockList);
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
    public Response get(@Context HttpServletRequest req, @PathParam("id") int id) throws Exception {
        setUser(req);

        if(userIs(ADMIN)) {
            item = (Item) th.runTransaction(conn -> {
                Item item = itemDAO.findById(id);
                if (item.isEmpty()) {
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(item));
                }
                return item;
            });

            baseResponse = new BaseResponse(true, 200, item);
            json = objectMapper.writeValueAsString(baseResponse);
            return Response.status(200).entity(json).build();
        }

        ItemWithStock itemWithStock = (ItemWithStock) th.runTransaction(conn -> {
            ItemWithStock item = itemWithStockDAO.findById(this.restaurantId, id);
            if (item.isEmpty()) {
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new ItemWithStock()));
            }
            return item;
        });

        baseResponse = new BaseResponse(true, 200, itemWithStock);
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
    public Response update(@Context HttpServletRequest req, @PathParam("id") int id, Item item) throws Exception {
        setUser(req);

        if(!userIs(ADMIN)){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
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

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/stock")
    public Response addStock(@Context HttpServletRequest req, List<ItemWithStock> itemWithStockList) throws Exception {
        setUser(req);

        if(!userIs(MANAGER)){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        if(itemWithStockList.isEmpty()){
            throw new BadRequestException();
        }
        for (ItemWithStock itemWithStock : itemWithStockList) {
            // Check item valid
            itemWithStock.setRestaurantId(this.restaurantId);
            if(itemWithStock.notValidAttribute()){
                throw new BadRequestException(ErrorMessage.requiredValue(itemWithStock));
            }

            insertItem(this.restaurantId, itemWithStock);
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
    @Path("/stock/{itemId}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateStock(@Context HttpServletRequest req, @PathParam("itemId") Integer itemId, ItemWithStock itemWithStock) throws Exception{
        setUser(req);

        if(!userIs(MANAGER)){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        itemWithStock.setRestaurantId(this.restaurantId);
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

    @DELETE
    @Path("/stock/{itemId}")
    @Produces("application/json")
    public Response deleteItemOnRestaurant(@Context HttpServletRequest req, @PathParam("itemId") Integer itemId) throws Exception{
        setUser(req);

        if(userIs(MANAGER)){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        th.runTransaction(conn -> {
            if(restaurantDAO.findById(this.restaurantId).isEmpty()){
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
