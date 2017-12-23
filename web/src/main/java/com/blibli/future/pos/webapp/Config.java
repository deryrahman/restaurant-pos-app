package com.blibli.future.pos.webapp;

class Config {
    private String baseUrl;
    private String restaurantRestPath;

    public Config() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRestaurantRestPath() {
        return restaurantRestPath;
    }

    public void setRestaurantRestPath(String restaurantRestPath) {
        this.restaurantRestPath = restaurantRestPath;
    }

    @Override
    public String toString() {
        return "Config{" +
                "baseUrl='" + baseUrl + '\'' +
                ", restaurantRestPath='" + restaurantRestPath + '\'' +
                '}';
    }
}
