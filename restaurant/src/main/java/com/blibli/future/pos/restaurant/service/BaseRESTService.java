package com.blibli.future.pos.restaurant.service;

import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.TransactionHandler;
import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.common.model.BaseResponse;
import com.blibli.future.pos.restaurant.dao.user.UserDAOMysql;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.MDC;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;

@SuppressWarnings("ALL")
public abstract class BaseRESTService {
    protected static ObjectMapper objectMapper = new ObjectMapper();
    protected static TransactionHelper tx;
    protected static TransactionHandler th;
    protected BaseResponse baseResponse;
    protected String json;
    protected static Integer userId;
    protected static Integer restaurantId;
    protected static final String ADMIN = "admin";
    protected static final String MANAGER = "manager";
    protected static final String CASHIER = "cashier";
    protected static String ROLE = null;

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    protected void initializeRole() throws Exception {
        ROLE = (String) th.runTransaction(conn -> {
            return (new UserDAOMysql()).findById(userId).getRole();
        });
    }

    protected boolean userIs(String role) {
        if(userId == null){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }

        return ROLE.equals(role);
    }

    public static void insertId() throws Exception {
        userId = (Integer) MDC.get("userId");
        restaurantId = (Integer) th.runTransaction(conn -> {
            return (new UserDAOMysql()).findById(userId).getRestaurantId();
        });
    }
}
