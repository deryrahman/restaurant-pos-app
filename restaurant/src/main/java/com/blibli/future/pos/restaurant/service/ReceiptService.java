package com.blibli.future.pos.restaurant.service;


import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.model.*;
import com.blibli.future.pos.restaurant.common.model.custom.ItemOnReceipt;
import com.blibli.future.pos.restaurant.common.model.custom.ItemWithStock;
import com.blibli.future.pos.restaurant.common.model.custom.ReceiptWithItem;
import com.blibli.future.pos.restaurant.dao.custom.itemwithstock.ItemWithStockDAOMysql;
import com.blibli.future.pos.restaurant.dao.item.ItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.receipt.ReceiptDAOMysql;
import com.blibli.future.pos.restaurant.dao.custom.receiptwithitem.ReceiptWithItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.restaurant.RestaurantDAOMysql;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("ALL")
@Path("/receipts")
public class ReceiptService extends BaseRESTService{
    private ReceiptDAOMysql receiptDAO = new ReceiptDAOMysql();
    private ReceiptWithItemDAOMysql receiptWithItemDAO = new ReceiptWithItemDAOMysql();
    private ItemDAOMysql itemDAO = new ItemDAOMysql();
    private RestaurantDAOMysql restaurantDAO = new RestaurantDAOMysql();
    private ItemWithStockDAOMysql itemWithStockDAO = new ItemWithStockDAOMysql();

    private ReceiptWithItem receiptWithItem;
    private List<ReceiptWithItem> receiptWithItemList;
    private Receipt receipt;
    private List<Receipt> receipts;

    // ---- BEGIN /receipts ----

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(List<ReceiptWithItem> receiptWithItemList) throws Exception {
        initializeRole();
        if(!userIs(CASHIER)){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }

        if(receiptWithItemList.isEmpty()){
            throw new BadRequestException();
        }
        for (ReceiptWithItem receiptWithItem : receiptWithItemList) {
            if(receiptWithItem.notValidAttribute()){
                throw new BadRequestException(ErrorMessage.requiredValue(receiptWithItem));
            }
            insertReceipt(this.restaurantId, receiptWithItem);
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
                if(itemWithStock.isEmpty()){
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(new ItemWithStock()));
                }
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

    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {
        initializeRole();
        if(userIs(ADMIN)){
            receipts = (List<Receipt>) th.runTransaction(conn -> {
                List<Receipt> receipts = receiptDAO.find("true");
                if(receipts.size()==0){
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(receipt));
                }
                return receipts;
            });

            baseResponse = new BaseResponse(true,200,receipts);
            json = objectMapper.writeValueAsString(baseResponse);
            return Response.status(200).entity(json).build();
        }
        if(userIs(MANAGER)){
            receipts = (List<Receipt>) th.runTransaction(conn -> {
                List<Receipt> receipts = receiptDAO.find("restaurant_id="+this.restaurantId);
                if(receipts.size()==0){
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(receipt));
                }
                return receipts;
            });

            baseResponse = new BaseResponse(true,200,receipts);
            json = objectMapper.writeValueAsString(baseResponse);
            return Response.status(200).entity(json).build();
        }

        throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
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
    // ---- END /receipts ----


    // ---- BEGIN /receipts/{id} ----

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id) throws Exception {
        initializeRole();
        if(!(userIs(ADMIN) || userIs(MANAGER))){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        this.receiptWithItem = (ReceiptWithItem) th.runTransaction(conn -> {
            ReceiptWithItem receiptWithItem = receiptWithItemDAO.findById(id);
            if(receiptWithItem.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(receiptWithItem));
            }
            if(userIs(MANAGER)){
                receipt = receiptDAO.findById(receiptWithItem.getReceiptId());
                if(receipt.getRestaurantId() != this.restaurantId){
                    throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
                }
            }
            return receiptWithItem;
        });

        baseResponse = new BaseResponse(true,200,receiptWithItem);
        json = objectMapper.writeValueAsString(baseResponse);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/{id}")
    @Produces("application/json")
    public Response create(@PathParam("id") int id) throws Exception {
        throw new NotAllowedException(ErrorMessage.DELETE_NOT_ALLOWED, Response.status(405).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) throws Exception {
        throw new NotAllowedException(ErrorMessage.DELETE_NOT_ALLOWED, Response.status(405).build());
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(@PathParam("id") int id, Receipt receipt) throws Exception {
        throw new NotAllowedException(ErrorMessage.PUT_NOT_ALLOWED, Response.status(405).build());
    }

    // ---- END /receipts/{id} ----


}
