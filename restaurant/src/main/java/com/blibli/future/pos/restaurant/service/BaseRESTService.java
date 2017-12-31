package com.blibli.future.pos.restaurant.service;

import com.blibli.future.pos.restaurant.common.ErrorMessage;
import com.blibli.future.pos.restaurant.common.TransactionHandler;
import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.common.model.BaseResponse;
import com.blibli.future.pos.restaurant.common.model.User;
import com.blibli.future.pos.restaurant.dao.user.UserDAOMysql;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.MDC;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;

@SuppressWarnings("ALL")
public abstract class BaseRESTService {
    protected static final ObjectMapper objectMapper = new ObjectMapper();
    protected static final String ADMIN = "admin";
    protected static final String MANAGER = "manager";
    protected static final String CASHIER = "cashier";

    protected static TransactionHelper tx;
    protected static TransactionHandler th;
    protected static BaseResponse baseResponse;
    protected static String json;
    protected static Integer userId;
    protected static Integer restaurantId;
    protected static String ROLE = null;

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    protected boolean userIs(String role) {
        if(userId == null){
            throw new NotAuthorizedException(ErrorMessage.USER_NOT_ALLOWED);
        }
        return ROLE.equals(role);
    }
}
