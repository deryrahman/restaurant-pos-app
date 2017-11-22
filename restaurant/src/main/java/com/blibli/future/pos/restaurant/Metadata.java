package com.blibli.future.pos.restaurant;

public class Metadata {
    private int count;
    private int limit;

    public Metadata() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
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
