package com.blibli.future.pos.restaurant.common.exception;

import com.blibli.future.pos.restaurant.common.model.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class BaseExceptionMapper<T extends Exception> implements ExceptionMapper<T> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public Response toResponse(T exception) {
        String msg = exception.getClass().getSimpleName() + " : " + exception.getMessage();
        BaseResponse baseResponse = new BaseResponse(false,getStatusCode(), msg);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(baseResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Response.status(getStatusCode()).entity(json).build();
    }

    public Integer getStatusCode(){
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }
}
