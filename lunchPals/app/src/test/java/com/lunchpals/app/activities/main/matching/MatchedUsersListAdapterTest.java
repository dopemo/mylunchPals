package com.lunchpals.app.activities.main.matching;

import android.app.Activity;

import com.lunchpals.app.user.UserInformation;
import com.lunchpals.app.utils.IndexedHashMap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by moritz on 12/14/17.
 */
public class MatchedUsersListAdapterTest {
    MatchedUsersListAdapter listAdapter;
    IndexedHashMap<String, UserInformation> users;

    String usr1 = "usr1";
    UserInformation userInformation = null;

    @Before
    public void setUp(){
        users = new IndexedHashMap<>();
        users.put(usr1, userInformation);
        listAdapter = new MatchedUsersListAdapter(new Activity(), users);
    }

    @Test
    public void getCount() throws Exception {
        assertEquals(1, listAdapter.getCount());
    }

    @Test
    public void getItem() throws Exception {
        assertEquals(userInformation, listAdapter.getItem(0));
    }

    @Test
    public void getItemId() throws Exception {
        assertEquals(0, listAdapter.getItemId(0));
    }

}