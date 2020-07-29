package com.example.event_planner.api;

//This class handles the API calling to retrieve a JSON Array
public class API {

    //Method used to build the full path to access the API
    public static String searchURL(String eventType,int qtyMen, int qtyWomen, int qtyChildren){
        String search = "search.php";
        String path = "https://thiagogpa.000webhostapp.com/api/product/";
        String URL_DATA = path + search +
                "?event=" + eventType +
                "&qtyMen=" + qtyMen +
                "&qtyWomen=" + qtyWomen +
                "&qtyChildren=" + qtyChildren;
        return URL_DATA;
    }
}
