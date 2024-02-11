package edu.lakeland.mealrater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class MealDataSource {
    private SQLiteDatabase database;
    private MealDBHelper dbHelper;
    public static final String TAG = "MealDataSource";

        public MealDataSource(Context context) {
            dbHelper = new MealDBHelper(context);
        }

        public void open() throws SQLException {
            database = dbHelper.getWritableDatabase();
        }

        public void close() {
            dbHelper.close();
        }

        public boolean insertMeal(Meal m) {
            boolean didSucceed = false;
            try {
                ContentValues initialValues = new ContentValues();

                initialValues.put("meal", m.getMeal());
                initialValues.put("restaurant", m.getRestaurant());
                initialValues.put("rating", m.getRating());
                initialValues.put("restaurant_latitude", m.getRestaurant_latitude());
                initialValues.put("restaurant_longitude", m.getRestaurant_longitude());
                if (m.getPicture() != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    m.getPicture().compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] photo = baos.toByteArray();
                    initialValues.put("mealPhoto", photo);
                }

                didSucceed = database.insert("mealRatings", null, initialValues) > 0;
                Log.d(TAG, "insertMeal: " + didSucceed);
            }
            catch (Exception e) {
                //Do nothing -will return false if there is an exception
            }
            return didSucceed;
        }

        public boolean updateMeal(Meal m) {
            boolean didSucceed = false;
            try {
                Integer rowId = m.getMealID();
                ContentValues updateValues = new ContentValues();

                updateValues.put("meal", m.getMeal());
                updateValues.put("restaurant", m.getRestaurant());
                updateValues.put("rating", m.getRating());

                didSucceed = database.update("contact", updateValues, "_id=" + rowId, null) > 0;
            }
            catch (Exception e) {
                //Do nothing -will return false if there is an exception
            }
            return didSucceed;
        }

    public Meal getSpecificMeal(int mealID) {
        Meal meal = new Meal();
        String query = "SELECT * FROM mealRatings WHERE _id =" + mealID;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            meal.setMealID(cursor.getInt(0));
            meal.setMeal(cursor.getString(1));
            meal.setRestaurant(cursor.getString(2));
            meal.setRating(cursor.getString(3));
            meal.setRestaurant_latitude(cursor.getDouble(4));
            meal.setRestaurant_longitude(cursor.getDouble(5));

            byte[] photo = cursor.getBlob(6);
            if (photo != null) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                Bitmap thePicture= BitmapFactory.decodeStream(imageStream);
                meal.setPicture(thePicture);
            }
            cursor.close();
        }
        return meal;
    }

        public int getLastMealID() {
            int lastId;
            try {
                String query = "Select MAX(_id) from contact";
                Cursor cursor = database.rawQuery(query, null);

                cursor.moveToFirst();
                lastId = cursor.getInt(0);
                cursor.close();
            }
            catch (Exception e) {
                lastId = -1;
            }
            return lastId;
        }

    public ArrayList<Meal> getMeals(String sortField, String sortOrder) {
        ArrayList<Meal> mealList = new ArrayList<Meal>();
        try {
            String query = "SELECT * FROM mealRatings ORDER BY " + sortField + " " + sortOrder;
            Log.d(TAG, "getMeals: " + query);
            Cursor cursor = database.rawQuery(query, null);

            Meal newMeal;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newMeal = new Meal();
                newMeal.setMealID(cursor.getInt(0));
                newMeal.setMeal(cursor.getString(1));
                newMeal.setRestaurant(cursor.getString(2));
                newMeal.setRating(cursor.getString(3));
                newMeal.setRestaurant_latitude(cursor.getDouble(4));
                newMeal.setRestaurant_longitude(cursor.getDouble(5));
                byte[] photo = cursor.getBlob(6);
                if (photo != null) {
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                    Bitmap thePicture= BitmapFactory.decodeStream(imageStream);
                    newMeal.setPicture(thePicture);
                }
                mealList.add(newMeal);
                cursor.moveToNext();
            }
            cursor.close();
            Log.d(TAG, "getMeals: " + mealList.size());
        }
        catch (Exception e) {
            mealList = new ArrayList<Meal>();
        }
        return mealList;
    }

    public ArrayList<String> getMealName() {
        ArrayList<String> mealNames = new ArrayList<>();
        try {
            String query = "Select meal from mealRatings";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mealNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
            Log.d(TAG, "getMeals: " + mealNames.size());
        }
        catch (Exception e) {
            mealNames = new ArrayList<String>();
        }
        return mealNames;
    }

    public boolean deleteMeal(int mealID) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("mealRatings", "_id=" + mealID, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

}
