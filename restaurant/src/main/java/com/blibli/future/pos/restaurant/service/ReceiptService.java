package com.blibli.future.pos.restaurant.service;


import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.model.*;
import com.blibli.future.pos.restaurant.common.model.custom.ReceiptWithItem;
import com.blibli.future.pos.restaurant.dao.item.ItemDAOMysql;
import com.blibli.future.pos.restaurant.dao.receipt.ReceiptDAOMysql;
import com.blibli.future.pos.restaurant.dao.custom.receiptwithitem.ReceiptWithItemDAOMysql;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("ALL")
@Path("/receipts")
public class ReceiptService extends BaseRESTService{
    private ReceiptDAOMysql receiptDAO = new ReceiptDAOMysql();
    private ReceiptWithItemDAOMysql receiptWithItemDAO = new ReceiptWithItemDAOMysql();
    private ItemDAOMysql itemDAO = new ItemDAOMysql();

    private ReceiptWithItem receiptWithItem;
    private List<ReceiptWithItem> receiptWithItemList;
    private Receipt receipt;
    private List<Receipt> receipts;

    // ---- BEGIN /receipts ----

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create() throws Exception {
        throw new NotAllowedException(ErrorMessage.POST_NOT_ALLOWED, Response.status(405).build());
    }

    @GET
    @Produces("application/json")
    public Response getAll() throws Exception {
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
        this.receiptWithItem = (ReceiptWithItem) th.runTransaction(conn -> {
            ReceiptWithItem receiptWithItem = receiptWithItemDAO.findById(id);
            if(receiptWithItem.isEmpty()){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(receiptWithItem));
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
