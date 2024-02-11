package edu.lakeland.mealrater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RateMealDialogueFragment.SaveDateListener, OnMapReadyCallback {
    public static final String TAG = "MainActivity";
    Meal currentMeal;
    final int PERMISSION_REQUEST_CAMERA = 103;
    final int CAMERA_REQUEST = 1888;
    final int MAP_REQUEST = 0;
    GoogleMap gMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTextChangedEvents();
        initRateButton();
        initSaveButton();
        initImageButton();
        initViewRatingsListButton();
        initViewRestaurantLocationButton();
        currentMeal = new Meal();

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            initMeal(extras.getInt("mealID"));
        } else {
            currentMeal = new Meal();
        }
    }

    private void initMeal(int mealID) {
        MealDataSource ds = new MealDataSource(MainActivity.this);
        try {
//            ds.open();
//            currentMeal = ds.getSpecificMeal(mealID);
//            ds.close();
            Log.d(TAG, "initMeal: " + mealID);
            getOneMeal(mealID);
            Log.d(TAG, "initMeal: " + currentMeal.getMeal());
        }
        catch (Exception e) {
            Toast.makeText(this, "Load Meal Failed", Toast.LENGTH_LONG).show();
            Log.d(TAG, "database exception = " + e.getMessage());
        }
    }

    private void getOneMeal(int id){
        {
            try {
                Log.d(TAG, "getOneMeal: " + RatingListActivity.MEALAPI + id);
                RestClient.executeGetOneRequest(RatingListActivity.MEALAPI + id, this, new VolleyCallback(){
                    @Override
                    public void onSuccess(ArrayList<Meal> result){
                        //do stuff here
                        for (Meal m : result) {
                            Log.d(TAG, "onResume: " + m.getMeal());
                        }
                        currentMeal= result.get(0);
                        RebindScreen();
                    }
                });

                Log.d(TAG, "getOneMeal: Meals End");

            }
            catch (Exception e) {
                Log.d(TAG, "getOneMeal: " + e.getMessage());
                Toast.makeText(this, "Error retrieving meals", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void RebindScreen()
    {
        EditText etRestaurant = findViewById(R.id.etRestaurant);
        EditText etDish = findViewById(R.id.etDish);
        TextView tvRating = findViewById(R.id.tvRatingValue);
        TextView tvLatitude = findViewById(R.id.tvMainLatitude);
        TextView tvLongitude = findViewById(R.id.tvMainLongitude);

        etRestaurant.setText(currentMeal.getRestaurant());
        etDish.setText(currentMeal.getMeal());
        tvRating.setText(currentMeal.getRating());
        tvLatitude.setText(currentMeal.getRestaurant_latitude().toString());
        tvLongitude.setText(currentMeal.getRestaurant_longitude().toString());

        ImageButton picture = findViewById(R.id.imageMeal);
        if (currentMeal.getPicture() != null) {
            Log.d(TAG, "RebindScreen: Picture not null" );
            picture.setImageBitmap(currentMeal.getPicture());
        }
        else {
            Log.d(TAG, "RebindScreen: Picture  null" );
            picture.setImageResource(R.drawable.noimage);
        }

        refreshMap();

    }

    private void refreshMap(){
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);
    }

    private void initTextChangedEvents() {
        final EditText etRestaurant = findViewById(R.id.etRestaurant);
        etRestaurant.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                currentMeal.setRestaurant(etRestaurant.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etDish = findViewById(R.id.etDish);
        etDish.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentMeal.setMeal(etDish.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final TextView tvRatingValue = findViewById(R.id.tvRatingValue);
        tvRatingValue.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                currentMeal.setRating(tvRatingValue.getText().toString());
            }
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });

        final TextView tvMainLatitude = findViewById(R.id.tvMainLatitude);
        tvMainLatitude.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                double lat = Double.parseDouble(tvMainLatitude.getText().toString());
                currentMeal.setRestaurant_latitude(lat);
            }
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });

        final TextView tvMainLongitude = findViewById(R.id.tvMainLongitude);
        tvMainLongitude.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                double longitude = Double.parseDouble(tvMainLongitude.getText().toString());
                currentMeal.setRestaurant_longitude(longitude);
            }
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });
    }

    private void initRateButton() {
        Button rate = findViewById(R.id.btnRateThisMeal);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                RateMealDialogueFragment rm = new RateMealDialogueFragment();
                rm.show(fm, "rate meal");
                TextView tv = v.findViewById(R.id.tvRatingValue);
            }
        });
    }

    private void initImageButton() {
        ImageButton ib = findViewById(R.id.imageMeal);
        ib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.CAMERA)) {
                            Snackbar.make(findViewById(R.id.activity_main), "The app needs permission to take pictures.", Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                                        }
                                    })
                                    .show();
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                        }
                    }
                    else {
                        takePhoto();
                    }
                } else {
                    takePhoto();
                }
            }
        });
    }

    public void takePhoto(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(MainActivity.this, "You will not be able to save contact pictures from this app", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public static final String TAG = "MainActivity";
            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                hideKeyboard();
//                MealDataSource mds = new MealDataSource(MainActivity.this);
                try {
//                    mds.open();

                    if (currentMeal.getMealID() == -1) {
                        RestClient.executePostRequest(currentMeal,
                                RatingListActivity.MEALAPI,
                                MainActivity.this,
                                new VolleyCallback() {
                                    @Override
                                    public void onSuccess(ArrayList<Meal> result) {
                                        currentMeal.setMealID(result.get(0).getMealID());
                                        Log.d(TAG, "onSuccess: " + currentMeal.getMealID());
                                    }
                                });

                    }
                    else {
                        RestClient.executePutRequest(currentMeal,
                                RatingListActivity.MEALAPI + currentMeal.getMealID(),
                                MainActivity.this,
                                new VolleyCallback() {
                                    @Override
                                    public void onSuccess(ArrayList<Meal> result) {
                                        Log.d(TAG, "onSuccess: " + currentMeal.getMealID());
                                    }
                                });
                    }
//                    mds.close();
                }
                catch (Exception e) {
                    wasSuccessful = false;
                }
            }
        });
    }

    private void initViewRatingsListButton() {
        Button ratingsList = findViewById(R.id.btnRatingList);
        ratingsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RatingListActivity.class));
            }
        });
    }

    private void initViewRestaurantLocationButton() {
        Button btnRestaurantLocation = findViewById(R.id.btnViewLocation);
        btnRestaurantLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etRestaurant = findViewById(R.id.etRestaurant);
                Intent intent = new Intent(MainActivity.this, RestaurantLocationActivity.class);
                intent.putExtra("restaurant", etRestaurant.getText().toString());
                startActivityForResult(intent,MAP_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MAP_REQUEST){
            if (resultCode == RESULT_OK){
                double latitude = data.getDoubleExtra("latitude", 0.0);
                currentMeal.setRestaurant_latitude(latitude);
                TextView tvMainLatitude = findViewById(R.id.tvMainLatitude);
                tvMainLatitude.setText(currentMeal.getRestaurant_latitude().toString());

                double longitude = data.getDoubleExtra("longitude", 0.0);
                currentMeal.setRestaurant_longitude(longitude);
                TextView tvMainLongitude = findViewById(R.id.tvMainLongitude);
                tvMainLongitude.setText(currentMeal.getRestaurant_longitude().toString());

                refreshMap();
            }
        }

        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 144, 144, true);
                ImageButton imageContact = findViewById(R.id.imageMeal);
                imageContact.setImageBitmap(scaledPhoto);
                currentMeal.setPicture(scaledPhoto);
            }
        }
    }

    @Override
    public void didFinishDatePickerDialog(Float rating) {
        TextView tvSelectedDate = findViewById(R.id.tvRatingValue);
        tvSelectedDate.setText(String.valueOf(rating));

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText etRestaurant = findViewById(R.id.etRestaurant);
        imm.hideSoftInputFromWindow(etRestaurant.getWindowToken(), 0);
        EditText etDish = findViewById(R.id.etDish);
        imm.hideSoftInputFromWindow(etDish.getWindowToken(), 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size);
        int measuredWidth = size.x;
        int measuredHeight = size.y;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng point = new LatLng(currentMeal.getRestaurant_latitude(), currentMeal.getRestaurant_longitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));

        gMap.addMarker(new MarkerOptions().position(point).title(currentMeal.getRestaurant()));
        builder.include(point);

        gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), measuredWidth, measuredHeight, 450));

    }

}