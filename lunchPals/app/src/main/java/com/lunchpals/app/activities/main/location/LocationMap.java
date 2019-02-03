package com.lunchpals.app.activities.main.location;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.lunchpals.app.R;
import com.lunchpals.app.requests.RestaurantSearchRequest;

/**
 * Created by moritz on 12/12/17.
 */

public class LocationMap {

    private static final String TAG = "LocationMap";

    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    // Just some location permission code
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    public void setmLocationPermissionsGranted(boolean mLocationPermissionsGranted) {
        this.mLocationPermissionsGranted = mLocationPermissionsGranted;
    }

    private FusedLocationProviderClient mFusedLocationProviderClient;

    public boolean ismLocationPermissionsGranted() {
        return mLocationPermissionsGranted;
    }

    private boolean mLocationPermissionsGranted = false;

    // This error is handled if the user's phone does not have the correct version of Google Play services
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static Location location;


    // Explicitly check permissions
    public void getLocationPermission(Activity activity) {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        if(ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocationPermission: location permission granted");
            mLocationPermissionsGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(activity,
                    permissions, LOCATION_PERMISSION_REQUEST_CODE);
            Log.d(TAG, "getLocationPermission: request location permissions");
        }
    }

    // Check that the user's phone has the correct version of Google Play services installed
    public boolean isServicesOK(Activity activity) {
        Log.d(TAG, "isServicesOK: Checking Google Play services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);

        // If everything is fine, the user can make map requests
        if(available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOK: Google Play services is working");
            return true;
        }
        // If there is an error but it is resolvable, point the user where they can resolve it
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: An error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        // Unresolvable error
        else {
            Toast.makeText(activity, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void getDeviceLocation(final Activity activity) {
        Log.d(TAG, "getDeviceLocation: Getting the current location of the device");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        getLocationPermission(activity);

        try {
            if (mLocationPermissionsGranted) {
                //IF TESTING ON EMULATOR MAKE SURE THERE IS ONE LAST LOCATION (I.E. LAUNCH MAPS APP FIRST)
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // If the task was successful, the location is found
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            Log.d(TAG, "onComplete: Found current location: " + currentLocation);
                            if(currentLocation != null) {
                                if(activity instanceof MapActivity)
                                    ((MapActivity)activity).moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                                setCurrentLocation(currentLocation);
                                RestaurantSearchRequest.setCurrentLocation(currentLocation);
                            }
                        }
                        // Otherwise, the location was not found
                        else {
                            Log.d(TAG, "onComplete: Current location is null");
                            if(activity != null)
                                Toast.makeText(activity, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    public static void setCurrentLocation(Location newLocation){
        location = newLocation;
    }

    public static Location getCurrentLocation() {
        return location;
    }
}
