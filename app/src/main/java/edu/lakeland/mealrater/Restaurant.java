package edu.lakeland.mealrater;

public class Restaurant {
    private Integer restaurant_ID;
    private String restaurant_name;
    private String restaurant_latitude;
    private String restaurant_longitude;

    public Restaurant(){
        restaurant_ID = -1;
        restaurant_name="";
        restaurant_latitude="";
        restaurant_longitude="";
    }

    public void setRestaurantID(Integer restaurantID){
        this.restaurant_ID = restaurantID;
    }
    public Integer getRestaurant_ID(){
        return this.restaurant_ID;
    }
    public String getRestaurant_name() {
        return this.restaurant_name;
    }
    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }
    public String getRestaurant_latitude() {
        return this.restaurant_latitude;
    }
    public void setRestaurant_latitude(String restaurant_latitude){
        this.restaurant_latitude = restaurant_latitude;
    }
    public String getRestaurant_longitude(){
        return this.restaurant_longitude;
    }
    public void setRestaurant_longitude(String restaurant_longitude){
        this.restaurant_longitude = restaurant_longitude;
    }
}
