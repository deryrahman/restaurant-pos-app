package com.blibli.future.pos.util;

/**
 * Created by dery on 10/27/17.
 */
public class MetaData {
    Long total;
    Integer limit;

    public MetaData() {
    }

    public MetaData(Long total, Integer limit) {
        this.total = total;
        this.limit = limit;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
