package com.blibli.future.pos.restaurant.common.model;

import java.util.Map;

public abstract class BaseResource {
    public abstract Boolean isEmpty();
    public abstract Boolean notValidAttribute();
    public abstract Map<String,String> requiredAttribute();
}
