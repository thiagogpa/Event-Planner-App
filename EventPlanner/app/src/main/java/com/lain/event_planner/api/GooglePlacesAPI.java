package com.lain.event_planner.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class GooglePlacesAPI {
    private static GooglePlacesAPI mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private static String GOOGLE_KEY = "YOUR_KEY_HERE";


    public GooglePlacesAPI(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized GooglePlacesAPI getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GooglePlacesAPI(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void getPlacesAutocomplete(Context ctx, String query, String latitude, String longitude, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {


        RequestQueue queue = Volley.newRequestQueue(ctx);
        StringRequest sr = new StringRequest(Request.Method.GET, "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + query + "&types=establishment&location=" + latitude + "," + longitude + "&radius=50000&key=" + GOOGLE_KEY, listener, errorListener);

        queue.add(sr);
    }

    public static void getPlaceDetails(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {


        RequestQueue queue = Volley.newRequestQueue(ctx);
        StringRequest sr = new StringRequest(Request.Method.GET, "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + query + "&fields=name,geometry,formatted_address,place_id&key=" + GOOGLE_KEY, listener, errorListener);

        queue.add(sr);
    }

    public static void getAddress(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {


        RequestQueue queue = Volley.newRequestQueue(ctx);
        StringRequest sr = new StringRequest(Request.Method.GET, "https://maps.googleapis.com/maps/api/geocode/json?address=" + query + "&key=" + GOOGLE_KEY, listener, errorListener);

        queue.add(sr);
    }

    public static void getAddressByLatLong(Context ctx, String latitude, String longitude, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {


        RequestQueue queue = Volley.newRequestQueue(ctx);
        StringRequest sr = new StringRequest(Request.Method.GET, "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=" + GOOGLE_KEY, listener, errorListener);

        queue.add(sr);
    }

}

