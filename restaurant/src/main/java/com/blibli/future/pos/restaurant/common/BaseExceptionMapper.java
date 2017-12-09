package com.blibli.future.pos.restaurant.common;

import com.blibli.future.pos.restaurant.common.model.BaseResponse;
import com.google.gson.Gson;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public abstract class BaseExceptionMapper<T extends Exception> implements ExceptionMapper<T> {

    @Override
    public Response toResponse(T exception) {
        Gson gson = new Gson();
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSuccess(false);
        baseResponse.setMessage(getExceptionName() + " : " + exception.getMessage());
        String json = gson.toJson(baseResponse);
        return Response.status(generateStatus()).entity(json).build();
    }

    public abstract int generateStatus();

    public abstract String getExceptionName();
}
