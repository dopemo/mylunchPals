package com.lunchpals.app.activities.main.matching;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lunchpals.app.requests.RestaurantSearchRequest;
import com.lunchpals.app.user.User;
import com.lunchpals.app.user.UserInformation;
import com.lunchpals.app.utils.IndexedHashMap;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moritz on 11/9/17.
 */

public class MatchingDatabase {

    private static final String TAG = "DATABASE";
    private static DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference newMatchRequestFor;
    private static DatabaseReference matchedRef;

    private static List<String> restaurantKeys = new ArrayList<>();
    private static List<DatabaseReference> databaseSearchEntries = new ArrayList<>();

    private static MatchedValueListener matchedValueListener = new MatchedValueListener();

    private static String userId;

    public static void createSearchEntry(SearchResponse searchResponse) {
        userId = User.getFirebaseUser().getUid();
        matchedRef = mRootRef.child("matched/" + userId);
        for (Business restaurant : searchResponse.getBusinesses()){
            restaurantKeys.add(restaurant.getId());
        }
        writeToDatabase();
    }

    private static void writeToDatabase(){
        matchedRef.addValueEventListener(matchedValueListener);
        newMatchRequestFor = mRootRef.child("newMatchRequestFor").child("" + RestaurantSearchRequest.getGroupSize());
        for(String key : restaurantKeys) {
            DatabaseReference newRef = newMatchRequestFor.child(key).child(userId);
            newRef.setValue(userId);

           // newRef.getParent().addValueEventListener(searchingValueListener);
            databaseSearchEntries.add(newRef);
        }
    }

    public static void removeSearchEntries(){
        Log.d(TAG, "removing search entries...");
        for (DatabaseReference ref : databaseSearchEntries){
            ref.removeValue();
        }
        Log.d(TAG, "Clear restaurant key list...");
        restaurantKeys = new ArrayList<>();
    }

    public static void resetMatchListener(){
        matchedValueListener.resetListener();
    }

    private static class MatchedValueListener implements ValueEventListener {

        private static boolean matchFound = false;

        public void resetListener(){
            matchFound = false;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //prevent listener from firing when adding itself to this path
            if(dataSnapshot.getChildrenCount() > 0 && !matchFound) {
                matchFound = true;
                Log.d(TAG, "Found match for my UID: " + userId + " at: " + dataSnapshot.child("restaurant").getValue());
                //display the match
                //get the restaurant that's the matches place
                ShowMatch.setRestaurant((String) dataSnapshot.child("restaurant").getValue());

                //get all other users we got matched with
                Log.d(TAG, "Matching with the following users: ");
                IndexedHashMap<String, UserInformation> userMap = new IndexedHashMap<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    if(!child.getKey().equals("restaurant")){
                        Log.d(TAG, child.getKey());
                        userMap.put(child.getKey(), UserInformation.getUserInformation(child.getKey(), false));
                        //notify the users that I picked this match up
                        mRootRef.child("matched").child(child.getKey()).child(userId).setValue("y");
                        matchedRef.child(child.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "Subscribed User triggered change: " + dataSnapshot.getKey() + " with " + dataSnapshot.getValue());
                                //listen to deletions first as null check
                                if (dataSnapshot.getValue() == null || dataSnapshot.getValue().equals("n")) {
                                    matchedRef.child(dataSnapshot.getKey()).removeEventListener(this);
                                    ShowMatch.userCheckedOutForMatch(dataSnapshot.getKey());
                                }else if (dataSnapshot.getValue().equals("y")) {
                                    matchedRef.child(dataSnapshot.getKey()).removeEventListener(this);
                                    ShowMatch.userCheckedInForMatch(dataSnapshot.getKey());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                ShowMatch.setUsers(userMap);


                //inform that no longer need to be searching and match was found
                SearchingMatch.matchFound();
            }


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    }
}
