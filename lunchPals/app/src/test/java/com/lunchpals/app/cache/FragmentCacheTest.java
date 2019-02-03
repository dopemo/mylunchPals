package com.lunchpals.app.cache;

import com.lunchpals.app.activities.main.MainFragment;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by moritz on 12/14/17.
 */
public class FragmentCacheTest {

    String key = "test";
    int id = 2842;

    @Test
    public void getFragment() throws Exception {
        FragmentCache.setFragment(key, id);
        assertNotNull(FragmentCache.getFragment(key));
        assertEquals(FragmentCache.getFragment(key), id);
    }

    @Test
    public void clearFragmentCache() throws Exception {
        FragmentCache.setFragment(key, id);
        assertNotNull(FragmentCache.getFragment(key));
        FragmentCache.clearFragmentCache();
        assertEquals(FragmentCache.getFragment(key), MainFragment.DEFAULT_LAYOUT);
    }

}