package com.blibli.future.pos.restaurant;

public class Metadata {
    private String count;
    private String limit;

    public Metadata() {
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "count='" + count + '\'' +
                ", limit='" + limit + '\'' +
                '}';
    }
}
