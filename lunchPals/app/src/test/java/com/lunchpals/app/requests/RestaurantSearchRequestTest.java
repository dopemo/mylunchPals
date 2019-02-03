package com.lunchpals.app.requests;

import android.location.Location;
import android.os.Parcel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by moritz on 12/14/17.
 */
public class RestaurantSearchRequestTest {

    String key = "test";
    String value = "value";

    @Test
    public void removeFilter() throws Exception {
        RestaurantSearchRequest.setFilter(key, value);
        assertEquals(RestaurantSearchRequest.getFilters().get(key), value);
        RestaurantSearchRequest.removeFilter(key);
        assertFalse(RestaurantSearchRequest.getFilters().containsKey(key));
    }

    @Test
    public void getFilters() throws Exception {
        assertNotNull(RestaurantSearchRequest.getFilters());
        assertTrue(RestaurantSearchRequest.getFilters().containsKey(RestaurantSearchRequest.TERM));
    }

    @Test
    public void getGroupSize() throws Exception {
        //Group Size should be set to three by default
        assertEquals(RestaurantSearchRequest.getGroupSize(), 3);
    }

    @Test
    public void getLimit() throws Exception {
        //Limit should be set to five by default
        assertEquals(RestaurantSearchRequest.getLimit(), 5);
    }

    @Test
    public void setCurrentLocation() throws Exception {
        Location loc = new Location("dummyprovider");
        assertNull(RestaurantSearchRequest.getFilters().get(RestaurantSearchRequest.LATITUDE));
        assertNull(RestaurantSearchRequest.getFilters().get(RestaurantSearchRequest.LONGITUDE));
        RestaurantSearchRequest.setCurrentLocation(loc);
        assertNotNull(RestaurantSearchRequest.getFilters().get(RestaurantSearchRequest.LATITUDE));
        assertNotNull(RestaurantSearchRequest.getFilters().get(RestaurantSearchRequest.LONGITUDE));
    }

}