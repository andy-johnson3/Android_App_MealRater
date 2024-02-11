package edu.lakeland.mealrater;

import static java.lang.Double.parseDouble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;

public class RestClient {
    public static final String TAG = "RestClient";
    public static void executeGetRequest(String url,
                                         Context context,
                                         VolleyCallback volleyCallback)
    {
        Log.d(TAG, "executeGetRequest: Start");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Meal> meals = new ArrayList<>();
        Log.d(TAG, "executeGetRequest: " + url);
        try {
            Log.d(TAG, "executeGetRequest: in try statement");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "executeGetRequest, onResponse: " + response);
                            try {
                                JSONArray items = new JSONArray(response);

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject object = items.getJSONObject(i);
                                    Meal meal = new Meal();
                                    meal.setMealID(object.getInt("id"));
                                    meal.setMeal(object.getString("mealName"));
                                    meal.setRestaurant(object.getString("restaurantName"));
                                    meal.setRating(object.getString("mealRating"));
                                    meal.setRestaurant_latitude(parseDouble(object.getString("latitude")));
                                    meal.setRestaurant_longitude(parseDouble(object.getString("longitude")));

                                    String jsonPhoto = object.getString("photo");
                                    if(jsonPhoto != null) {
                                        Log.d(TAG, "executeGetRequest, onResponse: jsonPhoto: " + jsonPhoto);
                                        byte[] bytePhoto = null; //need to initialize it
                                        bytePhoto = Base64.decode(jsonPhoto, Base64.DEFAULT);
                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);
                                        meal.setPicture(bmp);
                                    }

                                    Log.d(TAG, "executeGetRequest, onResponse: " + jsonPhoto);

                                    meals.add(meal);
                                }
                                Log.d(TAG, "executeGetRequest, onResponse: Items: " + meals.size());
                                for (Meal m : meals) {
                                    Log.d(TAG, "executeGetRequest, onResponse: " + m.getMeal());
                                }
                                volleyCallback.onSuccess(meals);

                            } catch (JSONException e) {
                                Log.d(TAG, "executeGetRequest, onResponse Exception: " + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "executeGetRequest, onErrorResponse: " + error.getMessage());
                }
            });

            queue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executePostRequest(Meal meal,
                                          String url,
                                          Context context,
                                          VolleyCallback volleyCallback)
    {
        try {
            executeRequest(meal, url, context, volleyCallback, Request.Method.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executePutRequest(Meal meal,
                                         String url,
                                         Context context,
                                         VolleyCallback volleyCallback)
    {
        try {
            executeRequest(meal, url, context, volleyCallback, Request.Method.PUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executeDeleteRequest(Meal meal,
                                         String url,
                                         Context context,
                                         VolleyCallback volleyCallback)
    {
        try {
            executeRequest(meal, url, context, volleyCallback, Request.Method.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executeGetOneRequest(String request,
                                            Context context,
                                            VolleyCallback volleyCallback)
    {
        Log.d(TAG, "executeGetOneRequest: Start");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Meal> meals = new ArrayList<>();
        Log.d(TAG, "executeGetOneRequest: " + request);
        try {
            Log.d(TAG, "executeGetOneRequest, in 1st try");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "executeGetOneRequest, onResponse: " + response);
                            try {

                                JSONObject object = new JSONObject(response);

                                Meal meal = new Meal();
                                meal.setMealID(object.getInt("id"));
                                meal.setMeal(object.getString("mealName"));
                                meal.setRestaurant(object.getString("restaurantName"));
                                meal.setRating(object.getString("mealRating"));
                                meal.setRestaurant_latitude(parseDouble(object.getString("latitude")));
                                meal.setRestaurant_longitude(parseDouble(object.getString("longitude")));

                                String jsonPhoto = object.getString("photo");
                                if(jsonPhoto != null) {
                                    Log.d(TAG, "onResponse: jsonPhoto: " + jsonPhoto);
                                    byte[] bytePhoto = null; //need to initialize it
                                    bytePhoto = Base64.decode(jsonPhoto, Base64.DEFAULT);
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);
                                    meal.setPicture(bmp);
                                }

                                meals.add(meal);

                                Log.d(TAG, "executeGetOneRequest, onResponse: " + meal.getMeal() + " ("  + meal.getMealID() + ")");
                                Log.d(TAG, "executeGetOneRequest, onResponse: " + meals.size());
                                volleyCallback.onSuccess(meals);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "executeGetOneRequest, Error: " + error.getMessage());
                }
            });

            queue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void executeRequest(Meal meal,
                                       String url,
                                       Context context,
                                       VolleyCallback volleyCallback,
                                       int method)

    {
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JSONObject object = new JSONObject();

            object.put("id", meal.getMealID());
            object.put("mealName", meal.getMeal());
            object.put("restaurantName", meal.getRestaurant());
            object.put("mealRating", meal.getRating());
            object.put("latitude", String.valueOf(meal.getRestaurant_latitude()));
            object.put("longitude", String.valueOf(meal.getRestaurant_longitude()));

            //Update the picture
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap bmp = meal.getPicture();
            if(bmp != null)
            {
                Bitmap bitmap = Bitmap.createScaledBitmap(bmp, 200, 200, false);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String jsonPhoto = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                Log.d(TAG, "executeRequest: Ended the Photo Processing" );
                object.put("photo", jsonPhoto);
            }

            final String requestBody = object.toString();

            JsonObjectRequest request = new JsonObjectRequest(method, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: " + response);
                            volleyCallback.onSuccess(new ArrayList<Meal>());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "JSONObject, onErrorResponse: " + error.getMessage());
                }
            })
            {
                @Override
                public byte[] getBody() {
                    Log.d(TAG, "getBody: " + object.toString());
                    return object.toString().getBytes(StandardCharsets.UTF_8);
                }
            };
            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
