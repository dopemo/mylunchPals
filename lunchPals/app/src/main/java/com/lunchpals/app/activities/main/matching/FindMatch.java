package com.lunchpals.app.activities.main.matching;

/**
 * Created by moritz on 10/2/17.
 */


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lunchpals.app.R;
import com.lunchpals.app.activities.main.MainActivity;
import com.lunchpals.app.activities.main.MainFragment;
import com.lunchpals.app.activities.main.filter.FilterEntry;
import com.lunchpals.app.activities.main.location.LocationMap;
import com.lunchpals.app.cache.FilterCache;
import com.lunchpals.app.user.User;
import com.lunchpals.app.utils.DownloadImageTask;

public class FindMatch extends MainFragment {

    private static final String TAG = "FindMatch";

    private static ImageView profilePic;
    private static TextView nameView;
    private static TextView locationView;
    private static TextView cuisineInfo;
    private static TextView priceInfo;
    private static TextView radiusInfo;
    private static View view;

    private LocationMap locMap = new LocationMap();

    @Override
    public void onStart() {
        super.onStart();

        //Setup Location of Device
        if(locMap.isServicesOK(this.getActivity())){
            Log.d(TAG, "requesting Location...");
            locMap.getDeviceLocation(this.getActivity());
        }else {
            locMap.getLocationPermission(this.getActivity());
        }

        view = getView();
        profilePic = getActivity().findViewById(R.id.profile_picture);
        nameView = getActivity().findViewById(R.id.user_name);
        locationView = getActivity().findViewById(R.id.location_name);
        cuisineInfo = getActivity().findViewById(R.id.filter_information_cuisines);
        priceInfo = getActivity().findViewById(R.id.filter_information_price);
        radiusInfo = getActivity().findViewById(R.id.filter_information_radius);

        updateUserInformation();
        updateFilterInformtion();

        Button matchMeBtn = getActivity().findViewById(R.id.match_me_btn);
        matchMeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setContent(MainFragment.SEARCH_MATCH);
            }
        });

        LinearLayout filterInfo = getActivity().findViewById(R.id.filter_information);
        filterInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setContent(MainFragment.FILTER_LAYOUT);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public static void updateUserInformation(){
        if (profilePic != null){
            Log.d(TAG, "Current User ID: " + User.getFirebaseUser().getUid());
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + User.getFirebaseUser().getUid());
            Glide.with(view.getContext() /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(profilePic);
        }
        if(view != null && nameView != null && locationView != null &&
                User.getCurrentUserInformation().getFirstName() != null) {
            String greetings = "";
            greetings += view.getContext().getResources().getString(R.string.Hi) + " ";
            greetings += User.getCurrentUserInformation().getFirstName();
            greetings += "!";
            nameView.setText(greetings);
            Location loc = LocationMap.getCurrentLocation();
            if(loc != null) {
                // To see if location is working -> it is!! For now keep hard coded
                //TODO built API request to maps api using generated loc to show city name
                // locationView.setText(loc.toString());
            }

        }

    }

    public static void updateFilterInformtion(){
        //check if everything has been initialized correctly
        if(cuisineInfo != null && priceInfo != null && radiusInfo != null && FilterCache.getFilters() != null){
            //go through all filterEntries (this is scalable)
            for(FilterEntry filterEntry : FilterCache.getFilters()) {
                //check if the entry is enabled - if not skip this one
                if(filterEntry.isEnabled()) {
                    //check if filter has any value (empty cuisines won't be displayed for example)
                    if (!filterEntry.getDescription().equals("")) {
                        //get the title of the filter and write its value into the corresponding text view
                        switch (filterEntry.getTitle()) {
                            case R.string.cuisine:
                                cuisineInfo.setText(filterEntry.getDescription());
                                break;
                            case R.string.price:
                                priceInfo.setText(filterEntry.getDescription());
                                break;
                            case R.string.radius:
                                radiusInfo.setText(filterEntry.getDescription());
                                break;
                        }
                    }
                }
            }
        }
    }
}
