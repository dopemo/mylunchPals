package com.lunchpals.app.activities.main.matching;

import com.lunchpals.app.user.UserInformation;
import com.lunchpals.app.utils.IndexedHashMap;
import com.yelp.fusion.client.models.Business;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by moritz on 12/14/17.
 */
public class ShowMatchTest {

    @Test
    public void setRestaurant() throws Exception {
        ShowMatch.resetMatch();
        ShowMatch.setRestaurant(new Business());
        assertFalse(ShowMatch.isSetUp());
    }

    @Test
    public void setUsers() throws Exception {
        ShowMatch.resetMatch();
        ShowMatch.setUsers(new IndexedHashMap<String, UserInformation>());
        assertFalse(ShowMatch.isSetUp());
    }

}