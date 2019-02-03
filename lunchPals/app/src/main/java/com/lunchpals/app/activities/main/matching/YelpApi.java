package com.lunchpals.app.activities.main.matching;

import android.util.Log;

import com.lunchpals.app.requests.RestaurantSearchRequest;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YelpApi {

    private static final String TAG = "RestaurantList";

    private static YelpFusionApi yelpFusionApi;

    public static void setYelpFusionApi(YelpFusionApi yelpFusionApi) {
        YelpApi.yelpFusionApi = yelpFusionApi;
    }

    public static void searchYelpApi() {
        if(hasYelpFusionApi()) {
            try {

                Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(RestaurantSearchRequest.getFilters());

                Callback<SearchResponse> callback = new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        SearchResponse searchResponse = response.body();

                        Log.d(TAG, "Yelp API request: " + call.request().url());
                        Log.d(TAG, "Yelp API response has " + searchResponse.getBusinesses().size() + " businesses.");
//
//                        if(searchResponse.getBusinesses().size() > RestaurantSearchRequest.getLimit()){
//                            ArrayList<Business> businesses = new ArrayList<>();
//                            for(int i = 0; i < RestaurantSearchRequest.getLimit(); i++){
//                                businesses.add(searchResponse.getBusinesses().get(i));
//                            }
//                            searchResponse.setBusinesses(businesses);
//                            Log.d(TAG, "Reset response. Now has " + searchResponse.getBusinesses().size() + " businesses.");
//                        }

                        SearchingMatch.startSearch(searchResponse);
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {

                        Log.d(TAG, "FAILED");
                        Log.d(TAG, call.request().url().toString());

                    }
                };

                call.enqueue(callback);

            } catch (NullPointerException e) {
                Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
            }
        }else{
            Log.d(TAG, "Need to load yelpFusion first...");
        }

    }

    public static void searchYelpApiForRestaurant(String restaurantKey){
        if(hasYelpFusionApi()) {
            try {

                Call<Business> call = yelpFusionApi.getBusiness(restaurantKey);

                Callback<Business> callback = new Callback<Business>() {
                    @Override
                    public void onResponse(Call<Business> call, Response<Business> response) {
                        Business business = response.body();
                        Log.d(TAG, business.getName());
                        ShowMatch.setRestaurant(response.body());
                    }

                    @Override
                    public void onFailure(Call<Business> call, Throwable t) {

                        Log.d(TAG, "FAILED");
                        Log.d(TAG, call.request().url().toString());

                    }
                };

                call.enqueue(callback);

            } catch (NullPointerException e) {
                Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
            }
        }else{
            Log.d(TAG, "Need to load yelpFusion first...");
        }
    }

    public static boolean hasYelpFusionApi(){
        return yelpFusionApi != null;
    }

}

