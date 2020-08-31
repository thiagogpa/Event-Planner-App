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
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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

public class EstablishmentActivity extends Base implements OnMapReadyCallback {
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

    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gets the event through the received intent
        Bundle data = getIntent().getExtras();
        currentEvent = data.getParcelable(getString(R.string.INTENT_KEY));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment);


        //Checks if the user has accepted to share the location, and waits for the respose, so it will focus on his location instead of a random place
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            getUserLocation();
        }

        businessDetailsLayout = findViewById(R.id.layoutBusinessDetails);

        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.actv_input);
        final TextView txtestablishmentName = findViewById(R.id.txtestablishmentName);
        final TextView txtaddress = findViewById(R.id.txtaddress);

        currentEvent.setEventLocation(null);
        businessDetailsLayout.setVisibility(View.GONE);

        autoSuggestAdapter = new AutoSuggestAdapter(this, android.R.layout.simple_dropdown_item_1line);

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {

                        //Sets a flag so it will only plot this specific place on the map
                        didClick = true;

                        if (!deniedAccess)
                            mMap.clear();

                        try {
                            JSONObject selectedBusinessJSON = googlePredictions.getJSONObject(position);
                            EventLocation selectedBusiness = businessHashMap.get(selectedBusinessJSON.getString("place_id"));

                            txtestablishmentName.setText(autoSuggestAdapter.getObject(position));
                            txtaddress.setText(selectedBusiness.getFormatted_address());


                            if (!deniedAccess) {
                                LatLng business = new LatLng(selectedBusiness.getLatitude(), selectedBusiness.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(business).title(selectedBusiness.getName()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(business, 16));
                            }

                            currentEvent.setEventLocation(selectedBusiness);
                            businessDetailsLayout.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }




                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
                //It takes the business details of the screen, so the user can see only the location of the business that match his search
                businessDetailsLayout.setVisibility(View.GONE);
                currentEvent.setEventLocation(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        //Calls the api using the user input
                        callGooglePlaces(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
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


    private void callGooglePlaces(String text) {
        //Checks if the user didnt just clicked on an item, which means the information doesn't need to be retrieved again
        if (!didClick && !deniedAccess) {
            //Calls the autocomplete to get the list of business based on the user input
            GooglePlacesAPI.getPlacesAutocomplete(this, text, currentLatitude.toString(), currentLongitude.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    List<String> stringList = new ArrayList<>();
                    businessHashMap = new HashMap<>();

                    try {
                        JSONObject responseObject = new JSONObject(response);
                        googlePredictions = responseObject.getJSONArray("predictions");

                        //Clear maps so only the ones that match the user input, appear on the map
                        mMap.clear();


                        for (int i = 0; i < googlePredictions.length(); i++) {
                            JSONObject business = googlePredictions.getJSONObject(i);

                            stringList.add(business.getString("description"));
                            EventLocation newBusiness = new EventLocation(business.getString("place_id"),business.getString("description"));
                            businessHashMap.put(newBusiness.getPlace_id(), newBusiness);


                            //Gets the details of the current business
                            GooglePlacesAPI.getPlaceDetails(getApplicationContext(), business.getString("place_id"), new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject responseObject = new JSONObject(response);
                                        JSONObject businessDetails = responseObject.getJSONObject("result");
                                        JSONObject location= businessDetails.getJSONObject("geometry").getJSONObject("location");

                                        EventLocation currentBusiness = businessHashMap.get(businessDetails.get("place_id").toString());
                                        currentBusiness.setFormatted_address(businessDetails.get("formatted_address").toString());
                                        currentBusiness.setLatitude(location.getDouble("lat"));
                                        currentBusiness.setLongitude(location.getDouble("lng"));

                                        LatLng businessLocation = new LatLng(currentBusiness.getLatitude(), currentBusiness.getLongitude());
                                        mMap.addMarker(new MarkerOptions().position(businessLocation).title(businessDetails.get("name").toString()));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(businessLocation, 10));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                        }


                        autoSuggestAdapter.setData(stringList);
                        autoSuggestAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        }
        //Sets the flag to false, so it loops later on
        didClick = false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Creates the map based on the current location of the user
        mMap = googleMap;
        LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
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