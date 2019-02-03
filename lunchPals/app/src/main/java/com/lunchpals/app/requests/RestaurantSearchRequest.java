package com.lunchpals.app.requests;

import android.location.Location;
import android.util.Log;

import com.lunchpals.app.activities.main.location.MapActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RestaurantSearchRequest {

    private static final String TAG = "RestaurantSearchReq";

    public static final String RESTAURANTS = "Restaurants";

    public static final String TERM = "term";
    public static final String OFFSET = "offset";
    public static final String LIMIT = "limit";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String RADIUS = "radius";
    public static final String CATEGORIES = "categories";
    public static final String SORT = "sort";
    public static final String PRICE = "price";
    private static final String LOCATION = "location";

    private static HashMap<String, String> filters = new HashMap<>();

    public static final String SORT_REAL_DISTANCE = "real_distance";
    public static final String SORT_COST = "cost";
    public static final String SORT_RATING = "rating";

    public static final String ASC = "asc";
    public static final String DESC = "desc";

    private static final String MAX_RESPONSE_NR = "" + getLimit();

    public static void setFilter(String key, String value){
        if (key == TERM){
            value = RESTAURANTS + " " + value;
        }
        filters.put(key, value);
    }

    public static void removeFilter(String key){
        filters.remove(key);
    }

    public static Map<String, String> getFilters() {
        if(!filters.containsKey(TERM)){
            filters.put(TERM, RESTAURANTS);
        }
        else if (filters.containsKey(LOCATION)){
            filters.remove(LOCATION);
        }
        if(!filters.containsKey(LIMIT)){
            filters.put(LIMIT, MAX_RESPONSE_NR);
        }else if(filters.containsKey(LIMIT)){
            filters.remove(LIMIT);
            filters.put(LIMIT, MAX_RESPONSE_NR);
        }
        if (!filters.containsKey(LONGITUDE) || !filters.containsKey(LATITUDE)){
            filters.put(LOCATION, "San Francisco CA");
        }
        return filters;
    }

    public static int getGroupSize(){
        return 3;
    }

    public static int getLimit() {
        return 5;
    }

    public static void setCurrentLocation(Location currentLocation) {
        Log.d(TAG, "putting latitude and longitude: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());
        filters.put(LONGITUDE, ""+currentLocation.getLongitude());
        filters.put(LATITUDE, ""+currentLocation.getLatitude());
    }
}
