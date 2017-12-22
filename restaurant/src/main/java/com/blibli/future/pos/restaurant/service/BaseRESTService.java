package com.blibli.future.pos.restaurant.service;

import com.blibli.future.pos.restaurant.common.TransactionHandler;
import com.blibli.future.pos.restaurant.common.TransactionHelper;
import com.blibli.future.pos.restaurant.common.model.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("ALL")
public abstract class BaseRESTService {
    protected static ObjectMapper objectMapper = new ObjectMapper();
    protected static TransactionHelper tx;
    protected static TransactionHandler th;
    protected BaseResponse baseResponse;
    protected String json;

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
