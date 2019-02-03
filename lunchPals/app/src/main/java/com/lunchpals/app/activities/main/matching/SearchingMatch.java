package com.lunchpals.app.activities.main.matching;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lunchpals.app.R;
import com.lunchpals.app.activities.main.MainActivity;
import com.lunchpals.app.activities.main.MainFragment;
import com.lunchpals.app.utils.DownloadImageTask;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by moritz on 11/13/17.
 */

public class SearchingMatch extends MainFragment {

    private static final String TAG = "SearchingMatch";

    private static final int NUM_OF_IMAGE_VIEWS = 3;

    private static View view;
    private static Timer timer;

    private static SearchResponse searchResponse;

    private static boolean cancelled = false;

    @Override
    public void onStart() {
        super.onStart();
        view = getView();
        YelpApi.searchYelpApi();
        Button btn = getActivity().findViewById(R.id.cancelMatchSearchBtn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cancelled = true;
                ((MainActivity)getActivity()).setContent(MainFragment.DEFAULT_LAYOUT);
            }
        });

    }

    @Override
    public void onStop() {
        if(!cancelled){
            showToastCancellingSearch();
        }
        timer.cancel();
        MatchingDatabase.removeSearchEntries();
        super.onStop();
    }

    public static void matchFound(){
        ((MainActivity)view.getContext()).setContent(MainFragment.SHOW_MATCH);
    }

    public static void startSearch(final SearchResponse searchResponse) {
        SearchingMatch.searchResponse = searchResponse;
        int size = searchResponse.getBusinesses().size();
        Log.d(TAG, "Found " + size + " restaurants to create search for.");

        //api has found some restaurants that match filter criteria
        if (size > 0) {
            //very few restaurants found
            if (size > 0 && size < 3) {
                //inform user that only a few restaurants have been found and ask if they want to set different filters#
                showDialogToConsiderFilterChange();

            // enough restaurants found to start searching
            } else {
                createSearchEntry();
            }
        } else {
            //TODO inform user that filter need to be adjusted, no response from API
            Log.d(TAG, "Found no restaurants. Change filters.");
            showDialogNoRestaurantsFound();
        }
    }

    private static void createSearchEntry(){
        //adding restaurant requests to database
        MatchingDatabase.createSearchEntry(searchResponse);

        //create list for image urls and diplay images
        final ArrayList<String> restaurantImageUrls = new ArrayList<>();
        for (Business restaurant : searchResponse.getBusinesses()) {
            restaurantImageUrls.add(restaurant.getImageUrl());
        }

        fillImageViews(restaurantImageUrls);
    }

    private static void fillImageViews(ArrayList<String> imageUrls) {
        int counter = 0;
        for (int imageView = 0; imageView < NUM_OF_IMAGE_VIEWS; imageView++) {
            if (counter > imageUrls.size() - 1) {
                counter = 0;
            }
            changeImageTo(imageUrls.get(counter++), imageView);
        }

        //start a new thread to cycle through the restaurant images
        startImageCycle(imageUrls);
    }

    private static void startImageCycle(final ArrayList<String> imageUrls) {
        timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3);
        timer.schedule(new TimerTask() {
            int counter = 0;
            int imageViewId = 0;

            @Override
            public void run() {
                counter++;
                imageViewId++;
                if (counter > imageUrls.size() - 1) counter = 0;
                Log.d(TAG, "Counter: " + counter + ", ImageView to change: " + imageViewId % NUM_OF_IMAGE_VIEWS);
                changeImageTo(imageUrls.get(counter), imageViewId % NUM_OF_IMAGE_VIEWS);
            }
        }, calendar.getTime(), 3000);
    }

    //TODO increase efficiency by implementing cache or using glide (glide needs to be called from main thread)
    private static void changeImageTo(String url, int imageView){
        int id = R.id.search_restaurant_image_0;
        switch (imageView){
            case 0:
                id = R.id.search_restaurant_image_0;
                break;
            case 1:
                id = R.id.search_restaurant_image_1;
                break;
            case 2:
                id = R.id.search_restaurant_image_2;
                break;
        }
        Log.d(TAG, "ImageView ID: " + id + ", image to load: " + url);
        new DownloadImageTask((ImageView) view.findViewById(id)).execute(url);
    }

    private static void showDialogNoRestaurantsFound() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        dialogBuilder.setMessage(R.string.filter_need_to_be_changed)
                .setTitle(R.string.no_restaurants_found)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((MainActivity) view.getContext()).setContent(MainFragment.DEFAULT_LAYOUT);
                    }
                })
                .setPositiveButton(R.string.back_and_change_filters, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((MainActivity) view.getContext()).setContent(MainFragment.FILTER_LAYOUT);
                    }
                })
                .create()
                .show();
    }

    private static void showDialogToConsiderFilterChange() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        dialogBuilder.setMessage(R.string.consider_changing_filters)
                .setTitle(R.string.few_restaurants_found)
                .setNegativeButton(R.string.continue_searching, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createSearchEntry();
                    }
                })
                .setPositiveButton(R.string.back_and_change_filters, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((MainActivity) view.getContext()).setContent(MainFragment.FILTER_LAYOUT);
                    }
                })
                .create()
                .show();
    }

    private void showToastCancellingSearch() {
        Toast cancelSearchToast = Toast.makeText(view.getContext(), R.string.cancel_searching, Toast.LENGTH_SHORT);
        cancelSearchToast.show();
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
//        dialogBuilder.setMessage(R.string.really_want_to_cancel_searching)
//                .setTitle(R.string.cancel_searching)
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        onStop();
//                    }
//                })
//                .setPositiveButton(R.string.continue_searching, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        onResume();
//                    }
//                })
//                .create()
//                .show();
    }
}
