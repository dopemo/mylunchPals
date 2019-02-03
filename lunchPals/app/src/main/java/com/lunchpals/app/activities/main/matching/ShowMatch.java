package com.lunchpals.app.activities.main.matching;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.lunchpals.app.R;
import com.lunchpals.app.activities.main.MainActivity;
import com.lunchpals.app.activities.main.MainFragment;
import com.lunchpals.app.activities.main.location.LocationMap;
import com.lunchpals.app.activities.main.location.MapActivity;
import com.lunchpals.app.user.UserInformation;
import com.lunchpals.app.utils.DownloadImageTask;
import com.lunchpals.app.utils.IndexedHashMap;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Category;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by moritz on 11/13/17.
 */

public class ShowMatch extends MainFragment {

    private static Business restaurant;
    private static IndexedHashMap<String, UserInformation> matchedUsers;
    private static HashMap<Integer, Boolean> userCheckedIn = new HashMap<>();
    private static final String TAG = "ShowMatch";
    private static Activity activity;
    private static ListView users;
    private static MatchedUsersListAdapter listAdapter;
    private static LocationMap locMap = new LocationMap();

    @Override
    public void onStart() {
        super.onStart();

        activity = getActivity();
        initMapButton();

    }

    @Override
    public void onResume() {
        super.onResume();

        if(isSetUp())
            showMatch();
    }

    @Override
    public void onStop() {
        super.onStop();
        resetMatch();
        MatchingDatabase.resetMatchListener();
    }

    public static void setRestaurant(String restaurantKey) {
        Log.d(TAG, "Searching for restaurant with key: " + restaurantKey);
        YelpApi.searchYelpApiForRestaurant(restaurantKey);
    }

    public static void setRestaurant(Business restaurant) {
        Log.d(TAG, "Restaurant set: " + restaurant.getName());
        ShowMatch.restaurant = restaurant;
        if (isSetUp()) showMatch();
    }

    private static void showMatch() {
        Log.d(TAG, "showing match in restaurant: " + restaurant.getName());
        Log.d(TAG, "with users: ");
        for (int i = 0; i < matchedUsers.size(); i++) {
            Log.d(TAG, matchedUsers.getItemAtPosition(i).toString());
        }

        ImageView imgView = activity.findViewById(R.id.restaurantImage);
        new DownloadImageTask(imgView).execute(restaurant.getImageUrl());

        TextView restaurantName = activity.findViewById(R.id.restaurantTitle);
        TextView restaurantAddr = activity.findViewById(R.id.restaurantAddress);
        TextView restaurantDesc = activity.findViewById(R.id.restaurantDescription);

        restaurantName.setText(restaurant.getName());
        restaurantAddr.setText(restaurant.getLocation().getAddress1() + ", " + restaurant.getLocation().getCity());
        String categories = "";
        for (Category category : restaurant.getCategories()){
            categories += category.getTitle() + ", ";
        }
        if (categories.length() > 0) {
            categories = categories.substring(0, categories.length() - 2);
        }
        restaurantDesc.setText(categories);

        users = activity.findViewById(R.id.matched_users_list);
        listAdapter = new MatchedUsersListAdapter(activity, matchedUsers);
        users.setAdapter(listAdapter);

        users.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                users.removeOnLayoutChangeListener(this);
                if (userCheckedIn.size() > 0) {
                    for (String key : matchedUsers.keySet()) {
                        if(userCheckedIn.get(matchedUsers.getId(key)) != null) {
                            if (userCheckedIn.get(matchedUsers.getId(key)) == true) {
                                userCheckedInForMatch(key);
                            } else if (userCheckedIn.get(matchedUsers.getId(key)) == false) {
                                userCheckedOutForMatch(key);
                            }
                        }
                    }
                }
            }
        });

        listAdapter.notifyDataSetChanged();

    }

    public static void setUsers(IndexedHashMap<String, UserInformation> matchedUsers){
        Log.d(TAG, matchedUsers.size() + " users to show. setting up...");
        ShowMatch.matchedUsers = matchedUsers;
        if (isSetUp()) showMatch();
    }

    public static boolean isSetUp(){
        return restaurant != null && matchedUsers != null;
    }

    public static void userCheckedInForMatch(String key) {
        userCheckedIn.put(matchedUsers.getId(key), true);
        setCheckedImage(key, R.drawable.ic_action_checked);
    }

    public static void userCheckedOutForMatch(String key) {
        userCheckedIn.put(matchedUsers.getId(key), false);
        setCheckedImage(key, R.drawable.ic_action_cancelled);
    }

    private static void setCheckedImage(String key, int imageId){
        if(users != null && users.getCount() > 0) {
            Log.d(TAG, "View requested at position: " + matchedUsers.getId(key));
            //TODO WORAKROUND
            //TODO remove this (?!) -> it's here only to display usage of app from within one instance of android studio
            //if there are 3 apps running from this studio, they all will write into matchedUsers. this means this will hold 3 users
            //however each user will only display two users on its screen, meaning users has a length of 2, but matchedUsers of 3
            //in a real environment this would not happen since each user only puts the users in both arrays he actually needs to display
            int id = matchedUsers.getId(key);
            if(id > users.getCount() - 1) {
                id = users.getCount() - 1;
            }
            View view = users.getChildAt(id);
            if(view != null){
                view.findViewById(R.id.matched_user_checked_progress_bar).setVisibility(View.GONE);
                view.findViewById(R.id.matched_user_checked).setVisibility(View.VISIBLE);
                ((ImageView) view.findViewById(R.id.matched_user_checked)).setImageResource(imageId);
            }
        }
    }

    public void initMapButton() {

        // If Google Play services are OK, initialize the button to the map
        if(locMap.isServicesOK(this.getActivity())) {
            TextView btnMap = this.getActivity().findViewById(R.id.mapBtn);
            btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Navigate to MapActivity
                    Intent intent = new Intent(view.getContext(), MapActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public static void resetMatch(){
        restaurant = null;
        matchedUsers = null;
        userCheckedIn = new HashMap<>();
    }
}
