package com.example.event_planner.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.event_planner.common.Base;
import com.example.event_planner.R;
import com.example.event_planner.api.API;
import com.example.event_planner.classes.Event;
import com.example.event_planner.classes.Product;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GuestActivity extends Base {

    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gets the event through the received intent
        Bundle data = getIntent().getExtras();
        currentEvent = data.getParcelable(getString(R.string.INTENT_KEY));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests);

        //Creates variables for each of the elements on the screen
        IndicatorSeekBar indSeekBarMen = findViewById(R.id.indSeekBarMen);
        IndicatorSeekBar indSeekBarWomen = findViewById(R.id.indSeekBarWomen);
        IndicatorSeekBar indSeekBarChildren = findViewById(R.id.indSeekBarChildren);
        final TextView txvTotal = findViewById(R.id.txvTotal);

        LinearLayout linearChildren = findViewById(R.id.linearChildren);
        //If the event is a Business meeting, there is no point showing the kids linear layout
        //So it is removed from the view
        if (currentEvent.getEventType().equals(Event.EventType.BUSINESS_MEETING)){
            linearChildren.setVisibility(View.GONE);
        }

        //Treats the behaviors of the Seek element
        indSeekBarMen.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                currentEvent.getGuests().setQtyMen(seekParams.progress);
                txvTotal.setText(String.valueOf(currentEvent.getGuests().getTotal()));
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });

        //Treats the behaviors of the Seek element
        indSeekBarWomen.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                currentEvent.getGuests().setQtyWomen(seekParams.progress);
                txvTotal.setText(String.valueOf(currentEvent.getGuests().getTotal()));
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });

        //Treats the behaviors of the Seek element
        indSeekBarChildren.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                currentEvent.getGuests().setQtyChildren(seekParams.progress);
                txvTotal.setText(String.valueOf(currentEvent.getGuests().getTotal()));
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
    }

    //This is called when the user clicks on the Arrow button, to go to the next screen
    public void NextActivity(View view) {

        //Check if there are any guests on the current event, it only goes to the next screen case there are 1 or more
        if(currentEvent.getGuests().getTotal() > 0){

            //Pops a dialog with a loading message while it waits for the API response
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            //Clear any Products that might be loaded on the event already
            currentEvent.getProducts().clear();

            //Creates the request for the API informing the user input as the parameters
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    API.searchURL(currentEvent.getEventType().toString(),
                            currentEvent.getGuests().getQtyMen(),
                            currentEvent.getGuests().getQtyWomen(),
                            currentEvent.getGuests().getQtyChildren()), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Once it gets the response, it tries to parse the data into the expected layout
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("records");

                        //Loops trough the records creating JSON objects for each of them
                        for (int i = 0; i < array.length(); i++){

                            JSONObject jo = array.getJSONObject(i);
                            //Creates a new Product object, using the data of the parsed JSON Object
                            Product productList = new Product(jo.getInt("id"),
                                    jo.getString("name"),
                                    jo.getDouble("price"),
                                    jo.getInt("quantity"),
                                    jo.getString("category_name"),
                                    jo.getBoolean("selected"),
                                    jo.getString("image_url")
                            );
                            //Adds the new Product to the event product list
                            currentEvent.getProducts().add(productList);
                        }

                        //Creates a new intent, puts the event object in it, and calls the next activity
                        Intent tabActivity = new Intent(getApplicationContext(), ProductsActivity.class);
                        tabActivity.putExtra(getString(R.string.INTENT_KEY), currentEvent);
                        startActivity(tabActivity);
                        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

                    } catch (JSONException e) {
                        //Case there`s a problem while parsing, show an error message with the return
                        Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("GuestActivity", e.toString());
                        e.printStackTrace();
                    } finally {
                        //When it is done parsing, dismisses the Dialog with the loading message
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            //Creates a new Volley (asynchronous response) with the API request
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } else  {
            //Case there are not enough guests, it sends a message to the user to provide the correct number of guests
            Toast.makeText(GuestActivity.this, "Please inform the number of guests", Toast.LENGTH_SHORT).show();
        }

    }

}
