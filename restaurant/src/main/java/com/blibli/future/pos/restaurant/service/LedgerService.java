package com.blibli.future.pos.restaurant.service;

import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.model.BaseResponse;
import com.blibli.future.pos.restaurant.common.model.Receipt;
import com.blibli.future.pos.restaurant.common.model.Restaurant;
import com.blibli.future.pos.restaurant.common.model.custom.Ledger;
import com.blibli.future.pos.restaurant.dao.receipt.ReceiptDAOMysql;
import com.blibli.future.pos.restaurant.dao.restaurant.RestaurantDAOMysql;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("/ledgers")
public class LedgerService extends BaseRESTService{
    private static final ReceiptDAOMysql receiptDAO = new ReceiptDAOMysql();
    private static final RestaurantDAOMysql restaurantDAO = new RestaurantDAOMysql();

    private Ledger ledger;

    private Long dayToMilis(Integer days){
        return Long.valueOf((days*(24*60*60*1000)));
    }

    @GET
    @Produces("application/json")
    public Response getAll(
            @Context HttpServletRequest req,
            @QueryParam("id") Integer id,
            @QueryParam("st") String startTime,
            @QueryParam("et") String endTime
    ) throws Exception {
        setUser(req);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp startTimestamp = new Timestamp(dateFormat.parse(startTime).getTime());
        Timestamp endTimestamp = new Timestamp(dateFormat.parse(endTime).getTime() + dayToMilis(1));

        Date date = new Date();
        date.setTime(startTimestamp.getTime());
        final String st = new SimpleDateFormat("yyyy-MM-dd").format(date);
        date.setTime(endTimestamp.getTime());
        final String et = new SimpleDateFormat("yyyy-MM-dd").format(date);

        if(userIs(ADMIN)){
            if(id == null){
                throw new NotFoundException(ErrorMessage.NotFoundFrom(new Restaurant()));
            }
            ledger = (Ledger) th.runTransaction(conn -> {

                if(restaurantDAO.findById(restaurantId).isEmpty()){
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(new Restaurant()));
                }

                List<Receipt> receipts = receiptDAO.find("timestamp_created >= '"+st+"' AND timestamp_created <= '"+ et +"' AND restaurant_id = "+id);
                if(receipts.size() == 0){
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(new Receipt()));
                }
                BigDecimal total = BigDecimal.ZERO;
                for (Receipt receipt:receipts) {
                    total = total.add(receipt.getTotalPrice());
                }
                System.out.println(total);
                Ledger ledger = new Ledger();
                ledger.setStartTime(startTimestamp);
                ledger.setEndTime(endTimestamp);
                ledger.setRestaurantId(id);
                ledger.setTotal(total);
                ledger.setReceipts(receipts);
                return ledger;
            });

            baseResponse = new BaseResponse(true,200,ledger);
            json = objectMapper.writeValueAsString(baseResponse);
            return Response.status(200).entity(json).build();
        }
        if(userIs(MANAGER)){
            ledger = (Ledger) th.runTransaction(conn -> {
                List<Receipt> receipts = receiptDAO.find("timestamp_created >= '"+st+"' AND timestamp_created <= '"+ et +"' AND restaurant_id = "+restaurantId);
                if(receipts.size() == 0){
                    throw new NotFoundException(ErrorMessage.NotFoundFrom(new Receipt()));
                }
                BigDecimal total = BigDecimal.ZERO;
                for (Receipt receipt:receipts) {
                    total = total.add(receipt.getTotalPrice());
                }
                System.out.println(total);
                Ledger ledger = new Ledger();
                ledger.setStartTime(startTimestamp);
                ledger.setEndTime(endTimestamp);
                ledger.setRestaurantId(restaurantId);
                ledger.setTotal(total);
                ledger.setReceipts(receipts);
                return ledger;
            });

            baseResponse = new BaseResponse(true,200,ledger);
            json = objectMapper.writeValueAsString(baseResponse);
            return Response.status(200).entity(json).build();
        }

        throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
    }
}
