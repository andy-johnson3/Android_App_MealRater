package edu.lakeland.mealrater;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantLocationActivity extends AppCompatActivity {
    public static final String TAG = "RestaurantLocationActivity";
    LocationManager locationManager;
    LocationListener gpsListener;
    final int PERMISSION_REQUEST_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_location);

        initRestaurantTV();
        initLookupButton();
        initFromMyLocationButton();
        initFindButton();
        initSaveButton();
    }

    private void initRestaurantTV(){
        TextView tvRestaurant = findViewById(R.id.tvRestaurant);
        Bundle extras = getIntent().getExtras();
        tvRestaurant.setText(extras.getString("restaurant"));
    }

    private void initLookupButton() {
        Button btnLookup = findViewById(R.id.btnLookup);
        btnLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView editAddress = findViewById(R.id.editAddress);
                hide(editAddress);

                TextView editCity = findViewById(R.id.editCity);
                hide(editCity);

                TextView editState = findViewById(R.id.editState);
                hide(editState);

                TextView editZipcode = findViewById(R.id.editZipcode);
                hide(editZipcode);

                Button btnFind = findViewById(R.id.btnFind);
                if (btnFind.getVisibility() == View.VISIBLE)
                    btnFind.setVisibility(View.INVISIBLE);
                else
                    btnFind.setVisibility(View.VISIBLE);
            }

            private void hide(TextView tv){
                if (tv.getVisibility() == View.VISIBLE)
                    tv.setVisibility(View.INVISIBLE);
                else
                    tv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initFindButton(){
        Button btnFind = findViewById(R.id.btnFind);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable GPS Sensor in case it's activated
                try {
                    locationManager.removeUpdates(gpsListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                EditText editAddress = findViewById(R.id.editAddress);
                EditText editCity = findViewById(R.id.editCity);
                EditText editState = findViewById(R.id.editState);
                EditText editZipcode = findViewById(R.id.editZipcode);

                String address = editAddress.getText().toString() + ", " +
                        editCity.getText().toString() + ", " +
                        editState.getText().toString() + ", " +
                        editZipcode.getText().toString();

                List<Address> addresses = null;
                Geocoder geo = new Geocoder(RestaurantLocationActivity.this);
                try {
                    addresses = geo.getFromLocationName(address, 1);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                TextView tvLatitude = findViewById(R.id.tvLatitude);
                TextView tvLongitude = findViewById(R.id.tvLongitude);

                tvLatitude.setText(String.valueOf(addresses.get(0).getLatitude()));
                tvLongitude.setText(String.valueOf(addresses.get(0).getLongitude()));
            }
        });
    }

    private void initFromMyLocationButton() {
        Button btnFromMyLoc = findViewById(R.id.btnFromMyLoc);
        btnFromMyLoc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(RestaurantLocationActivity.this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(RestaurantLocationActivity.this,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                                Snackbar.make(findViewById(R.id.activity_restaurant_location),
                                                "MyContactList requires this permission to locate " +
                                                        "your contacts", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ActivityCompat.requestPermissions(
                                                        RestaurantLocationActivity.this,
                                                        new String[]{
                                                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                                                        PERMISSION_REQUEST_LOCATION);

                                            }
                                        }).show();
                            } else {
                                ActivityCompat.requestPermissions(
                                        RestaurantLocationActivity.this,
                                        new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSION_REQUEST_LOCATION);
                            }
                        } else {
                            startLocationUpdates();
                        }
                    } else {
                        startLocationUpdates();
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Error requesting permission", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d(TAG, "startLocationUpdates: onLocationChanged: ");
                    TextView txtLatitude = findViewById((R.id.tvLatitude));
                    TextView txtLongitude = findViewById((R.id.tvLongitude));

                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtLongitude.setText(String.valueOf(location.getLongitude()));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ActivityCompat.checkSelfPermission(RestaurantLocationActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(RestaurantLocationActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, gpsListener);
        }
        catch(Exception e)
        {
            Toast.makeText(getBaseContext(), "Error, Location not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(gpsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSaveButton(){
        Button saveButton = findViewById(R.id.btnSaveLocation);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvLatitude = findViewById(R.id.tvLatitude);
                TextView tvLongitude = findViewById(R.id.tvLongitude);
                double latitude = Double.parseDouble(tvLatitude.getText().toString());
                double longitude = Double.parseDouble(tvLongitude.getText().toString());
                Intent resultIntent = new Intent(RestaurantLocationActivity.this, MainActivity.class);
                resultIntent.putExtra("latitude", latitude);
                resultIntent.putExtra("longitude", longitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

}
