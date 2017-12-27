package com.blibli.future.pos.restaurant.service;

import com.blibli.future.pos.restaurant.common.TransactionHandler;
import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.common.model.BaseResponse;
import com.blibli.future.pos.restaurant.dao.user.UserDAOMysql;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.MDC;

@SuppressWarnings("ALL")
public abstract class BaseRESTService {
    protected static ObjectMapper objectMapper = new ObjectMapper();
    protected static TransactionHelper tx;
    protected static TransactionHandler th;
    protected BaseResponse baseResponse;
    protected String json;
    protected Integer userId;
    protected Integer restaurantId;

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    void insertId() throws Exception {
        if(userId != null && restaurantId !=null) return;
        userId = (Integer) MDC.get("userId");
        restaurantId = (Integer) th.runTransaction(conn -> {
            return (new UserDAOMysql()).findById(userId).getRestaurantId();
        });
    }
}
