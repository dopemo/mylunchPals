package com.lunchpals.app.activities.main.location;

import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Created by Albert JK on 12/13/2017.
 */

public class LocationTest {

    MapActivity mapActivity = new MapActivity();

    String[] permissions = new String[2];

    int[] grantResults = new int[2];

    @Test
    public void testLocation(){

        // Set test location
        Location location = new Location("");
        location.setLatitude(20.3);
        location.setLongitude(52.6);

        mapActivity.getLocmap().setCurrentLocation(location);

        // Test that the location is correctly set and returned
        assertEquals(mapActivity.getLocmap().getCurrentLocation(), location);
    }

    @Test
    public void testOnRequestPermissionsResult() {

        // Test that permissions is false by default
        assertFalse(mapActivity.getLocmap().ismLocationPermissionsGranted());

        // Set test permissions and results
        permissions[0] = "A";

        grantResults[0] = PackageManager.PERMISSION_GRANTED;

        mapActivity.onRequestPermissionsResult(LocationMap.LOCATION_PERMISSION_REQUEST_CODE, permissions, grantResults);

        // Test that if permission was granted variable now becomes true
        assertTrue(mapActivity.getLocmap().ismLocationPermissionsGranted());

    }
}