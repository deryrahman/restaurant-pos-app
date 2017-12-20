package com.blibli.future.pos.restaurant.common;

import com.blibli.future.pos.restaurant.common.model.BaseResource;
import com.blibli.future.pos.restaurant.common.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@SuppressWarnings("ALL")
public class ErrorMessage {
    private static final String NOT_ALLOWED = " method not allowed";
    public static final String GET_NOT_ALLOWED = "GET" + NOT_ALLOWED;
    public static final String POST_NOT_ALLOWED = "POST" + NOT_ALLOWED;
    public static final String DELETE_NOT_ALLOWED = "DELETE" + NOT_ALLOWED;
    public static final String PUT_NOT_ALLOWED = "PUT" + NOT_ALLOWED;

    public static String NotFoundFrom(Object obj){
        return obj.getClass().getSimpleName() + " not found";
    }

    public static String requiredValue(BaseResource model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return model.getClass().getSimpleName() + "" + objectMapper.writeValueAsString(model.requiredAttribute());
    }
}
