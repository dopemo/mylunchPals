package com.lunchpals.app.user;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lunchpals.app.activities.main.matching.ShowMatch;

import static com.lunchpals.app.user.User.getFirebaseUser;

/**
 * Created by moritz on 11/13/17.
 */

public class UserInformation{

    private static final String TAG = "UserInfo";

    private String userId;
    private String firstName;
    private String lastName;
    private String imageUrl;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUsername() {
        return username;
    }

    private String username;

    private static DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference userRef = mRootRef.child("users");

    public static UserInformation getUserInformation(String userID, boolean notifyCurrent){
        UserInformation userInfo = new UserInformation();
        userRef.child(userID).addListenerForSingleValueEvent(new UserValueEventListener(userInfo, notifyCurrent));
        Log.d(TAG, "new User created: " + userInfo.toString());
        return userInfo;
    }

    public String toString(){
        return this.firstName + " " + this.lastName;
    }

    private static class UserValueEventListener implements ValueEventListener {

        UserInformation userInfo;
        public boolean notifyCurrentUser = false;

        public UserValueEventListener(UserInformation userInfo, boolean notifyCurrentUser) {
            this.userInfo = userInfo;
            this.notifyCurrentUser = notifyCurrentUser;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            userInfo.userId = dataSnapshot.getKey();

            for (DataSnapshot userData : dataSnapshot.getChildren()){
                String val = (String) userData.getValue();
                switch (userData.getKey()){
                    case "firstname":
                        userInfo.firstName = val;
                        break;
                    case "lastname":
                        userInfo.lastName = val;
                        break;
                    case "username":
                        userInfo.username = val;
                        break;
                    case "imageurl":
                        userInfo.imageUrl = val;
                        break;
                    default:
                        Log.d(TAG, "Could not find any corresponding field for UserInfo with key: " + userData.getKey() + " and value: " + val);
                }
            }

            if(notifyCurrentUser)
                User.onUserUpdate();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
