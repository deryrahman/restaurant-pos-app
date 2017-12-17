package com.blibli.future.pos.restaurant.service;


import com.blibli.future.pos.restaurant.common.model.*;
import com.blibli.future.pos.restaurant.dao.receipt.ReceiptDAOMysql;
import com.blibli.future.pos.restaurant.dao.receipt.ReceiptItemDAOMysql;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/receipts")
public class ReceiptService {
    private ReceiptDAOMysql receiptDAO = new ReceiptDAOMysql();
    private ReceiptItemDAOMysql receiptItemDAO = new ReceiptItemDAOMysql();
    private Gson gson = new Gson();
    private Message msg = new Message();

//    private Response get405Response(){
//        msg.setMessage("Method not allowed");
//        String json = gson.toJson(msg);
//        return Response.status(405).entity(json).build();
//    }
//
//    private Response get404Response(){
//        msg.setMessage("Not found");
//        String json = gson.toJson(msg);
//        return Response.status(404).entity(json).build();
//    }

    // ---- BEGIN /receipts ----

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(ReceiptItems receiptItems) throws SQLException {
        receiptDAO.create(receiptItems.getReceipt());
        int lastId = receiptDAO.getId();
        receiptItemDAO.createBulk(lastId,receiptItems.getItems());
        return Response.status(201).build();
    }

    @GET
    @Produces("application/json")
    public Response getAll() throws SQLException {
        Gson gson = new Gson();
        List<Receipt> receipts = receiptDAO.getBulk("true");

        Map<String, Object> map = new HashMap<>();
        Metadata metadata = new Metadata();
        metadata.setCount(receipts.size());
        metadata.setLimit(receipts.size());

        map.put("metadata", metadata);
        map.put("results", receipts);

        String json = gson.toJson(map);
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
    // ---- END /receipts ----


    // ---- BEGIN /receipts/{id} ----

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") int id) throws Exception {
        Gson gson = new Gson();
        ReceiptItems receiptItems = new ReceiptItems();
        Receipt receipt = receiptDAO.getById(id);

        if(receipt == null){
            throw new Exception("Not found");
        }
        receiptItems.setReceipt(receipt);
        receiptItems.setItems(receiptItemDAO.getBulkByReceiptId(id));

        String json = gson.toJson(receiptItems);
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
    public Response delete(@PathParam("id") int id) throws SQLException {
        receiptDAO.delete(id);
        return Response.status(204).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, Receipt receipt) throws SQLException {
        Gson gson = new Gson();
        gson.toJson(receipt);
        receiptDAO.update(id,receipt);
        return Response.status(204).build();
    }

    // ---- END /receipts/{id} ----
}
