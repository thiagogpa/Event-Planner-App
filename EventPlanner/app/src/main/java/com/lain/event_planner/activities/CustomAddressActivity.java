package com.lain.event_planner.activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lain.event_planner.R;
import com.lain.event_planner.adapters.AutoSuggestAdapter;
import com.lain.event_planner.api.GooglePlacesAPI;
import com.lain.event_planner.classes.Event;
import com.lain.event_planner.classes.EventLocation;
import com.lain.event_planner.common.Base;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomAddressActivity extends Base implements OnMapReadyCallback {
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 100;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Double currentLatitude, currentLongitude;
    JSONArray googlePredictions;
    boolean didClick = false;
    ConstraintLayout businessDetailsLayout;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private GoogleMap mMap;
    private boolean deniedAccess = false;
    HashMap<String, EventLocation> businessHashMap = new HashMap<>();
    EditText edtAddress;
    TextView txtaddress;
    EventLocation customPlace = null;

    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gets the event through the received intent
        Bundle data = getIntent().getExtras();
        currentEvent = data.getParcelable(getString(R.string.INTENT_KEY));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_address);

//Checks if the user has accepted to share the location, and waits for the respose, so it will focus on his location instead of a random place
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            getUserLocation();
        }

        businessDetailsLayout = findViewById(R.id.layoutBusinessDetails);

        txtaddress = findViewById(R.id.txtaddress);
        edtAddress = findViewById(R.id.edtAddress);


        currentEvent.setEventLocation(null);
        businessDetailsLayout.setVisibility(View.GONE);


        edtAddress.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentEvent.setEventLocation(null);
                businessDetailsLayout.setVisibility(View.GONE);
            }
        });
    }


    //Necessary in case the app doest have the permissions yet. Otherwise it would not be able to use the map on the first run
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation();
                } else {
                    // permission denied, send message to the user
                    Toast.makeText(this, "Permission was denied... Sorry you wont be able to use the app", Toast.LENGTH_SHORT).show();
                    deniedAccess = true;
                }
            }
        }
    }

    //Gets the user location GPS, and points the map towards that point
    private void getUserLocation() {
        Location locationGPS = getLastKnownLocation();
        if (locationGPS != null) {
            double lat = locationGPS.getLatitude();
            double longi = locationGPS.getLongitude();
            currentLatitude = Double.valueOf(lat);
            currentLongitude = Double.valueOf(longi);
            Log.i("TAG", "Your Location: " + "\n" + "Latitude: " + currentLatitude + "\n" + "Longitude: " + currentLongitude);
        } else {
            Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            deniedAccess = true;
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
    }


    private Location getLastKnownLocation() {
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Creates the map based on the current location of the user
        mMap = googleMap;
        LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                mMap.clear();
                customPlace = null;
                LatLng marker = new LatLng(point.latitude, point.longitude);
                mMap.addMarker(new MarkerOptions().position(marker));


                GooglePlacesAPI.getAddressByLatLong(getApplicationContext(), Double.toString(point.latitude), Double.toString(point.longitude), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<String> stringList = new ArrayList<>();
                        try {

                            JSONObject responseObject = new JSONObject(response);

                            JSONArray googlePredictions = responseObject.getJSONArray("results");
                            JSONObject location= googlePredictions.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

                            customPlace = new EventLocation();
                            customPlace.setFormatted_address(googlePredictions.getJSONObject(0).getString("formatted_address"));
                            customPlace.setLatitude(googlePredictions.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                            customPlace.setLongitude(googlePredictions.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                            txtaddress.setText(customPlace.getFormatted_address());

                            //Saves the selected location in the event
                            currentEvent.setEventLocation(customPlace);

                            businessDetailsLayout.setVisibility(View.VISIBLE);


                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "There was no address match to your search", Toast.LENGTH_SHORT).show();
                            currentEvent.setEventLocation(null);
                            businessDetailsLayout.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", "onErrorResponse: error");
                    }
                });
            }
        });
    }

    public void searchAddress(View view) {
        if(edtAddress.getText().toString().matches(""))
            return;

        customPlace = null;

        GooglePlacesAPI.getAddress(getApplicationContext(), edtAddress.getText().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject responseObject = new JSONObject(response);

                    JSONArray googlePredictions = responseObject.getJSONArray("results");
                    JSONObject location= googlePredictions.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

                    customPlace = new EventLocation();
                    customPlace.setFormatted_address(googlePredictions.getJSONObject(0).getString("formatted_address"));
                    customPlace.setLatitude(googlePredictions.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                    customPlace.setLongitude(googlePredictions.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));


                    if (!deniedAccess) {
                        LatLng businessLocation = new LatLng(customPlace.getLatitude(), customPlace.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(businessLocation));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(businessLocation, 16));
                    }

                    txtaddress.setText(customPlace.getFormatted_address());

                    //Saves the current location on the event
                    currentEvent.setEventLocation(customPlace);

                    businessDetailsLayout.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    customPlace = null;
                    currentEvent.setEventLocation(null);
                    businessDetailsLayout.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "There was no address match to your search", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    //This is called when the user clicks on the Arrow button, to go to the next screen
    public void NextActivity(View view) {
        if(currentEvent.getEventLocation() == null){
            Toast.makeText(getApplicationContext(), "Please select a valid address for the event", Toast.LENGTH_SHORT).show();
            return;
        }

        //Creates a new intent, puts the event object in it, and calls the next activity
        Intent summaryActivity = new Intent(getApplicationContext(), SummaryActivity.class);
        summaryActivity.putExtra(getString(R.string.INTENT_KEY), currentEvent);
        startActivity(summaryActivity);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }
}