package edu.lakeland.mealrater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RatingListActivity extends AppCompatActivity {
    public static final String TAG = "RatingsListActivity";
    ArrayList<Meal> meals;
    MealAdaptor mealAdaptor;
    public static final String MEALAPI = "https://fvtcdp.azurewebsites.net/api/MealRating/";

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int mealID = meals.get(position).getMealID();
            Intent intent = new Intent(RatingListActivity.this, MainActivity.class);
            intent.putExtra("mealID", mealID);
            startActivity(intent);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_list);
        initRateAMealButton();

//        MealDataSource ds = new MealDataSource(this);
//
//        String sortBy = "restaurant";
//        String sortOrder = "ASC";

        try {
//            ds.open();
//            contacts = ds.getContacts(sortBy, sortOrder);
//            ds.close();

            RestClient.executeGetRequest(MEALAPI, this, new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Meal> result) {
                            Log.d(TAG, "onSuccess: Meal : " + result.size());
                            meals = result;
                            Log.d(TAG, "onCreate: meals size1 = " + meals.size());
                            if (meals.size() > 0) {
                                RecyclerView mealList = findViewById(R.id.rvMeals);

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RatingListActivity.this);
                                mealList.setLayoutManager(layoutManager);

                                mealAdaptor = new MealAdaptor(meals, RatingListActivity.this);
                                mealAdaptor.setOnItemClickListener(onItemClickListener);
                                mealList.setAdapter(mealAdaptor);
                                Log.d(TAG, "onSuccess End");
                            }
                            else {
                                Intent intent = new Intent(RatingListActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                        }
                    });
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
            Log.d(TAG, "onCreate: " + e.getMessage(), e);
        }

    }

//        try {
//            ds.open();
//            meals = ds.getMeals(sortBy, sortOrder);
//            ds.close();
//            if (meals.size() > 0) {
//                RecyclerView mealList = findViewById(R.id.rvMeals);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//                mealList.setLayoutManager(layoutManager);
//                mealAdaptor = new MealAdaptor(meals, this);
//                mealAdaptor.setOnItemClickListener(onItemClickListener);
//                mealList.setAdapter(mealAdaptor);
//            }
//            else {
//                Intent intent = new Intent(RatingListActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        }
//        catch (Exception e) {
//            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
//        }


    private void initRateAMealButton() {
        Button btnRateAMeal = findViewById(R.id.btnRateAMeal);
        btnRateAMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RatingListActivity.this, MainActivity.class));
            }
        });
    }
}
