package edu.lakeland.mealrater;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Meal {
    public final String TAG = "Meals";
    private Integer mealID;
    private String meal;
    private String restaurant;
    private String rating;
    private Double restaurant_latitude;
    private Double restaurant_longitude;
    private Bitmap picture;

    public Meal() {
        mealID=-1;
        meal="";
        restaurant="";
        rating = "";
        restaurant_latitude=0.0;
        restaurant_longitude=0.0;
    }
    public void setMealID(Integer mealID){this.mealID = mealID;}
    public Integer getMealID(){return this.mealID;}
    public String getMeal() {
        return this.meal;
    }
    public void setMeal(String meal) {
        this.meal = meal;
    }
    public String getRestaurant() {
        return this.restaurant;
    }
    public void setRestaurant(String restaurant){
        this.restaurant = restaurant;
    }
    public String getRating(){
        return this.rating;
    }
    public void setRating(String rating){
        this.rating = rating;
    }
    public Double getRestaurant_latitude() {
        return this.restaurant_latitude;
    }
    public void setRestaurant_latitude(Double restaurant_latitude){ this.restaurant_latitude = restaurant_latitude; }
    public Double getRestaurant_longitude(){
        return this.restaurant_longitude;
    }
    public void setRestaurant_longitude(Double restaurant_longitude){ this.restaurant_longitude = restaurant_longitude; }
    public Bitmap getPicture() { return picture; }
    public void setPicture(Bitmap picture) { this.picture = picture; }

}
