package com.lunchpals.app.user;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lunchpals.app.activities.login.LoginActivity;
import com.lunchpals.app.activities.main.MainFragment;
import com.lunchpals.app.activities.main.matching.FindMatch;

/**
 * Created by Mohamad on 11/1/2017.
 */

public class User{

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static UserInformation currentUserInformation;

    public static FirebaseAuth getFirebaseAuth(){
        return firebaseAuth;
    }

    public static FirebaseUser getFirebaseUser(){
        return firebaseAuth.getCurrentUser();
    }



    public static boolean isOnline(Context context){
        if(getFirebaseUser() != null){
            return true;
        }else{
            Toast.makeText(context, "You need to login to proceed.", Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
    }


    public static UserInformation getCurrentUserInformation(){
        if(currentUserInformation == null)
            currentUserInformation = UserInformation.getUserInformation(getFirebaseUser().getUid(), true);

        return currentUserInformation;
    }

    public static void resetUserInformation(){
        currentUserInformation = null;
    }

    public static void onUserUpdate(){
        FindMatch.updateUserInformation();
    }



}

